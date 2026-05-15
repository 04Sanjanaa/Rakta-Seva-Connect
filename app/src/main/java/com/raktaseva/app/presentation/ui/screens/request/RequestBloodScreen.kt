package com.raktaseva.app.presentation.ui.screens.request

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.hilt.navigation.compose.hiltViewModel
import com.raktaseva.app.utils.Resource

import androidx.compose.ui.res.stringResource
import com.raktaseva.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestBloodScreen(
    onNavigateBack: () -> Unit,
    viewModel: RequestBloodViewModel = hiltViewModel()
) {
    var bloodGroup by remember { mutableStateOf("") }
    var hospitalName by remember { mutableStateOf("") }
    var unitsRequired by remember { mutableStateOf("1") }
    var contactNumber by remember { mutableStateOf("") }
    var urgencyLevel by remember { mutableStateOf("Normal") }
    var expandedGroup by remember { mutableStateOf(false) }
    var expandedUrgency by remember { mutableStateOf(false) }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
    val urgencyLevels = listOf("Normal", "Urgent", "Critical")
    val requestState by viewModel.requestState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val successMsg = stringResource(R.string.request_submitted_success)

    LaunchedEffect(requestState) {
        if (requestState is Resource.Success && (requestState as Resource.Success<Boolean>).data == true) {
            snackbarHostState.showSnackbar(successMsg)
            onNavigateBack()
        } else if (requestState is Resource.Error) {
            snackbarHostState.showSnackbar((requestState as Resource.Error).message ?: "Error submitting request")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.request_blood_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedGroup,
                onExpandedChange = { expandedGroup = !expandedGroup },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = bloodGroup,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.blood_group_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedGroup,
                    onDismissRequest = { expandedGroup = false }
                ) {
                    bloodGroups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group) },
                            onClick = {
                                bloodGroup = group
                                expandedGroup = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = hospitalName,
                onValueChange = { hospitalName = it },
                label = { Text(stringResource(R.string.hospital_name_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = unitsRequired,
                    onValueChange = { unitsRequired = it },
                    label = { Text(stringResource(R.string.units_label)) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedUrgency,
                    onExpandedChange = { expandedUrgency = !expandedUrgency },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = urgencyLevel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.urgency_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUrgency) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedUrgency,
                        onDismissRequest = { expandedUrgency = false }
                    ) {
                        urgencyLevels.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    urgencyLevel = level
                                    expandedUrgency = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it },
                label = { Text(stringResource(R.string.contact_number_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (bloodGroup.isNotBlank() && hospitalName.isNotBlank() && contactNumber.isNotBlank()) {
                        viewModel.submitRequest(
                            bloodGroup = bloodGroup,
                            hospitalName = hospitalName,
                            unitsRequired = unitsRequired.toIntOrNull() ?: 1,
                            contactNumber = contactNumber,
                            urgencyLevel = urgencyLevel
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = requestState !is Resource.Loading
            ) {
                if (requestState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.submit_emergency_request))
                }
            }
        }
    }
}
