package com.devesh.vedahostelwarden.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.devesh.vedahostelwarden.data.model.SessionType
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel,
    onNavigateToCreateHostel: () -> Unit,
    onNavigateToStudents: () -> Unit,
    onNavigateToAddStudent: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    onNavigateToLiveAttendance: (String) -> Unit,
    onNavigateToCreateSession: () -> Unit,
    onNavigateToRooms: () -> Unit,
    onNavigateToMessMenu: () -> Unit,
    onNavigateToNotices: () -> Unit
) {
    val warden by dashboardViewModel.warden.collectAsState()
    val hostel by dashboardViewModel.hostel.collectAsState()
    val studentsCount by dashboardViewModel.studentsCount.collectAsState()
    val pendingCount by dashboardViewModel.pendingCount.collectAsState()
    val activeSessionsCount by dashboardViewModel.activeSessionsCount.collectAsState()
    val noticesCount by dashboardViewModel.noticesCount.collectAsState()
    val roomsCount by dashboardViewModel.roomsCount.collectAsState()
    val activeSession by dashboardViewModel.activeSession.collectAsState()

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Dashboard",
                subtitle = hostel?.name ?: "No Hostel Created"
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
                // Warden Greeting Header
                VedaCard(
                    backgroundColor = VedaNavySurface,
                    borderColor = VedaBluePrimary.copy(alpha = 0.3f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(VedaBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = warden?.name?.take(2) ?: "W",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = VedaTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Good morning,",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = VedaTextSecondary,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = warden?.name ?: "Dr. S. Mishra",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = VedaTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(VedaBluePrimary.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${warden?.role ?: "Chief Warden"} • ${hostel?.name ?: "No Hostel"}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = VedaBlueLight,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // No Hostel Setup Banner
            if (hostel == null) {
                item {
                    VedaCard(
                        backgroundColor = VedaNavySurface,
                        borderColor = VedaWarningYellow
                    ) {
                        Text(
                            text = "Set Up Your Hostel",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = VedaWarningYellow,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Create your hostel to start managing students, rooms, notices, and attendance.",
                            style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        VedaButton(
                            text = "Create Hostel Now",
                            icon = Icons.Filled.Add,
                            containerColor = VedaBluePrimary,
                            onClick = onNavigateToCreateHostel
                        )
                    }
                }
            }

            // Summary Grid Cards
            item {
                Text(
                    text = "OVERVIEW",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VedaStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Students",
                            value = "$studentsCount",
                            icon = Icons.Filled.People,
                            badgeColor = VedaBluePrimary,
                            onClick = onNavigateToStudents
                        )
                        VedaStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Active Sessions",
                            value = "$activeSessionsCount",
                            icon = Icons.Filled.Assignment,
                            badgeColor = VedaSuccessGreen,
                            onClick = onNavigateToAttendance
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VedaStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Pending",
                            value = "$pendingCount",
                            icon = Icons.Filled.HourglassEmpty,
                            badgeColor = VedaWarningYellow,
                            onClick = onNavigateToStudents
                        )
                        VedaStatCard(
                            modifier = Modifier.weight(1f),
                            title = "Notices",
                            value = "$noticesCount",
                            icon = Icons.Filled.Campaign,
                            badgeColor = Color(0xFFA855F7),
                            onClick = onNavigateToNotices
                        )
                    }

                    VedaStatCard(
                        title = "Hostel Rooms",
                        value = if (hostel != null) "$roomsCount Rooms (${hostel!!.occupancyPercentage}% Occupied)" else "0 Rooms",
                        icon = Icons.Filled.Bed,
                        badgeColor = Color(0xFF0EA5E9),
                        onClick = onNavigateToRooms
                    )
                }
            }

            // Today's Attendance Widget
            item {
                Text(
                    text = "TODAY'S ATTENDANCE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                VedaCard(
                    backgroundColor = VedaNavySurface,
                    borderColor = VedaBluePrimary.copy(alpha = 0.4f)
                ) {
                    if (activeSession != null) {
                        val session = activeSession!!
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (session.sessionType == SessionType.MORNING) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                                        contentDescription = null,
                                        tint = VedaWarningYellow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${session.sessionType.name.lowercase().replaceFirstChar { it.uppercase() }} Attendance",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = VedaTextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${session.date} • ${session.startTime} - ${session.endTime}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = VedaTextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                            VedaStatusChip("Active", VedaSuccessGreen, VedaSuccessBg)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        VedaButton(
                            text = "Monitor Live Attendance",
                            icon = Icons.Filled.Visibility,
                            onClick = { onNavigateToLiveAttendance(session.sessionId) }
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "No Active Session",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = VedaTextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Create Morning or Evening session for students",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = VedaTextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        VedaButton(
                            text = "Start Attendance Session",
                            icon = Icons.Filled.PlayArrow,
                            onClick = onNavigateToCreateSession
                        )
                    }
                }
            }

            // Quick Actions Section
            item {
                Text(
                    text = "QUICK ACTIONS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.PersonAdd,
                        label = "Add Student",
                        color = VedaBluePrimary,
                        onClick = onNavigateToAddStudent
                    )
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.PlayArrow,
                        label = "Start Session",
                        color = VedaSuccessGreen,
                        onClick = onNavigateToCreateSession
                    )
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Bed,
                        label = "Manage Rooms",
                        color = Color(0xFF0EA5E9),
                        onClick = onNavigateToRooms
                    )
                    QuickActionButton(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Restaurant,
                        label = "Mess Menu",
                        color = Color(0xFFF97316),
                        onClick = onNavigateToMessMenu
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(VedaCardBackground)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = VedaTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            ),
            maxLines = 1
        )
    }
}
