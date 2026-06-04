package com.example.gardenapp.data

import androidx.compose.ui.graphics.Color
import com.example.gardenapp.model.Achievement
import com.example.gardenapp.model.EncyclopediaEntry
import com.example.gardenapp.model.GardenPlot
import com.example.gardenapp.model.GardenPlotState
import com.example.gardenapp.model.MarketCategory
import com.example.gardenapp.model.MarketItem
import com.example.gardenapp.model.ProfileSummary
import com.example.gardenapp.model.ToolAction

object GardenRepository {
    fun gardenPlots(): List<GardenPlot> =
        (1..6).map { id ->
            GardenPlot(
                id = id,
                state = GardenPlotState.EMPTY,
                statusText = "\u5f85\u79cd\u690d"
            )
        }

    fun toolActions(): List<ToolAction> = listOf(
        ToolAction("water", "\u6d47\u6c34", Color(0xFF2563EB)),
        ToolAction("bug", "\u9664\u866b", Color(0xFFDC2626)),
        ToolAction("boost", "\u52a0\u901f", Color(0xFFF97316)),
        ToolAction("harvest", "\u6536\u53d6", Color(0xFF16A34A))
    )

    fun marketCategories(): List<MarketCategory> = listOf(
        MarketCategory("seed", "\u79cd\u5b50\u533a"),
        MarketCategory("tool", "\u56ed\u827a\u5de5\u5177")
    )

    fun marketItems(): Map<String, List<MarketItem>> = mapOf(
        "seed" to listOf(
            MarketItem(
                id = "rose",
                title = "\u7ea2\u73ab\u7470\u79cd\u5b50",
                price = 50,
                imageUrl = "https://images.unsplash.com/photo-1526333632117-91f51673d6a8?auto=format&fit=crop&q=80&w=400"
            ),
            MarketItem(
                id = "sunflower",
                title = "\u5411\u65e5\u8475\u79cd\u5b50",
                price = 80,
                imageUrl = "https://images.unsplash.com/photo-1582794543139-8ac9cb0f7b11?auto=format&fit=crop&q=80&w=400"
            ),
            MarketItem(
                id = "cactus",
                title = "\u4ed9\u4eba\u7403\u79cd\u5b50",
                price = 30,
                imageUrl = "https://images.unsplash.com/photo-1554631221-f9603e6808be?auto=format&fit=crop&q=80&w=400"
            ),
            MarketItem(
                id = "lily",
                title = "\u7a00\u6709\u767e\u5408",
                lockedLevel = 15,
                isLocked = true
            )
        ),
        "tool" to listOf(
            MarketItem(
                id = "watering",
                title = "\u9ad8\u7ea7\u6d12\u6c34\u58f6",
                price = 120,
                imageUrl = "https://images.unsplash.com/photo-1599685315640-7e2095b0d58a?auto=format&fit=crop&q=80&w=400"
            ),
            MarketItem(
                id = "fertilizer",
                title = "\u8425\u517b\u80a5\u6599",
                price = 66,
                imageUrl = "https://images.unsplash.com/photo-1617575521317-d2974f3b56d2?auto=format&fit=crop&q=80&w=400"
            )
        ),
        "warehouse" to listOf(
            MarketItem(
                id = "stock-rose",
                title = "\u7ea2\u73ab\u7470 x12",
                price = 0,
                imageUrl = "https://images.unsplash.com/photo-1526333632117-91f51673d6a8?auto=format&fit=crop&q=80&w=400"
            ),
            MarketItem(
                id = "stock-sunflower",
                title = "\u5411\u65e5\u8475 x8",
                price = 0,
                imageUrl = "https://images.unsplash.com/photo-1582794543139-8ac9cb0f7b11?auto=format&fit=crop&q=80&w=400"
            )
        )
    )

    fun profileSummary(): ProfileSummary = ProfileSummary(
        nickname = "\u65b0\u624b\u56ed\u4e01",
        progressText = "\u82b1\u8349\u6536\u85cf\u8fdb\u5ea6\uff1a0%",
        avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&q=80&w=300",
        collectionText = "\u6536\u96c6 0 / 12",
        achievements = listOf(
            Achievement("\u65b0\u624b\u4efb\u52a1\uff1a\u79cd\u4e0b\u7b2c\u4e00\u9897\u79cd\u5b50")
        )
    )

    fun encyclopediaEntries(): List<EncyclopediaEntry> = listOf(
        EncyclopediaEntry(
            id = "rose",
            title = "\u73ab\u7470",
            imageUrl = "https://images.unsplash.com/photo-1526333632117-91f51673d6a8?auto=format&fit=crop&q=80&w=200",
            isUnlocked = true
        ),
        EncyclopediaEntry(
            id = "sunflower",
            title = "\u5411\u65e5\u8475",
            imageUrl = "https://images.unsplash.com/photo-1582794543139-8ac9cb0f7b11?auto=format&fit=crop&q=80&w=200",
            isUnlocked = true,
            isHighlighted = true
        ),
        EncyclopediaEntry(
            id = "lavender",
            title = "\u85b0\u8863\u8349",
            imageUrl = "https://images.unsplash.com/photo-1530836361253-215068444dcb?auto=format&fit=crop&q=80&w=200",
            isUnlocked = true
        ),
        EncyclopediaEntry(id = "locked-1", title = "", isUnlocked = false),
        EncyclopediaEntry(id = "locked-2", title = "", isUnlocked = false),
        EncyclopediaEntry(id = "locked-3", title = "", isUnlocked = false)
    )
}
