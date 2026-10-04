package com.devesh.vedahostelwarden.ui.screens.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.SessionType
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.AttendanceViewModel

@Composable
fun AttendanceHistoryScreen(
    attendanceViewModel: AttendanceViewModel,
    onNavigateBack: () -> Unit
) {
    val sessions by attendanceViewModel.sessions.collectAsState()

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Attendance History",
                subtitle = "Past Sessions & Logs",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(sessions, key = { it.sessionId }) { sess ->
                VedaCard {
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
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${sess.sessionType.name} SESSION",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = VedaTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${sess.date} • ${sess.startTime} - ${sess.endTime}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = VedaTextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        VedaStatusChip(
                            statusText = sess.status.name,
                            textColor = if (sess.status.name == "ACTIVE") VedaSuccessGreen else VedaTextSecondary,
                            backgroundColor = if (sess.status.name == "ACTIVE") VedaSuccessBg else VedaNavySurface
                        )
                    }
                }
            }
        }
    }
}
