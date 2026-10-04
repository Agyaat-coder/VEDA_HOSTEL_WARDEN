package com.devesh.vedahostelwarden.ui.screens.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.SessionStatus
import com.devesh.vedahostelwarden.data.model.SessionType
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.AttendanceViewModel

@Composable
fun AttendanceMainScreen(
    attendanceViewModel: AttendanceViewModel,
    onCreateSessionClick: () -> Unit,
    onLiveAttendanceClick: (String) -> Unit,
    onHistoryClick: () -> Unit
) {
    val activeSession by attendanceViewModel.activeSession.collectAsState()
    val sessions by attendanceViewModel.sessions.collectAsState()

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Attendance Management",
                subtitle = "Morning & Evening Sessions"
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
                // Active Session Banner
                if (activeSession != null) {
                    val session = activeSession!!
                    VedaCard(
                        backgroundColor = VedaNavySurface,
                        borderColor = VedaSuccessGreen
                    ) {
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
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${session.date} • ${session.startTime} - ${session.endTime}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary)
                                )
                            }
                            VedaStatusChip("LIVE NOW", VedaSuccessGreen, VedaSuccessBg)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        VedaButton(
                            text = "Monitor Live Attendance",
                            icon = Icons.Filled.Visibility,
                            onClick = { onLiveAttendanceClick(session.sessionId) }
                        )
                    }
                } else {
                    VedaCard {
                        Text(
                            text = "No Session Active Currently",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = VedaTextPrimary,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Create a Morning (e.g. 07:00-09:00 AM) or Evening session to open student attendance.",
                            style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        VedaButton(
                            text = "Create Attendance Session",
                            icon = Icons.Filled.Add,
                            onClick = onCreateSessionClick
                        )
                    }
                }
            }

            // Quick Actions & History Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VedaButton(
                        modifier = Modifier.weight(1f),
                        text = "Create Session",
                        icon = Icons.Filled.Add,
                        onClick = onCreateSessionClick,
                        enabled = activeSession == null
                    )
                    OutlinedButton(
                        onClick = onHistoryClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VedaBlueLight)
                    ) {
                        Icon(Icons.Filled.History, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("History Log")
                    }
                }
            }

            // Recent Sessions List
            item {
                Text(
                    text = "SESSION HISTORY",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    sessions.forEach { sess ->
                        VedaCard(
                            onClick = {
                                if (sess.status == SessionStatus.ACTIVE) {
                                    onLiveAttendanceClick(sess.sessionId)
                                } else {
                                    onHistoryClick()
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (sess.sessionType == SessionType.MORNING) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                                        contentDescription = null,
                                        tint = VedaWarningYellow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "${sess.sessionType.name} ATTENDANCE",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                color = VedaTextPrimary,
                                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "${sess.date} • ${sess.startTime} - ${sess.endTime}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary)
                                        )
                                    }
                                }

                                if (sess.status == SessionStatus.ACTIVE) {
                                    VedaStatusChip("ACTIVE", VedaSuccessGreen, VedaSuccessBg)
                                } else {
                                    VedaStatusChip("CLOSED", VedaTextSecondary, VedaNavySurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
