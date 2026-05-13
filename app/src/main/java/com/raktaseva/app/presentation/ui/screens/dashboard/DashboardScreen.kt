package com.raktaseva.app.presentation.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raktaseva.app.domain.models.BloodRequest
import com.raktaseva.app.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToRequest: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val requestsState by viewModel.requestsState.collectAsState()
    val currentUserId = viewModel.currentUserId

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rakta-Seva Dashboard") },
                actions = {
                    IconButton(onClick = onNavigateToAssistant) {
                        Icon(
                            imageVector = androidx.compose.material.icons.filled.Person, // Using Person as a placeholder for AI/Assistant
                            contentDescription = "AI Assistant",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToRequest) {
                Icon(Icons.Default.Add, contentDescription = "New Request")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Emergency Blood Requests",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (val state = requestsState) {
                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    val requests = state.data ?: emptyList()
                    if (requests.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No active requests found.")
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(requests) { request ->
                                BloodRequestCard(
                                    request = request,
                                    currentUserId = currentUserId,
                                    onAccept = { viewModel.acceptRequest(request.requestId) }
                                )
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun BloodRequestCard(
    request: BloodRequest,
    currentUserId: String?,
    onAccept: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Blood Group: ${request.bloodGroup}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Badge(
                    containerColor = when(request.urgencyLevel) {
                        "Critical" -> MaterialTheme.colorScheme.error
                        "Urgent" -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.secondary
                    }
                ) {
                    Text(request.urgencyLevel, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Hospital: ${request.hospitalName}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Units Required: ${request.unitsRequired}", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (request.status == "Accepted") {
                if (request.acceptedByDonorId == currentUserId) {
                    Text(
                        text = "Contact: ${request.contactNumber}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(text = "Already Accepted", color = MaterialTheme.colorScheme.outline)
                }
            } else {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Accept Request")
                }
            }
        }
    }
}
