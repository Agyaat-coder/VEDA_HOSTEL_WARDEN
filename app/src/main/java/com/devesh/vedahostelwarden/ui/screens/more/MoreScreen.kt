package com.devesh.vedahostelwarden.ui.screens.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.AuthViewModel
import com.devesh.vedahostelwarden.viewmodel.ProfileViewModel

@Composable
fun MoreScreen(
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    onNavigateToHostel: () -> Unit,
    onNavigateToRooms: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToMessMenu: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val context = LocalContext.current
    val warden by profileViewModel.warden.collectAsState()
    val hostel by profileViewModel.hostel.collectAsState()
    var showSignOutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "More & Settings",
                subtitle = "Hostel Admin Controls"
            )
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Warden Profile Brief Card
                VedaCard(
                    onClick = onNavigateToProfile,
                    backgroundColor = VedaNavySurface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(VedaBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = warden?.name?.take(2) ?: "DR",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = VedaTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = warden?.name ?: "Dr. S. Mishra",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = VedaTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${warden?.role ?: "Warden"} • ${hostel?.name ?: "No Hostel"}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = VedaTextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VedaTextMuted)
                    }
                }
            }

            // Hostel Management Items
            item {
                Text(
                    text = "HOSTEL MANAGEMENT",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                VedaCard {
                    MoreMenuItem("Hostel Overview", Icons.Filled.Domain, onNavigateToHostel)
                    HorizontalDivider(color = VedaCardBorder)
                    MoreMenuItem("Room Management", Icons.Filled.MeetingRoom, onNavigateToRooms)
                    HorizontalDivider(color = VedaCardBorder)
                    MoreMenuItem("Hostel Contacts", Icons.Filled.Contacts, onNavigateToContacts)
                    HorizontalDivider(color = VedaCardBorder)
                    MoreMenuItem("Mess Menu", Icons.Filled.Restaurant, onNavigateToMessMenu)
                    HorizontalDivider(color = VedaCardBorder)
                    MoreMenuItem("Reports & Analytics", Icons.Filled.BarChart, onNavigateToReports)
                }
            }

            // Account & System
            item {
                Text(
                    text = "ACCOUNT & APP",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                VedaCard {
                    MoreMenuItem("Warden Profile", Icons.Filled.Person, onNavigateToProfile)
                    HorizontalDivider(color = VedaCardBorder)
                    MoreMenuItem("Notification Settings", Icons.Filled.Notifications, onNavigateToSettings)
                    HorizontalDivider(color = VedaCardBorder)
                    MoreMenuItem("Help & Feedback", Icons.Filled.Help, onNavigateToHelp)
                    HorizontalDivider(color = VedaCardBorder)
                    MoreMenuItem("About VEDA", Icons.Filled.Info, onNavigateToAbout)
                }
            }

            item {
                VedaButton(
                    text = "Sign Out",
                    icon = Icons.Filled.Logout,
                    containerColor = VedaDangerRed,
                    onClick = {
                        showSignOutDialog = true
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text("Sign Out?", color = VedaTextPrimary) },
            text = { Text("Are you sure you want to sign out?", color = VedaTextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutDialog = false
                        authViewModel.logout(context, onLoggedOut = onLoggedOut)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VedaDangerRed)
                ) {
                    Text("Sign Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel", color = VedaTextSecondary)
                }
            },
            containerColor = VedaCardBackground
        )
    }
}

@Composable
fun MoreMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = VedaBlueLight, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = VedaTextPrimary,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VedaTextMuted)
    }
}
