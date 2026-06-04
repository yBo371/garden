package com.example.gardenapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gardenapp.ui.components.EncyclopediaCell
import com.example.gardenapp.ui.components.MarketItemCard
import com.example.gardenapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val summary = uiState.summary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .padding(bottom = 28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!summary.avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = summary.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFFDCFCE7), CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7))
                        .border(4.dp, Color(0xFFBBF7D0), CircleShape)
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(summary.nickname, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                Text(
                    text = summary.progressText,
                    modifier = Modifier.padding(top = 4.dp),
                    color = Color(0xFF6B7280)
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF0FDF4)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("\u9c9c\u82b1\u56fe\u9274", fontWeight = FontWeight.Bold, color = Color(0xFF166534))
                    Text(summary.collectionText, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(252.dp)
                        .padding(top = 16.dp),
                    userScrollEnabled = false,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.entries) { entry ->
                        EncyclopediaCell(entry = entry)
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFFFF7ED)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "\u6211\u7684\u4ed3\u5e93",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9A3412)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.warehouseItems.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { item ->
                                MarketItemCard(
                                    item = item,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            summary.achievements.forEach { achievement ->
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFFFF7ED),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFED7AA))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFF97316)
                        )
                        Text(
                            text = achievement.title,
                            modifier = Modifier.padding(start = 12.dp),
                            color = Color(0xFF9A3412),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
