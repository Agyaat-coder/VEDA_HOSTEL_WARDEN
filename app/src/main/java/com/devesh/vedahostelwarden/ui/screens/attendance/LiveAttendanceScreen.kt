package com.devesh.vedahostelwarden.ui.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.AttendanceRecord
import com.devesh.vedahostelwarden.data.model.AttendanceStatus
import com.devesh.vedahostelwarden.data.model.SessionType
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.AttendanceViewModel

enum class AttendanceFilterTab {
    ALL,
    PRESENT,
    ABSENT,
    NOT_MARKED
}

@Composable
fun LiveAttendanceScreen(
    sessionId: String,
    attendanceViewModel: AttendanceViewModel,
    onNavigateBack: () -> Unit
) {
    val activeSession by attendanceViewModel.activeSession.collectAsState()
    val records by attendanceViewModel.currentRecords.collectAsState()

    var selectedTab by remember { mutableStateOf(AttendanceFilterTab.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var showCloseConfirmDialog by remember { mutableStateOf(false) }

    val presentCount = remember(records) { records.count { it.status == AttendanceStatus.PRESENT } }
    val absentCount = remember(records) { records.count { it.status == AttendanceStatus.ABSENT } }
    val notMarkedCount = remember(records) { records.count { it.status == AttendanceStatus.NOT_MARKED } }
    val totalCount = remember(records) { records.size }
    val presentPercentage = remember(presentCount, totalCount) {
        if (totalCount > 0) ((presentCount.toDouble() / totalCount) * 100).toInt() else 0
    }

    val filteredRecords = remember(records, selectedTab, searchQuery) {
        records.filter { rec ->
            val matchesTab = when (selectedTab) {
                AttendanceFilterTab.ALL -> true
                AttendanceFilterTab.PRESENT -> rec.status == AttendanceStatus.PRESENT
                AttendanceFilterTab.ABSENT -> rec.status == AttendanceStatus.ABSENT
                AttendanceFilterTab.NOT_MARKED -> rec.status == AttendanceStatus.NOT_MARKED
            }
            val matchesSearch = searchQuery.isBlank() ||
                    rec.studentName.contains(searchQuery, ignoreCase = true) ||
                    rec.rollNumber.contains(searchQuery, ignoreCase = true) ||
                    rec.roomNumber.contains(searchQuery, ignoreCase = true)
            matchesTab && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = if (activeSession != null) "${activeSession!!.sessionType.name} ATTENDANCE" else "LIVE ATTENDANCE",
                subtitle = if (activeSession != null) "${activeSession!!.date} • ${activeSession!!.startTime} - ${activeSession!!.endTime}" else "",
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
                .padding(horizontal = 16.dp)
        ) {
            // Stats & Progress Banner
            VedaCard(backgroundColor = VedaNavySurface) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$presentPercentage% Present",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = VedaSuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Present: $presentCount  •  Absent: $absentCount  •  Not Marked: $notMarkedCount",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = VedaTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }

                    if (activeSession != null) {
                        Button(
                            onClick = { showCloseConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = VedaDangerRed),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Close Session", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { if (totalCount > 0) presentCount.toFloat() / totalCount else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = VedaSuccessGreen,
                    trackColor = VedaNavyBackground
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AttendanceFilterTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    val count = when (tab) {
                        AttendanceFilterTab.ALL -> totalCount
                        AttendanceFilterTab.PRESENT -> presentCount
                        AttendanceFilterTab.ABSENT -> absentCount
                        AttendanceFilterTab.NOT_MARKED -> notMarkedCount
                    }
                    val label = "${tab.name.lowercase().replaceFirstChar { it.uppercase() }} ($count)"

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) VedaBluePrimary else VedaCardBackground)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) VedaTextPrimary else VedaTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            VedaTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "",
                placeholder = "Search student by name, roll, or room...",
                leadingIcon = Icons.Filled.Search
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Student Record List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredRecords, key = { it.recordId }) { record ->
                    AttendanceRecordRow(
                        record = record,
                        onToggle = {
                            attendanceViewModel.toggleRecordStatus(record.recordId, record.status)
                        }
                    )
                }
            }
        }
    }

    if (showCloseConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showCloseConfirmDialog = false },
            title = { Text("Close Attendance Session?", color = VedaTextPrimary) },
            text = { Text("Students will no longer be able to mark attendance for this session.", color = VedaTextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        showCloseConfirmDialog = false
                        attendanceViewModel.closeActiveSession(
                            onSuccess = onNavigateBack,
                            onError = {}
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VedaDangerRed)
                ) {
                    Text("Close Session")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCloseConfirmDialog = false }) {
                    Text("Cancel", color = VedaTextSecondary)
                }
            },
            containerColor = VedaCardBackground
        )
    }
}

@Composable
fun AttendanceRecordRow(
    record: AttendanceRecord,
    onToggle: () -> Unit
) {
    VedaCard(
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (record.status) {
                                AttendanceStatus.PRESENT -> VedaSuccessBg
                                AttendanceStatus.ABSENT -> VedaDangerBg
                                AttendanceStatus.NOT_MARKED -> VedaWarningBg
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = record.studentName.take(1),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = when (record.status) {
                                AttendanceStatus.PRESENT -> VedaSuccessGreen
                                AttendanceStatus.ABSENT -> VedaDangerRed
                                AttendanceStatus.NOT_MARKED -> VedaWarningYellow
                            },
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = record.studentName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = VedaTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${record.rollNumber} • Room ${record.roomNumber}${if (record.markedAt != null) " • ${record.markedAt}" else ""}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = VedaTextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            AttendanceStatusChip(status = record.status)
        }
    }
}
