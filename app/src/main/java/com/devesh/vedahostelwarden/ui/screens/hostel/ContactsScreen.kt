package com.devesh.vedahostelwarden.ui.screens.hostel

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.HostelContact
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.HostelViewModel

@Composable
fun ContactsScreen(
    hostelViewModel: HostelViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val contacts by hostelViewModel.contacts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Important Contacts",
                subtitle = "Hostel Helpdesk & Emergency Numbers",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = VedaBluePrimary,
                contentColor = VedaTextPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Contact")
            }
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(contacts, key = { it.contactId }) { contact ->
                VedaCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (contact.isEmergency) VedaDangerBg else VedaBluePrimary.copy(alpha = 0.2f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Phone,
                                    contentDescription = null,
                                    tint = if (contact.isEmergency) VedaDangerRed else VedaBlueLight,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = contact.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = VedaTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "${contact.role} • ${contact.phone}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = VedaTextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                                context.startActivity(intent)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Call,
                                    contentDescription = "Call",
                                    tint = VedaSuccessGreen
                                )
                            }

                            IconButton(onClick = { hostelViewModel.deleteContact(contact.contactId) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = VedaDangerRed.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddContactDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, role, phone, isEmergency ->
                hostelViewModel.addContact(name, role, phone, isEmergency, onSuccess = { showAddDialog = false })
            }
        )
    }
}

@Composable
fun AddContactDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isEmergency by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Hostel Contact", color = VedaTextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                VedaTextField(value = name, onValueChange = { name = it }, label = "Contact Name", placeholder = "e.g. Warden Office")
                VedaTextField(value = role, onValueChange = { role = it }, label = "Role / Designation", placeholder = "e.g. Chief Warden Desk")
                VedaTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number", placeholder = "e.g. +91 98765 43210")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isEmergency,
                        onCheckedChange = { isEmergency = it },
                        colors = CheckboxDefaults.colors(checkedColor = VedaDangerRed)
                    )
                    Text("Emergency Contact", color = VedaTextSecondary, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onAdd(name, role.ifBlank { "Hostel Staff" }, phone, isEmergency)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = VedaBluePrimary)
            ) {
                Text("Add Contact")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = VedaTextSecondary)
            }
        },
        containerColor = VedaCardBackground
    )
}
