package com.devesh.vedahostelwarden.ui.screens.students

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.ActivationStatus
import com.devesh.vedahostelwarden.data.model.Student
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.StudentViewModel

enum class StudentFilterTab {
    ALL,
    ACTIVE,
    PENDING,
    INACTIVE
}

@Composable
fun StudentListScreen(
    studentViewModel: StudentViewModel,
    onNavigateToAddStudent: () -> Unit,
    onNavigateToStudentDetails: (String) -> Unit
) {
    val students by studentViewModel.students.collectAsState()
    var selectedTab by remember { mutableStateOf(StudentFilterTab.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredStudents = remember(students, selectedTab, searchQuery) {
        students.filter { student ->
            val matchesTab = when (selectedTab) {
                StudentFilterTab.ALL -> true
                StudentFilterTab.ACTIVE -> student.activationStatus == ActivationStatus.ACTIVE
                StudentFilterTab.PENDING -> student.activationStatus == ActivationStatus.PENDING_ACTIVATION
                StudentFilterTab.INACTIVE -> student.activationStatus == ActivationStatus.INACTIVE
            }
            val matchesSearch = searchQuery.isBlank() ||
                    student.fullName.contains(searchQuery, ignoreCase = true) ||
                    student.rollNumber.contains(searchQuery, ignoreCase = true) ||
                    student.roomNumber.contains(searchQuery, ignoreCase = true)
            matchesTab && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Students",
                subtitle = "${students.size} Total Registered"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddStudent,
                containerColor = VedaBluePrimary,
                contentColor = VedaTextPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Student")
            }
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StudentFilterTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    val tabCount = when (tab) {
                        StudentFilterTab.ALL -> students.size
                        StudentFilterTab.ACTIVE -> students.count { it.activationStatus == ActivationStatus.ACTIVE }
                        StudentFilterTab.PENDING -> students.count { it.activationStatus == ActivationStatus.PENDING_ACTIVATION }
                        StudentFilterTab.INACTIVE -> students.count { it.activationStatus == ActivationStatus.INACTIVE }
                    }
                    val labelText = "${tab.name} ($tabCount)"

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) VedaBluePrimary else VedaCardBackground)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = labelText,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) VedaTextPrimary else VedaTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // Search Bar
            VedaTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "",
                placeholder = "Search student by name, roll no, or room...",
                leadingIcon = Icons.Filled.Search
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredStudents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = VedaTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No students found",
                            style = MaterialTheme.typography.bodyLarge.copy(color = VedaTextSecondary)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredStudents, key = { it.studentId }) { student ->
                        StudentCard(
                            student = student,
                            onClick = { onNavigateToStudentDetails(student.studentId) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // FAB spacing
                    }
                }
            }
        }
    }
}

@Composable
fun StudentCard(
    student: Student,
    onClick: () -> Unit
) {
    VedaCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(VedaBluePrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.fullName.take(1),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = VedaBlueLight,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = VedaTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${student.rollNumber} • Room ${student.roomNumber}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = VedaTextSecondary,
                        fontSize = 12.sp
                    )
                )
            }

            StudentStatusChip(status = student.activationStatus)
        }
    }
}
