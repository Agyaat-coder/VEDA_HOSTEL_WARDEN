package com.devesh.vedahostelwarden.ui.screens.hostel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.HostelViewModel

@Composable
fun CreateHostelScreen(
    hostelViewModel: HostelViewModel,
    onNavigateBack: () -> Unit,
    onHostelCreated: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var floorsText by remember { mutableStateOf("4") }
    var roomsText by remember { mutableStateOf("60") }
    var description by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isCreatedSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Create Hostel",
                subtitle = "Set up your hostel structure",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (isCreatedSuccess) {
                VedaCard(
                    backgroundColor = VedaNavySurface,
                    borderColor = VedaSuccessGreen
                ) {
                    Text(
                        text = "Hostel Created Successfully!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = VedaSuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your hostel '$name' has been set up with $roomsText rooms across $floorsText floors.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = VedaTextSecondary)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    VedaButton(
                        text = "Continue to Dashboard",
                        icon = Icons.Filled.Check,
                        containerColor = VedaSuccessGreen,
                        onClick = onHostelCreated
                    )
                }
            } else {
                VedaCard {
                    Text(
                        text = "HOSTEL INFORMATION",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = VedaTextMuted,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    VedaTextField(
                        value = name,
                        onValueChange = { name = it; errorMessage = null },
                        label = "Hostel Name *",
                        placeholder = "e.g. Charak Chatras",
                        leadingIcon = Icons.Filled.Domain
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VedaTextField(
                        value = location,
                        onValueChange = { location = it; errorMessage = null },
                        label = "Campus Block / Location *",
                        placeholder = "e.g. North Campus, Block A",
                        leadingIcon = Icons.Filled.Place
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VedaTextField(
                        value = address,
                        onValueChange = { address = it; errorMessage = null },
                        label = "Street Address *",
                        placeholder = "e.g. Sector 14, University Road",
                        leadingIcon = Icons.Filled.Home
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VedaTextField(
                            modifier = Modifier.weight(1f),
                            value = city,
                            onValueChange = { city = it },
                            label = "City",
                            placeholder = "e.g. New Delhi"
                        )
                        VedaTextField(
                            modifier = Modifier.weight(1f),
                            value = state,
                            onValueChange = { state = it },
                            label = "State",
                            placeholder = "e.g. Delhi"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    VedaTextField(
                        value = pinCode,
                        onValueChange = { pinCode = it },
                        label = "PIN Code",
                        placeholder = "e.g. 110001",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "BUILDING STRUCTURE",
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
                        VedaTextField(
                            modifier = Modifier.weight(1f),
                            value = floorsText,
                            onValueChange = { floorsText = it; errorMessage = null },
                            label = "Number of Floors *",
                            placeholder = "4",
                            leadingIcon = Icons.Filled.Layers,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        VedaTextField(
                            modifier = Modifier.weight(1f),
                            value = roomsText,
                            onValueChange = { roomsText = it; errorMessage = null },
                            label = "Total Rooms *",
                            placeholder = "60",
                            leadingIcon = Icons.Filled.MeetingRoom,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    VedaTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Hostel Description (Optional)",
                        placeholder = "Brief description or guidelines for students...",
                        singleLine = false,
                        modifier = Modifier.heightIn(min = 80.dp)
                    )

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
                        text = "Create Hostel",
                        icon = Icons.Filled.Add,
                        onClick = {
                            val floorsInt = floorsText.toIntOrNull() ?: 0
                            val roomsInt = roomsText.toIntOrNull() ?: 0

                            if (name.isBlank()) {
                                errorMessage = "Hostel name cannot be empty."
                            } else if (address.isBlank() && location.isBlank()) {
                                errorMessage = "Address or location cannot be empty."
                            } else if (floorsInt <= 0) {
                                errorMessage = "Number of floors must be greater than 0."
                            } else if (roomsInt <= 0) {
                                errorMessage = "Number of rooms must be greater than 0."
                            } else {
                                hostelViewModel.createHostel(
                                    context = context,
                                    name = name,
                                    location = location,
                                    address = address,
                                    city = city,
                                    state = state,
                                    pinCode = pinCode,
                                    floors = floorsInt,
                                    roomsCount = roomsInt,
                                    description = description,
                                    onSuccess = {
                                        isCreatedSuccess = true
                                    },
                                    onError = { err -> errorMessage = err }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
