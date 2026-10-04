package com.devesh.vedahostelwarden.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel,
    onNavigateNext: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.initSession(context)
        delay(1200)
        onNavigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        VedaNavyBackground,
                        VedaNavySurface,
                        VedaNavyBackground
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(VedaBlueLight, VedaBluePrimary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = "VEDA Warden Logo",
                    tint = VedaTextPrimary,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "VEDA",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = VedaTextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            )

            Text(
                text = "Hostel Warden",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = VedaBlueLight,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Manage. Monitor. Support.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = VedaTextSecondary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "For a better hostel life.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = VedaTextMuted,
                    fontSize = 13.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                color = VedaBlueLight,
                modifier = Modifier.size(28.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
