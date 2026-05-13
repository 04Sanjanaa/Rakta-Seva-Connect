const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();
const db = admin.firestore();

// 1. FCM Notification on New Emergency Request
exports.onEmergencyRequestCreated = functions.firestore
  .document("blood_requests/{requestId}")
  .onCreate(async (snap, context) => {
    const requestData = snap.data();
    
    // Find nearby eligible donors (simplified - ignoring distance in this snippet for brevity, filtering by blood group & availability)
    const donorsSnapshot = await db.collection("users")
      .where("bloodGroup", "==", requestData.bloodGroup)
      .where("isAvailable", "==", true)
      .get();
      
    const tokens = [];
    donorsSnapshot.forEach((doc) => {
      const user = doc.data();
      if (user.fcmToken && user.uid !== requestData.requesterId) {
        tokens.push(user.fcmToken);
      }
    });

    if (tokens.length === 0) return null;

    const payload = {
      notification: {
        title: `URGENT: ${requestData.bloodGroup} Blood Needed!`,
        body: `Required at ${requestData.hospitalName}. Tap to view details.`,
      },
      data: {
        requestId: context.params.requestId,
      }
    };

    return admin.messaging().sendToDevice(tokens, payload);
  });

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
