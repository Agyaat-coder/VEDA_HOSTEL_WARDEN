package com.devesh.vedahostelwarden.ui.screens.hostel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.HostelViewModel

@Composable
fun HostelManagementScreen(
    hostelViewModel: HostelViewModel,
    onNavigateToCreateHostel: () -> Unit,
    onNavigateToEditHostel: () -> Unit,
    onNavigateToRooms: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToMessMenu: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val hostel by hostelViewModel.hostel.collectAsState()

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Hostel Management",
                subtitle = hostel?.name ?: "No Hostel Assigned"
            )
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                if (hostel == null) {
                    VedaCard(
                        backgroundColor = VedaNavySurface,
                        borderColor = VedaWarningYellow
                    ) {
                        Text(
                            text = "No Hostel Created",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = VedaWarningYellow,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Create your hostel to begin managing students, rooms, notices, and attendance.",
                            style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        VedaButton(
                            text = "Create Hostel",
                            icon = Icons.Filled.Add,
                            onClick = onNavigateToCreateHostel
                        )
                    }
                } else {
                    val h = hostel!!
                    VedaCard(
                        backgroundColor = VedaNavySurface,
                        borderColor = VedaBluePrimary.copy(alpha = 0.4f)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = h.name,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            color = VedaTextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "${h.location} • ${h.address}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = VedaTextSecondary,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                                VedaStatusChip("${h.occupancyPercentage}% Occupied", VedaSuccessGreen, VedaSuccessBg)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                HostelStatItem("Total Students", "${h.totalStudents}", Icons.Filled.People)
                                HostelStatItem("Total Rooms", "${h.totalRooms}", Icons.Filled.Bed)
                                HostelStatItem("Total Floors", "${h.totalFloors}", Icons.Filled.Layers)
                            }
                        }
                    }
                }
            }

            // Management Action Tiles
            item {
                Text(
                    text = "MANAGEMENT MODULES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (hostel != null) {
                        ManagementTile(
                            title = "Edit Hostel Information",
                            subtitle = "Update name, address, location, and description",
                            icon = Icons.Filled.Edit,
                            iconColor = VedaBluePrimary,
                            onClick = onNavigateToEditHostel
                        )
                    }

                    ManagementTile(
                        title = "Manage Rooms & Occupancy",
                        subtitle = "View floor plans, room capacities, and open slots",
                        icon = Icons.Filled.MeetingRoom,
                        iconColor = Color(0xFF0EA5E9),
                        onClick = onNavigateToRooms
                    )

                    ManagementTile(
                        title = "Important Hostel Contacts",
                        subtitle = "Manage Warden Office, Security Desk, Mess numbers",
                        icon = Icons.Filled.Contacts,
                        iconColor = VedaSuccessGreen,
                        onClick = onNavigateToContacts
                    )

                    ManagementTile(
                        title = "Weekly Mess Menu",
                        subtitle = "Update Breakfast, Lunch, Snacks & Dinner schedule",
                        icon = Icons.Filled.Restaurant,
                        iconColor = Color(0xFFF97316),
                        onClick = onNavigateToMessMenu
                    )

                    ManagementTile(
                        title = "Reports & Analytics",
                        subtitle = "Attendance logs, occupancy trends, and reports",
                        icon = Icons.Filled.BarChart,
                        iconColor = Color(0xFFA855F7),
                        onClick = onNavigateToReports
                    )
                }
            }
        }
    }
}

@Composable
fun HostelStatItem(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = VedaBlueLight, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(color = VedaTextPrimary, fontWeight = FontWeight.Bold)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary, fontSize = 11.sp)
        )
    }
}

@Composable
fun ManagementTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    VedaCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = VedaTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
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
