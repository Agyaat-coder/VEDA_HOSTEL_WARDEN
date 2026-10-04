package com.devesh.vedahostelwarden.ui.screens.students

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.ActivationStatus
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.StudentViewModel

@Composable
fun StudentDetailsScreen(
    studentId: String,
    studentViewModel: StudentViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val student = remember(studentId, studentViewModel) { studentViewModel.getStudentById(studentId) }
    var currentCode by remember { mutableStateOf(student?.activationCode ?: "") }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Student Profile",
                subtitle = student?.rollNumber ?: "",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        if (student == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Student not found", color = VedaTextSecondary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Card
                VedaCard(backgroundColor = VedaNavySurface) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(VedaBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = student.fullName.take(2).uppercase(),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = VedaTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = student.fullName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = VedaTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Roll No: ${student.rollNumber}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = VedaTextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            StudentStatusChip(status = student.activationStatus)
                        }
                    }
                }

                // Activation Code Box
                VedaCard(
                    backgroundColor = VedaNavySurface,
                    borderColor = VedaWarningYellow.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "ACTIVATION CODE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = VedaWarningYellow,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentCode,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = VedaTextPrimary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        )
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Activation Code", currentCode)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Activation Code Copied!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy Code", tint = VedaBlueLight)
                        }
                    }

                    if (student.activationStatus == ActivationStatus.PENDING_ACTIVATION) {
                        Spacer(modifier = Modifier.height(12.dp))
                        VedaButton(
                            text = "Simulate Student Account Activation",
                            icon = Icons.Filled.CheckCircle,
                            containerColor = VedaSuccessGreen,
                            onClick = {
                                studentViewModel.simulateActivation(
                                    studentId = student.studentId,
                                    onSuccess = {
                                        statusMessage = "Account activated successfully!"
                                    },
                                    onError = { err -> statusMessage = err }
                                )
                            }
                        )
                    }
                }

                if (statusMessage != null) {
                    Text(
                        text = statusMessage!!,
                        color = VedaSuccessGreen,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Details Grid Card
                VedaCard {
                    Text(
                        text = "ACADEMIC & HOSTEL DETAILS",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = VedaTextMuted,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    DetailItem("Hostel Name", student.hostelName, Icons.Filled.Home)
                    DetailItem("Room & Floor", "Room ${student.roomNumber} (${student.floor})", Icons.Filled.MeetingRoom)
                    DetailItem("Course & Branch", "${student.course} - ${student.branch}", Icons.Filled.School)
                    DetailItem("Academic Year", student.year, Icons.Filled.CalendarToday)
                    DetailItem("Email Address", student.email, Icons.Filled.Email)
                    DetailItem("Phone Number", student.phone, Icons.Filled.Phone)
                    DetailItem("Registration Date", student.createdAt, Icons.Filled.DateRange)
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(VedaNavySurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = VedaBlueLight, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(color = VedaTextSecondary, fontSize = 11.sp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(color = VedaTextPrimary, fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
