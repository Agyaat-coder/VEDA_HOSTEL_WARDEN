package com.devesh.vedahostelwarden.ui.screens.hostel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.HostelViewModel

@Composable
fun EditHostelScreen(
    hostelViewModel: HostelViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentHostel by hostelViewModel.hostel.collectAsState()

    var name by remember(currentHostel) { mutableStateOf(currentHostel?.name ?: "") }
    var location by remember(currentHostel) { mutableStateOf(currentHostel?.location ?: "") }
    var address by remember(currentHostel) { mutableStateOf(currentHostel?.address ?: "") }
    var city by remember(currentHostel) { mutableStateOf(currentHostel?.city ?: "") }
    var state by remember(currentHostel) { mutableStateOf(currentHostel?.state ?: "") }
    var pinCode by remember(currentHostel) { mutableStateOf(currentHostel?.pinCode ?: "") }
    var description by remember(currentHostel) { mutableStateOf(currentHostel?.description ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Edit Hostel Details",
                subtitle = currentHostel?.name ?: "Hostel Details",
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
            VedaCard {
                VedaTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = null },
                    label = "Hostel Name",
                    placeholder = "e.g. Charak Chatras",
                    leadingIcon = Icons.Filled.Domain
                )

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = "Campus Location",
                    placeholder = "e.g. North Campus, Block A",
                    leadingIcon = Icons.Filled.Place
                )

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = "Street Address",
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
                    placeholder = "e.g. 110001"
                )

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description",
                    placeholder = "Hostel guidelines, description...",
                    singleLine = false,
                    modifier = Modifier.heightIn(min = 80.dp)
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(errorMessage!!, color = VedaDangerRed, style = MaterialTheme.typography.bodySmall)
                }

                if (successMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(successMessage!!, color = VedaSuccessGreen, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(24.dp))

                VedaButton(
                    text = "Save Changes",
                    icon = Icons.Filled.Save,
                    onClick = {
                        if (name.isBlank()) {
                            errorMessage = "Hostel name cannot be empty."
                        } else {
                            hostelViewModel.updateHostel(
                                context = context,
                                name = name,
                                location = location,
                                address = address,
                                city = city,
                                state = state,
                                pinCode = pinCode,
                                description = description,
                                onSuccess = {
                                    successMessage = "Hostel details updated successfully!"
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
