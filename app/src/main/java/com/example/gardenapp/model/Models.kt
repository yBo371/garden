package com.example.gardenapp.model

import androidx.compose.ui.graphics.Color

enum class GardenPlotState {
    BLOOMING,
    GROWING,
    EMPTY,
    NEED_WATER
}

data class GardenPlot(
    val id: Int,
    val state: GardenPlotState,
    val plantId: String? = null,
    val imageUrl: String? = null,
    val hasBug: Boolean = false,
    val plantedAtMillis: Long? = null,
    val bloomAtMillis: Long? = null,
    val needsWater: Boolean = false,
    val statusText: String = ""
)

data class ToolAction(
    val id: String,
    val label: String,
    val accent: Color
)

data class MarketCategory(
    val id: String,
    val title: String
)

data class MarketItem(
    val id: String,
    val title: String,
    val price: Int? = null,
    val imageUrl: String? = null,
    val lockedLevel: Int? = null,
    val isLocked: Boolean = false,
    val count: Int = 0,
    val showActionButton: Boolean = true
)

data class EncyclopediaEntry(
    val id: String,
    val title: String,
    val imageUrl: String? = null,
    val isUnlocked: Boolean,
    val isHighlighted: Boolean = false
)

data class Achievement(
    val title: String
)

data class ProfileSummary(
    val nickname: String,
    val progressText: String,
    val avatarUrl: String,
    val collectionText: String,
    val achievements: List<Achievement>
)
