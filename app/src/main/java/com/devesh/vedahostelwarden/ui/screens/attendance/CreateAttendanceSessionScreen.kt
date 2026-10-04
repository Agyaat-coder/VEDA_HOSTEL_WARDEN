package com.devesh.vedahostelwarden.ui.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.SessionType
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateAttendanceSessionScreen(
    attendanceViewModel: AttendanceViewModel,
    onNavigateBack: () -> Unit,
    onSessionCreated: () -> Unit
) {
    var selectedType by remember { mutableStateOf(SessionType.MORNING) }
    var dateText by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())) }
    var startTime by remember { mutableStateOf("07:00 AM") }
    var endTime by remember { mutableStateOf("09:00 AM") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedType) {
        if (selectedType == SessionType.MORNING) {
            startTime = "07:00 AM"
            endTime = "09:00 AM"
        } else {
            startTime = "07:00 PM"
            endTime = "09:00 PM"
        }
    }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Create Attendance Session",
                subtitle = "Morning / Evening Session",
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
            VedaCard {
                Text(
                    text = "SESSION TYPE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SessionTypeChip(
                        modifier = Modifier.weight(1f),
                        type = SessionType.MORNING,
                        isSelected = selectedType == SessionType.MORNING,
                        onClick = { selectedType = SessionType.MORNING }
                    )
                    SessionTypeChip(
                        modifier = Modifier.weight(1f),
                        type = SessionType.EVENING,
                        isSelected = selectedType == SessionType.EVENING,
                        onClick = { selectedType = SessionType.EVENING }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                VedaTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = "Date",
                    placeholder = "e.g. 02 Oct 2026",
                    leadingIcon = Icons.Filled.DateRange
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VedaTextField(
                        modifier = Modifier.weight(1f),
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = "Start Time",
                        placeholder = "07:00 AM",
                        leadingIcon = Icons.Filled.AccessTime
                    )
                    VedaTextField(
                        modifier = Modifier.weight(1f),
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = "End Time",
                        placeholder = "09:00 AM",
                        leadingIcon = Icons.Filled.AccessTime
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Info Callout
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VedaBluePrimary.copy(alpha = 0.15f))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = VedaBlueLight,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Students will be able to mark their presence during this session window.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = VedaTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = VedaDangerRed,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                VedaButton(
                    text = "Create Session",
                    icon = Icons.Filled.Check,
                    onClick = {
                        attendanceViewModel.createSession(
                            sessionType = selectedType,
                            date = dateText,
                            startTime = startTime,
                            endTime = endTime,
                            onSuccess = onSessionCreated,
                            onError = { err -> errorMessage = err }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SessionTypeChip(
    modifier: Modifier = Modifier,
    type: SessionType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) VedaBluePrimary else VedaNavySurface)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (type == SessionType.MORNING) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                contentDescription = null,
                tint = if (isSelected) VedaTextPrimary else VedaWarningYellow,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium.copy(
                    color = if (isSelected) VedaTextPrimary else VedaTextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
        }
    }
}
