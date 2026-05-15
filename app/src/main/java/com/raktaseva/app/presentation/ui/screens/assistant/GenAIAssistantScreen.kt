package com.raktaseva.app.presentation.ui.screens.assistant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.stringResource
import com.raktaseva.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenAIAssistantScreen() {
    var query by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.assistant_title)) },
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
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // Chat history will be displayed here
            }
            
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.ask_eligibility_label)) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* Send query to Gemini API */ }) {
                    Text(stringResource(R.string.send_button))
                }
            }
        }
    }
}
