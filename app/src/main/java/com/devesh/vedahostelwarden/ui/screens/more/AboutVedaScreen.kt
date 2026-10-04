package com.devesh.vedahostelwarden.ui.screens.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*

@Composable
fun AboutVedaScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            VedaTopBar(
                title = "About VEDA",
                subtitle = "Hostel Management System",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            VedaCard {
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
                        Icon(Icons.Filled.Shield, contentDescription = null, tint = VedaTextPrimary, modifier = Modifier.size(40.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("VEDA Hostel Warden", style = MaterialTheme.typography.titleLarge.copy(color = VedaTextPrimary, fontWeight = FontWeight.Bold))
                    Text("Version 1.0.0 (Phase 1)", style = MaterialTheme.typography.bodySmall.copy(color = VedaBlueLight))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "VEDA is a comprehensive Hostel Management System designed for seamless administration, real-time attendance tracking, student activation, notices, mess management, and hostel security.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = VedaTextSecondary),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("© 2026 VEDA Systems. All rights reserved.", style = MaterialTheme.typography.bodySmall.copy(color = VedaTextMuted))
                }
            }
        }
    }
}
