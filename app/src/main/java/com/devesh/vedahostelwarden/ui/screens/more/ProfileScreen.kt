package com.devesh.vedahostelwarden.ui.screens.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.screens.students.DetailItem
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val warden by profileViewModel.warden.collectAsState()
    val hostel by profileViewModel.hostel.collectAsState()

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Warden Profile",
                subtitle = "Official Account",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VedaCard(backgroundColor = VedaNavySurface) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(VedaBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = warden?.name?.take(2) ?: "DR",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = VedaTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = warden?.name ?: "Dr. S. Mishra",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = VedaTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = warden?.role ?: "Chief Warden",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = VedaBlueLight
                        )
                    )
                }
            }

            VedaCard {
                DetailItem("Assigned Hostel", hostel?.name ?: "No Hostel Created", Icons.Filled.Domain)
                DetailItem("Email Address", warden?.email ?: "warden@veda.edu.in", Icons.Filled.Email)
                DetailItem("Phone Number", warden?.phone ?: "+91 98765 43210", Icons.Filled.Phone)
                DetailItem("Hostel Location", hostel?.location ?: "Not Specified", Icons.Filled.Place)
            }
        }
    }
}
