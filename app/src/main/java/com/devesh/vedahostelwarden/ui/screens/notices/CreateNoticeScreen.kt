package com.devesh.vedahostelwarden.ui.screens.notices

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
import com.devesh.vedahostelwarden.data.model.TargetAudience
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.NoticeViewModel

@Composable
fun CreateNoticeScreen(
    noticeViewModel: NoticeViewModel,
    onNavigateBack: () -> Unit,
    onNoticePublished: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedAudience by remember { mutableStateOf(TargetAudience.ALL_STUDENTS) }
    var targetDetail by remember { mutableStateOf("All Students") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Create Notice",
                subtitle = "Send announcements to students",
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
                VedaTextField(
                    value = title,
                    onValueChange = { title = it; errorMessage = null },
                    label = "Notice Title",
                    placeholder = "e.g. Water Supply Maintenance",
                    leadingIcon = Icons.Filled.Title
                )

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = message,
                    onValueChange = { message = it; errorMessage = null },
                    label = "Message Body",
                    placeholder = "Enter full announcement text here...",
                    singleLine = false,
                    modifier = Modifier.heightIn(min = 120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "TARGET AUDIENCE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AudienceOption(
                        label = "All Students",
                        isSelected = selectedAudience == TargetAudience.ALL_STUDENTS,
                        onClick = {
                            selectedAudience = TargetAudience.ALL_STUDENTS
                            targetDetail = "All Students"
                        }
                    )
                    AudienceOption(
                        label = "Specific Hostel (Charak Chatras)",
                        isSelected = selectedAudience == TargetAudience.SPECIFIC_HOSTEL,
                        onClick = {
                            selectedAudience = TargetAudience.SPECIFIC_HOSTEL
                            targetDetail = "Charak Chatras"
                        }
                    )
                    AudienceOption(
                        label = "Specific Room (e.g. Room 101)",
                        isSelected = selectedAudience == TargetAudience.SPECIFIC_ROOM,
                        onClick = {
                            selectedAudience = TargetAudience.SPECIFIC_ROOM
                            targetDetail = "Room 101"
                        }
                    )
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
                    text = "Publish Notice",
                    icon = Icons.Filled.Send,
                    onClick = {
                        if (title.isBlank() || message.isBlank()) {
                            errorMessage = "Please enter both title and message"
                        } else {
                            noticeViewModel.createNotice(
                                title = title,
                                message = message,
                                targetAudience = selectedAudience,
                                targetDetail = targetDetail,
                                scheduleTime = "Send Now",
                                onSuccess = onNoticePublished,
                                onError = { err -> errorMessage = err }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AudienceOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) VedaBluePrimary.copy(alpha = 0.2f) else VedaNavySurface)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = VedaBluePrimary)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isSelected) VedaTextPrimary else VedaTextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        )
    }
}
