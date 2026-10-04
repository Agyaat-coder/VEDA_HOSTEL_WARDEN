package com.devesh.vedahostelwarden.ui.screens.more

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    var attendanceAlerts by remember { mutableStateOf(true) }
    var noticeAlerts by remember { mutableStateOf(true) }
    var emergencyAlerts by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Notification Settings",
                subtitle = "App Preferences",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VedaCard {
                SettingToggle("Attendance Session Alerts", "Notifications when session opens/closes", attendanceAlerts) { attendanceAlerts = it }
                HorizontalDivider(color = VedaCardBorder)
                SettingToggle("Notice Confirmation Alerts", "Notifications on publishing notices", noticeAlerts) { noticeAlerts = it }
                HorizontalDivider(color = VedaCardBorder)
                SettingToggle("Emergency Alerts", "Urgent notifications from security desk", emergencyAlerts) { emergencyAlerts = it }
            }
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(color = VedaTextPrimary))
            Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = VedaTextPrimary,
                checkedTrackColor = VedaBluePrimary
            )
        )
    }
}
