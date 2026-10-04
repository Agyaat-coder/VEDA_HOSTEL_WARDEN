package com.devesh.vedahostelwarden.ui.screens.hostel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.MessMenu
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.MessMenuViewModel

@Composable
fun MessMenuScreen(
    messMenuViewModel: MessMenuViewModel,
    onNavigateBack: () -> Unit
) {
    val menuList by messMenuViewModel.messMenu.collectAsState()
    var selectedDay by remember { mutableStateOf("Monday") }
    var showEditDialog by remember { mutableStateOf(false) }

    val days = remember { listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday") }
    val currentDayMenu = remember(menuList, selectedDay) {
        menuList.find { it.dayOfWeek.equals(selectedDay, ignoreCase = true) } ?: menuList.firstOrNull()
    }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Mess Menu Management",
                subtitle = "Weekly Food Schedule",
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
                .padding(horizontal = 16.dp)
        ) {
            // Day Selector Scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
                    val isSelected = selectedDay == day
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) VedaBluePrimary else VedaCardBackground)
                            .clickable { selectedDay = day }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.take(3).uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) VedaTextPrimary else VedaTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (currentDayMenu != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    VedaCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${currentDayMenu.dayOfWeek} Menu",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = VedaTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            IconButton(onClick = { showEditDialog = true }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Menu", tint = VedaBlueLight)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        MealSection("Breakfast", currentDayMenu.breakfast, Color(0xFFF97316))
                        MealSection("Lunch", currentDayMenu.lunch, VedaSuccessGreen)
                        MealSection("Snacks", currentDayMenu.snacks, VedaWarningYellow)
                        MealSection("Dinner", currentDayMenu.dinner, Color(0xFFA855F7))

                        if (currentDayMenu.specialNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Note: ${currentDayMenu.specialNote}",
                                style = MaterialTheme.typography.bodySmall.copy(color = VedaTextMuted)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog && currentDayMenu != null) {
        EditMenuDialog(
            menu = currentDayMenu,
            onDismiss = { showEditDialog = false },
            onSave = { b, l, s, d, note ->
                messMenuViewModel.updateMenuForDay(
                    dayOfWeek = currentDayMenu.dayOfWeek,
                    breakfast = b,
                    lunch = l,
                    snacks = s,
                    dinner = d,
                    specialNote = note,
                    onSuccess = { showEditDialog = false }
                )
            }
        )
    }
}

@Composable
fun MealSection(mealName: String, description: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = mealName.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = VedaTextPrimary,
                fontSize = 14.sp
            ),
            modifier = Modifier.padding(start = 18.dp)
        )
    }
}

@Composable
fun EditMenuDialog(
    menu: MessMenu,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
    var breakfast by remember { mutableStateOf(menu.breakfast) }
    var lunch by remember { mutableStateOf(menu.lunch) }
    var snacks by remember { mutableStateOf(menu.snacks) }
    var dinner by remember { mutableStateOf(menu.dinner) }
    var note by remember { mutableStateOf(menu.specialNote) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit ${menu.dayOfWeek} Menu", color = VedaTextPrimary) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VedaTextField(value = breakfast, onValueChange = { breakfast = it }, label = "Breakfast")
                VedaTextField(value = lunch, onValueChange = { lunch = it }, label = "Lunch")
                VedaTextField(value = snacks, onValueChange = { snacks = it }, label = "Snacks")
                VedaTextField(value = dinner, onValueChange = { dinner = it }, label = "Dinner")
                VedaTextField(value = note, onValueChange = { note = it }, label = "Special Note (Optional)")
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(breakfast, lunch, snacks, dinner, note) },
                colors = ButtonDefaults.buttonColors(containerColor = VedaBluePrimary)
            ) {
                Text("Save Changes")
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
