package com.devesh.vedahostelwarden.ui.screens.hostel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.Room
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.HostelViewModel

@Composable
fun ManageRoomsScreen(
    hostelViewModel: HostelViewModel,
    onNavigateBack: () -> Unit
) {
    val rooms by hostelViewModel.rooms.collectAsState()
    var selectedFloor by remember { mutableStateOf("Floor 1") }

    val floors = remember(rooms) { rooms.map { it.floor }.distinct().sorted() }
    val filteredRooms = remember(rooms, selectedFloor) { rooms.filter { it.floor == selectedFloor } }

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Manage Rooms",
                subtitle = "Charak Chatras • ${rooms.size} Total Rooms",
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
            // Floor Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                floors.forEach { floor ->
                    val isSelected = selectedFloor == floor
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) VedaBluePrimary else VedaCardBackground)
                            .clickable { selectedFloor = floor }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = floor,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) VedaTextPrimary else VedaTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredRooms, key = { it.roomId }) { room ->
                    RoomCard(room = room)
                }
            }
        }
    }
}

@Composable
fun RoomCard(room: Room) {
    VedaCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.MeetingRoom,
                    contentDescription = null,
                    tint = if (room.isFull) VedaDangerRed else VedaBlueLight,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Room ${room.roomNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = VedaTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "${room.floor} • Capacity: ${room.capacity}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = VedaTextSecondary,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            if (room.isFull) {
                VedaStatusChip("FULL (${room.occupied}/${room.capacity})", VedaDangerRed, VedaDangerBg)
            } else {
                VedaStatusChip("${room.occupied}/${room.capacity} Occupied", VedaSuccessGreen, VedaSuccessBg)
            }
        }
    }
}
