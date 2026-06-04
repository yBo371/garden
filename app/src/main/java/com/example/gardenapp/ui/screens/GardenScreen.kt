package com.example.gardenapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gardenapp.model.GardenPlotState
import com.example.gardenapp.model.MarketItem
import com.example.gardenapp.ui.components.FloatingActionTray
import com.example.gardenapp.ui.components.GardenPlotCard
import com.example.gardenapp.ui.components.GlassBadge
import com.example.gardenapp.ui.theme.GrassBottom
import com.example.gardenapp.ui.theme.SkyMiddle
import com.example.gardenapp.ui.theme.SkyTop
import com.example.gardenapp.viewmodel.GardenViewModel

@Composable
fun GardenScreen(viewModel: GardenViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var seedPickerPlotId by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SkyTop, SkyMiddle, GrassBottom)))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 12.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(listOf("header")) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassBadge {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFACC15), CircleShape)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("L", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = uiState.level,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color(0xFF245D2C),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    GlassBadge {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFD97706)
                        )
                        Text(
                            text = uiState.coins,
                            modifier = Modifier.padding(start = 4.dp),
                            color = Color(0xFFD97706),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            items(listOf("task")) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.34f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = uiState.message,
                        color = Color(0xFF245D2C),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(
                items = uiState.plots,
                key = { plot -> plot.id }
            ) { plot ->
                GardenPlotCard(
                    plot = plot,
                    onClick = {
                        if (plot.state == GardenPlotState.EMPTY && uiState.seeds.isNotEmpty()) {
                            seedPickerPlotId = plot.id
                        } else {
                            viewModel.interactWithPlot(plot.id)
                        }
                    }
                )
            }
        }

        FloatingActionTray(
            actions = uiState.actions,
            selectedToolId = uiState.selectedToolId,
            onActionClick = viewModel::selectTool,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
        )
    }

    seedPickerPlotId?.let { plotId ->
        SeedPickerDialog(
            seeds = uiState.seeds,
            onSeedClick = { seedId ->
                viewModel.plantSeed(plotId, seedId)
                seedPickerPlotId = null
            },
            onDismiss = { seedPickerPlotId = null }
        )
    }
}

@Composable
private fun SeedPickerDialog(
    seeds: List<MarketItem>,
    onSeedClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "\u9009\u62e9\u79cd\u5b50",
                color = Color(0xFF245D2C),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                seeds.forEach { seed ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF0FDF4))
                            .clickable { onSeedClick(seed.id) }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!seed.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = seed.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = seed.title,
                                color = Color(0xFF245D2C),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "\u5e93\u5b58 x${seed.count}",
                                color = Color(0xFF16A34A),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("\u53d6\u6d88")
            }
        }
    )
}
