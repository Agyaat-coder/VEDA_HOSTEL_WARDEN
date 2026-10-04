package com.devesh.vedahostelwarden.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.devesh.vedahostelwarden.ui.components.VedaButton
import com.devesh.vedahostelwarden.ui.components.VedaCard
import com.devesh.vedahostelwarden.ui.components.VedaTextField
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val isAuthenticating by authViewModel.isAuthenticating.collectAsState()

    var email by remember { mutableStateOf("warden@veda.edu.in") }
    var password by remember { mutableStateOf("warden123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VedaNavyBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(VedaBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = VedaTextPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = VedaTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Warden",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = VedaBlueLight,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sign in to manage your hostel",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = VedaTextSecondary
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            VedaCard {
                VedaTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = "Email Address",
                    placeholder = "warden@veda.edu.in",
                    leadingIcon = Icons.Filled.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                VedaTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = "Password",
                    placeholder = "••••••••",
                    leadingIcon = Icons.Filled.Lock,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "Toggle Password Visibility",
                                tint = VedaTextSecondary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = VedaBlueLight,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.clickable {
                            errorMessage = "Password reset instructions sent to $email"
                        }
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = if (errorMessage!!.contains("sent")) VedaSuccessGreen else VedaDangerRed,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                VedaButton(
                    text = if (isAuthenticating) "Signing in..." else "Sign In",
                    enabled = !isAuthenticating,
                    onClick = {
                        if (email.isBlank()) {
                            errorMessage = "Enter your email."
                        } else if (password.isBlank()) {
                            errorMessage = "Enter your password."
                        } else {
                            authViewModel.login(
                                context = context,
                                email = email,
                                pass = password,
                                onSuccess = onLoginSuccess,
                                onError = { errorMessage = it }
                            )
                        }
                    }
                )
            }
        }
    }
}
