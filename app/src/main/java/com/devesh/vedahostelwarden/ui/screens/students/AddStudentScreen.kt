package com.devesh.vedahostelwarden.ui.screens.students

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreen(
    studentViewModel: StudentViewModel,
    onNavigateBack: () -> Unit,
    onStudentCreated: (String) -> Unit
) {
    val rooms by studentViewModel.rooms.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("B.Tech") }
    var branch by remember { mutableStateOf("Computer Science") }
    var year by remember { mutableStateOf("1st Year") }

    var selectedRoomId by remember { mutableStateOf(rooms.firstOrNull { !it.isFull }?.roomId ?: "") }
    var roomExpanded by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val selectedRoom = rooms.find { it.roomId == selectedRoomId } ?: rooms.firstOrNull()

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Add New Student",
                subtitle = "Generate activation code for student",
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
                    value = fullName,
                    onValueChange = { fullName = it; errorMessage = null },
                    label = "Full Name",
                    placeholder = "e.g. Aarav Sharma",
                    leadingIcon = Icons.Filled.Person
                )

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = rollNumber,
                    onValueChange = { rollNumber = it; errorMessage = null },
                    label = "Student Roll Number",
                    placeholder = "e.g. 24A1011",
                    leadingIcon = Icons.Filled.Badge
                )

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = email,
                    onValueChange = { email = it; errorMessage = null },
                    label = "Email Address",
                    placeholder = "e.g. student@veda.edu.in",
                    leadingIcon = Icons.Filled.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = "Phone Number",
                    placeholder = "e.g. +91 98765 12345",
                    leadingIcon = Icons.Filled.Phone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VedaTextField(
                        modifier = Modifier.weight(1f),
                        value = course,
                        onValueChange = { course = it },
                        label = "Course",
                        placeholder = "B.Tech"
                    )
                    VedaTextField(
                        modifier = Modifier.weight(1f),
                        value = year,
                        onValueChange = { year = it },
                        label = "Year",
                        placeholder = "1st Year"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                VedaTextField(
                    value = branch,
                    onValueChange = { branch = it },
                    label = "Branch / Department",
                    placeholder = "e.g. Computer Science"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Room Selector Dropdown
                ExposedDropdownMenuBox(
                    expanded = roomExpanded,
                    onExpandedChange = { roomExpanded = !roomExpanded }
                ) {
                    VedaTextField(
                        modifier = Modifier.menuAnchor(),
                        value = if (selectedRoom != null) "Room ${selectedRoom.roomNumber} (${selectedRoom.floor} • ${selectedRoom.available} slots left)" else "Select Room",
                        onValueChange = {},
                        readOnly = true,
                        label = "Assigned Room",
                        leadingIcon = Icons.Filled.MeetingRoom,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roomExpanded) }
                    )

                    ExposedDropdownMenu(
                        expanded = roomExpanded,
                        onDismissRequest = { roomExpanded = false }
                    ) {
                        rooms.forEach { room ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Room ${room.roomNumber} (${room.floor}) - ${if (room.isFull) "FULL" else "${room.available} available"}",
                                        color = if (room.isFull) VedaDangerRed else VedaTextPrimary
                                    )
                                },
                                onClick = {
                                    selectedRoomId = room.roomId
                                    roomExpanded = false
                                },
                                enabled = !room.isFull
                            )
                        }
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
                    text = "Create Student & Code",
                    icon = Icons.Filled.Check,
                    onClick = {
                        if (fullName.isBlank() || rollNumber.isBlank() || email.isBlank() || selectedRoom == null) {
                            errorMessage = "Please fill in all required fields"
                        } else {
                            studentViewModel.addStudent(
                                fullName = fullName,
                                rollNumber = rollNumber,
                                email = email,
                                phone = phone.ifBlank { "+91 98000 00000" },
                                course = course,
                                branch = branch,
                                year = year,
                                hostelId = "HOSTEL_CHARAK",
                                hostelName = "Charak Chatras",
                                roomId = selectedRoom.roomId,
                                roomNumber = selectedRoom.roomNumber,
                                floor = selectedRoom.floor,
                                onSuccess = { createdStudent ->
                                    onStudentCreated(createdStudent.studentId)
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
