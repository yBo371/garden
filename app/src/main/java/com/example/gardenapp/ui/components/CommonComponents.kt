package com.example.gardenapp.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gardenapp.model.EncyclopediaEntry
import com.example.gardenapp.model.GardenPlot
import com.example.gardenapp.model.GardenPlotState
import com.example.gardenapp.model.MarketCategory
import com.example.gardenapp.model.MarketItem
import com.example.gardenapp.model.ToolAction
import com.example.gardenapp.ui.theme.AmberSoil
import com.example.gardenapp.ui.theme.AmberSoilDark
import com.example.gardenapp.ui.theme.SurfaceGlass

@Composable
fun GlassBadge(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = SurfaceGlass,
        tonalElevation = 0.dp,
        shadowElevation = 3.dp,
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun GardenPlotCard(
    plot: GardenPlot,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val bounceTransition = rememberInfiniteTransition(label = "flower")
    val flowerScale by bounceTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flowerScale"
    )
    val pulseTransition = rememberInfiniteTransition(label = "water")
    val waterScale by pulseTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "waterScale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 104.dp)
            .clickable(onClick = onClick)
            .graphicsLayer {
                rotationX = 5f
                cameraDistance = 14f * density
            }
            .clip(RoundedCornerShape(22.dp))
            .background(
                when {
                    plot.state == GardenPlotState.EMPTY -> Color(0xFFA56536)
                    plot.state == GardenPlotState.NEED_WATER || plot.needsWater -> AmberSoilDark
                    else -> AmberSoil
                }
            )
            .then(
                if (plot.state == GardenPlotState.EMPTY) {
                    Modifier.border(
                        BorderStroke(2.dp, Color.White.copy(alpha = 0.42f)),
                        RoundedCornerShape(22.dp)
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        if (plot.state != GardenPlotState.EMPTY) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.26f)),
                            startY = 84f
                        )
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                when (plot.state) {
                    GardenPlotState.BLOOMING,
                    GardenPlotState.GROWING -> {
                        if (!plot.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = plot.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(if (plot.state == GardenPlotState.BLOOMING) 58.dp else 50.dp)
                                    .offset(y = if (plot.state == GardenPlotState.BLOOMING) (-6).dp else 0.dp)
                                    .scale(if (plot.state == GardenPlotState.BLOOMING) flowerScale else 1f),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    GardenPlotState.EMPTY -> {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.78f),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    GardenPlotState.NEED_WATER -> {
                        if (!plot.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = plot.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = Color(0xFF60A5FA),
                                modifier = Modifier
                                    .size(28.dp)
                                    .scale(waterScale)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = plotTitle(plot),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = plot.statusText.ifBlank {
                        if (plot.state == GardenPlotState.EMPTY) "\u70b9\u51fb\u64ad\u79cd\uff0c\u5f00\u59cb\u79cd\u690d" else "\u751f\u957f\u4e2d"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(
                        if (plot.state == GardenPlotState.EMPTY) {
                            Color(0xFFFFF7ED).copy(alpha = 0.9f)
                        } else {
                            Color.White.copy(alpha = 0.18f)
                        }
                    )
                    .padding(horizontal = 9.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stateLabel(plot),
                    color = if (plot.state == GardenPlotState.EMPTY) Color(0xFF9A3412) else Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
            }
        }

        if (plot.needsWater || plot.state == GardenPlotState.NEED_WATER) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 6.dp, start = 6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color(0xFFDBEAFE))
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "\u5f85\u6d47\u6c34",
                        modifier = Modifier.padding(start = 3.dp),
                        color = Color(0xFF1D4ED8),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }

        if (plot.hasBug) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 2.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

private fun plotTitle(plot: GardenPlot): String =
    when {
        plot.plantId == "rose" -> "\u7ea2\u73ab\u7470"
        plot.plantId == "sunflower" -> "\u5411\u65e5\u8475"
        plot.plantId == "cactus" -> "\u4ed9\u4eba\u7403"
        plot.state == GardenPlotState.EMPTY -> "\u82b1\u7530 ${plot.id}"
        else -> "\u690d\u7269 ${plot.id}"
    }

private fun stateLabel(plot: GardenPlot): String =
    when {
        plot.hasBug -> "\u5f85\u9664\u866b"
        plot.needsWater || plot.state == GardenPlotState.NEED_WATER -> "\u5f85\u6d47\u6c34"
        plot.state == GardenPlotState.BLOOMING -> "\u82b1\u857e\u671f"
        plot.state == GardenPlotState.GROWING -> "\u751f\u957f\u4e2d"
        else -> "\u5f85\u79cd\u690d"
    }

@Composable
fun FloatingActionTray(
    actions: List<ToolAction>,
    modifier: Modifier = Modifier,
    selectedToolId: String? = null,
    onActionClick: (String) -> Unit = {}
) {
    val iconMap = mapOf(
        "water" to Icons.Default.WaterDrop,
        "bug" to Icons.Default.BugReport,
        "boost" to Icons.Default.Bolt,
        "harvest" to Icons.Default.LocalFlorist
    )

    Surface(
        modifier = modifier,
        color = SurfaceGlass,
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
        shadowElevation = 18.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            actions.forEach { action ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            action.accent.copy(
                                alpha = if (selectedToolId == action.id) 0.24f else 0.12f
                            )
                        )
                        .border(
                            width = if (selectedToolId == action.id) 2.dp else 0.dp,
                            color = if (selectedToolId == action.id) action.accent else Color.Transparent,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable { onActionClick(action.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconMap.getValue(action.id),
                        contentDescription = action.label,
                        tint = action.accent
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: MarketCategory,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(999.dp),
        color = if (selected) Color(0xFFD97706) else Color.White,
        shadowElevation = if (selected) 6.dp else 0.dp,
        border = if (selected) null else BorderStroke(1.dp, Color(0xFFF5D6AC))
    ) {
        Text(
            text = category.title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            color = if (selected) Color.White else Color(0xFF78350F),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MarketItemCard(
    item: MarketItem,
    modifier: Modifier = Modifier,
    onActionClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFFDE7CC))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (item.isLocked) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE5E7EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF9CA3AF),
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else if (!item.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF3F4F6))
                )
            }

            Text(
                text = item.title,
                modifier = Modifier.padding(top = 10.dp),
                color = if (item.isLocked) Color(0xFF6B7280) else Color(0xFF78350F),
                fontWeight = FontWeight.Bold
            )

            if (item.isLocked) {
                Text(
                    text = "Lv.${item.lockedLevel} \u89e3\u9501",
                    modifier = Modifier.padding(top = 4.dp),
                    color = Color(0xFF9CA3AF)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.showActionButton) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = item.price.toString(),
                                color = Color(0xFFD97706),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF22C55E))
                                .clickable(enabled = onActionClick != null) { onActionClick?.invoke() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "\u5e93\u5b58 x${item.count}",
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EncyclopediaCell(entry: EncyclopediaEntry, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(if (entry.isUnlocked) Color.White else Color(0xFFF3F4F6))
            .then(
                if (entry.isHighlighted) {
                    Modifier.border(BorderStroke(2.dp, Color(0xFF4CAF50)), shape)
                } else {
                    Modifier
                }
            )
            .alpha(if (entry.isUnlocked) 1f else 0.4f),
        contentAlignment = Alignment.Center
    ) {
        if (entry.isUnlocked) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                if (!entry.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = entry.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = entry.title,
                    modifier = Modifier.padding(top = if (!entry.imageUrl.isNullOrBlank()) 6.dp else 0.dp),
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
