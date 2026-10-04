package com.devesh.vedahostelwarden.ui.screens.more

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*

@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Reports & Insights",
                subtitle = "Hostel Attendance & Occupancy Analytics",
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
            Text(
                text = "ATTENDANCE METRICS",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = VedaTextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VedaStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Monthly Avg Attendance",
                    value = "89.4%",
                    icon = Icons.Filled.TrendingUp,
                    badgeColor = VedaSuccessGreen
                )
                VedaStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Sessions Run",
                    value = "58",
                    icon = Icons.Filled.Assignment,
                    badgeColor = VedaBluePrimary
                )
            }

            VedaCard {
                Text(
                    text = "SESSION EXTREMES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VedaTextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                ReportRow("Highest Attendance Session", "Morning Session (02 Oct) • 90.1%", VedaSuccessGreen)
                ReportRow("Lowest Attendance Session", "Evening Session (28 Sep) • 82.5%", VedaWarningYellow)
            }

            Text(
                text = "OCCUPANCY METRICS",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = VedaTextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            VedaCard {
                ReportRow("Total Capacity", "150 Students (60 Rooms)", VedaBlueLight)
                ReportRow("Current Occupancy", "128 Students (92% Occupied)", Color(0xFF0EA5E9))
                ReportRow("Pending Activations", "5 Students Pending Code", VedaWarningYellow)
            }
        }
    }
}

@Composable
fun ReportRow(title: String, subtitle: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = VedaTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                color = color,
                fontSize = 12.sp
            )
        )
    }
}
