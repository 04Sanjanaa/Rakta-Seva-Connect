package com.raktaseva.app.presentation.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.res.stringResource
import com.raktaseva.app.R

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
                title = { Text(stringResource(R.string.dashboard_title)) },
                actions = {
                    IconButton(onClick = onNavigateToAssistant) {
                        Icon(
                            imageVector = Icons.Default.Person, // Using Person as a placeholder for AI/Assistant
                            contentDescription = stringResource(R.string.ai_assistant_description),
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
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_request_description)
                )
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
                text = stringResource(R.string.emergency_requests_title),
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
                            Text(stringResource(R.string.no_requests_found))
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
                    text = stringResource(R.string.blood_group_display, request.bloodGroup),
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
            Text(
                text = stringResource(R.string.hospital_display, request.hospitalName),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.units_display, request.unitsRequired.toString()),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (request.status == "Accepted") {
                if (request.acceptedByDonorId == currentUserId) {
                    Text(
                        text = stringResource(R.string.contact_display, request.contactNumber),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.already_accepted),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.accept_request))
                }
            }
        }
    }
}
