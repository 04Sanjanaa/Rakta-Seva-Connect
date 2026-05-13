const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();
const db = admin.firestore();

// 1. FCM Notification on New Emergency Request
exports.onEmergencyRequestCreated = functions.firestore
  .document("blood_requests/{requestId}")
  .onCreate(async (snap, context) => {
    const requestData = snap.data();
    const { latitude, longitude, bloodGroup, hospitalName, requestId, requesterId } = requestData;
    
    // Find all potential donors of the same blood group
    const donorsSnapshot = await db.collection("users")
      .where("bloodGroup", "==", bloodGroup)
      .where("isAvailable", "==", true)
      .get();
      
    const tokens = [];
    const RADIUS_KM = 10;

    donorsSnapshot.forEach((doc) => {
      const user = doc.data();
      
      // Basic distance check (Haversine formula)
      if (user.latitude && user.longitude && user.fcmToken && user.uid !== requesterId) {
        const distance = calculateDistance(
          latitude, longitude,
          user.latitude, user.longitude
        );
        
        if (distance <= RADIUS_KM) {
          tokens.push(user.fcmToken);
        }
      }
    });

    if (tokens.length === 0) return null;

    const payload = {
      notification: {
        title: `URGENT: ${bloodGroup} Blood Needed!`,
        body: `Required at ${hospitalName}. Tap to help save a life.`,
      },
      data: {
        requestId: requestId,
        click_action: "FLUTTER_NOTIFICATION_CLICK" // For Android to open the app correctly
      }
    };

    return admin.messaging().sendToDevice(tokens, payload);
  });

function calculateDistance(lat1, lon1, lat2, lon2) {
  const R = 6371; // km
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLon = (lon2 - lon1) * Math.PI / 180;
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c;
}

// 2. Scheduled Cron Job for 90-Day Cooldown Reset
exports.resetCooldownDaily = functions.pubsub
  .schedule("every 24 hours")
  .onRun(async (context) => {
    const now = Date.now();
    const cooldownPeriod = 90 * 24 * 60 * 60 * 1000; // 90 days in ms
    
    const unavailableDonors = await db.collection("users")
      .where("isAvailable", "==", false)
      .get();
      
    const batch = db.batch();
    
    unavailableDonors.forEach((doc) => {
      const user = doc.data();
      if (user.lastDonationDate && (now - user.lastDonationDate > cooldownPeriod)) {
        batch.update(doc.ref, { isAvailable: true });
      }
    });
    
    return batch.commit();
  });
