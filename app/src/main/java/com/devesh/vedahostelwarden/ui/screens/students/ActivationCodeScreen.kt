package com.devesh.vedahostelwarden.ui.screens.students

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.StudentViewModel

@Composable
fun ActivationCodeScreen(
    studentId: String,
    studentViewModel: StudentViewModel,
    onDoneClick: () -> Unit
) {
    val context = LocalContext.current
    val student = remember(studentId, studentViewModel) { studentViewModel.getStudentById(studentId) }
    val code = student?.activationCode ?: "VD123456"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VedaNavyBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(VedaWarningYellow.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.VpnKey,
                    contentDescription = null,
                    tint = VedaWarningYellow,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Student Created!",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = VedaTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Give this activation code to ${student?.fullName ?: "the student"}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = VedaTextSecondary,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            VedaCard(
                backgroundColor = VedaNavySurface,
                borderColor = VedaWarningYellow
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ACTIVATION CODE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = VedaWarningYellow,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = code,
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = VedaTextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 4.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Activation Code", code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VedaBlueLight)
                    ) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy Code")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            VedaButton(
                text = "Done",
                onClick = onDoneClick
            )
        }
    }
}
