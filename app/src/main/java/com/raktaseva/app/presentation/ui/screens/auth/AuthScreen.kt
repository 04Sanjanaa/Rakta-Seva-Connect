package com.raktaseva.app.presentation.ui.screens.auth

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.raktaseva.app.R

@Composable
fun AuthScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToProfileSetup: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> onNavigateToDashboard()
            is AuthState.NeedsProfileSetup -> onNavigateToProfileSetup()
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.auth_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (authState is AuthState.Idle || authState is AuthState.Error) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text(stringResource(R.string.phone_number_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.verifyPhoneNumber(phoneNumber, context as Activity) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.send_otp))
            }
        } else if (authState is AuthState.CodeSent) {
            OutlinedTextField(
                value = otpCode,
                onValueChange = { otpCode = it },
                label = { Text(stringResource(R.string.otp_code_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.verifyCode(otpCode) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.verify))
            }
        }

        if (authState is AuthState.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        if (authState is AuthState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
