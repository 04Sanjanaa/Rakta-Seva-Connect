package com.raktaseva.app.presentation.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.raktaseva.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onNavigateToDashboard: () -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
    val setupState by viewModel.setupState.collectAsState()

    LaunchedEffect(setupState) {
        if (setupState is Resource.Success && (setupState as Resource.Success<Boolean>).data == true) {
            onNavigateToDashboard()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.profile_setup_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.profile_setup_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.full_name_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = bloodGroup,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.blood_group_label)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    bloodGroups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group) },
                            onClick = {
                                bloodGroup = group
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text(stringResource(R.string.city_area_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && bloodGroup.isNotBlank() && location.isNotBlank()) {
                        viewModel.saveProfile(name, bloodGroup, location)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = setupState !is Resource.Loading
            ) {
                if (setupState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.save_and_continue))
                }
            }

            if (setupState is Resource.Error) {
                Text(
                    text = (setupState as Resource.Error).message ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
