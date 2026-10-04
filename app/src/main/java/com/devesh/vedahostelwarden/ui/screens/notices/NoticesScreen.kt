package com.devesh.vedahostelwarden.ui.screens.notices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devesh.vedahostelwarden.data.model.Notice
import com.devesh.vedahostelwarden.ui.components.*
import com.devesh.vedahostelwarden.ui.theme.*
import com.devesh.vedahostelwarden.viewmodel.NoticeViewModel

@Composable
fun NoticesScreen(
    noticeViewModel: NoticeViewModel,
    onCreateNoticeClick: () -> Unit
) {
    val notices by noticeViewModel.notices.collectAsState()

    Scaffold(
        topBar = {
            VedaTopBar(
                title = "Notices & Announcements",
                subtitle = "Hostel Communication Portal"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNoticeClick,
                containerColor = VedaBluePrimary,
                contentColor = VedaTextPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create Notice")
            }
        },
        containerColor = VedaNavyBackground
    ) { innerPadding ->
        if (notices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Campaign,
                        contentDescription = null,
                        tint = VedaTextMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No notices published yet", style = MaterialTheme.typography.bodyLarge.copy(color = VedaTextSecondary))
                }
            }
        } else {
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

                items(notices, key = { it.noticeId }) { notice ->
                    NoticeCard(
                        notice = notice,
                        onDelete = { noticeViewModel.deleteNotice(notice.noticeId) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun NoticeCard(
    notice: Notice,
    onDelete: () -> Unit
) {
    VedaCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(VedaBluePrimary.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = notice.targetDetail,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = VedaBlueLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }

            Text(
                text = notice.createdAt,
                style = MaterialTheme.typography.bodySmall.copy(color = VedaTextMuted, fontSize = 11.sp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = notice.title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = VedaTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = notice.message,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = VedaTextSecondary,
                fontSize = 13.sp
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "By ${notice.createdBy}",
                style = MaterialTheme.typography.bodySmall.copy(color = VedaTextMuted, fontSize = 11.sp)
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Notice",
                    tint = VedaDangerRed.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
