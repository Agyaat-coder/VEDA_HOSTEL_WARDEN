package com.devesh.vedahostelwarden.ui.screens.more

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*

@Composable
fun HelpFeedbackScreen(
    onNavigateBack: () -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var sentMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Help & Feedback",
                subtitle = "Contact VEDA Tech Support",
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
                VedaTextField(value = subject, onValueChange = { subject = it }, label = "Issue Subject", placeholder = "e.g. Room allocation query")
                Spacer(modifier = Modifier.height(12.dp))
                VedaTextField(value = description, onValueChange = { description = it }, label = "Detailed Feedback", placeholder = "Describe your query or suggestion...", singleLine = false, modifier = Modifier.heightIn(min = 100.dp))

                if (sentMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(sentMessage!!, color = VedaSuccessGreen, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(20.dp))

                VedaButton(
                    text = "Submit Feedback",
                    icon = Icons.Filled.Send,
                    onClick = {
                        if (subject.isNotBlank() && description.isNotBlank()) {
                            sentMessage = "Feedback submitted successfully! Our tech support team will get back to you."
                        }
                    }
                )
            }
        }
    }
}
