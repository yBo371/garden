package com.example.gardenapp.data

import com.example.gardenapp.model.GardenPlot
import com.example.gardenapp.model.GardenPlotState
import com.example.gardenapp.model.MarketItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameState(
    val coins: Int = 320,
    val selectedToolId: String = "water",
    val plots: List<GardenPlot> = GardenRepository.gardenPlots(),
    val inventory: Map<String, Int> = mapOf(
        "rose" to 3,
        "sunflower" to 2,
        "cactus" to 1
    ),
    val latestMessage: String = "\u4efb\u52a1\uff1a\u70b9\u51fb\u5f85\u79cd\u690d\u82b1\u7530\uff0c\u64ad\u4e0b\u7b2c\u4e00\u9897\u79cd\u5b50"
)

object GameStore {
    private const val BLOOM_DURATION_MILLIS = 2 * 60 * 60 * 1000L
    private val marketCatalog = GardenRepository.marketItems()
    private val seedIds = listOf("rose", "sunflower", "cactus")

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun selectTool(toolId: String) {
        val label = GardenRepository.toolActions().firstOrNull { it.id == toolId }?.label ?: return
        _state.update { it.copy(selectedToolId = toolId, latestMessage = "\u5df2\u9009\u4e2d$label") }
    }

    fun interactWithPlot(plotId: Int) {
        _state.update { current ->
            val plot = current.plots.firstOrNull { it.id == plotId } ?: return@update current
            when {
                plot.state == GardenPlotState.EMPTY ->
                    if (seedIds.any { current.inventory[it].orZero() > 0 }) {
                        current.copy(latestMessage = "\u8bf7\u5148\u9009\u62e9\u8981\u64ad\u79cd\u7684\u79cd\u5b50")
                    } else {
                        current.copy(latestMessage = "\u4ed3\u5e93\u6ca1\u6709\u53ef\u79cd\u690d\u7684\u79cd\u5b50\uff0c\u53bb\u5e02\u573a\u8d2d\u4e70\u5427")
                    }
                plot.hasBug && current.selectedToolId != "bug" ->
                    current.copy(latestMessage = "\u8fd9\u5757\u571f\u5730\u9700\u8981\u5148\u9664\u866b")
                current.selectedToolId == "water" && (plot.state == GardenPlotState.NEED_WATER || plot.needsWater) ->
                    waterPlot(current, plot)

                current.selectedToolId == "bug" && plot.hasBug ->
                    current.copy(
                        plots = current.plots.replace(plot.copy(hasBug = false)),
                        latestMessage = "\u5df2\u5b8c\u6210\u9664\u866b"
                    )

                current.selectedToolId == "boost" && plot.state == GardenPlotState.GROWING ->
                    current.copy(
                        plots = current.plots.replace(
                            plot.copy(
                                state = GardenPlotState.BLOOMING,
                                needsWater = false,
                                statusText = "\u82b1\u857e\u671f\uff0c\u53ef\u6536\u53d6"
                            )
                        ),
                        latestMessage = "\u52a0\u901f\u6210\u529f\uff0c\u82b1\u6735\u5df2\u76db\u5f00"
                    )

                current.selectedToolId == "harvest" && plot.state == GardenPlotState.BLOOMING ->
                    harvest(current, plot)

                plot.state == GardenPlotState.NEED_WATER || plot.needsWater ->
                    current.copy(latestMessage = "\u8bf7\u5148\u5207\u6362\u5230\u6d47\u6c34\u5de5\u5177")
                plot.state == GardenPlotState.GROWING ->
                    current.copy(latestMessage = plot.statusText.ifBlank { "\u53ef\u4f7f\u7528\u52a0\u901f\u63d0\u5347\u751f\u957f\u901f\u5ea6" })
                plot.state == GardenPlotState.BLOOMING -> current.copy(latestMessage = "\u82b1\u5df2\u76db\u5f00\uff0c\u53ef\u4ee5\u6536\u83b7")
                else -> current
            }
        }
    }

    fun plantSeed(plotId: Int, seedId: String) {
        _state.update { current ->
            val plot = current.plots.firstOrNull { it.id == plotId } ?: return@update current
            if (plot.state != GardenPlotState.EMPTY) {
                return@update current.copy(latestMessage = "\u8fd9\u5757\u82b1\u7530\u5df2\u7ecf\u6709\u690d\u7269\u4e86")
            }
            plantSeed(current, plot, seedId)
        }
    }

    fun refreshPlotTimers(nowMillis: Long) {
        _state.update { current ->
            val refreshed = current.plots.map { plot ->
                when (plot.state) {
                    GardenPlotState.EMPTY -> {
                        if (plot.statusText == "\u5f85\u79cd\u690d") plot else plot.copy(statusText = "\u5f85\u79cd\u690d")
                    }

                    GardenPlotState.GROWING -> refreshGrowingPlot(plot, nowMillis)
                    GardenPlotState.BLOOMING -> {
                        val nextStatus = if (plot.hasBug) {
                            "\u82b1\u857e\u671f\uff0c\u5f85\u9664\u866b"
                        } else {
                            "\u82b1\u857e\u671f\uff0c\u53ef\u6536\u53d6"
                        }
                        if (plot.statusText == nextStatus) plot else plot.copy(statusText = nextStatus)
                    }

                    GardenPlotState.NEED_WATER -> {
                        val nextStatus = "\u5f85\u6d47\u6c34"
                        if (plot.statusText == nextStatus) plot else plot.copy(statusText = nextStatus)
                    }
                }
            }
            if (refreshed == current.plots) current else current.copy(plots = refreshed)
        }
    }

    fun buyItem(itemId: String): Boolean {
        val item = marketCatalog.values.flatten().firstOrNull { it.id == itemId } ?: return false
        if (item.isLocked) {
            _state.update { it.copy(latestMessage = "${item.title} \u5c1a\u672a\u89e3\u9501") }
            return false
        }
        val price = item.price ?: return false
        var success = false
        _state.update { current ->
            if (current.coins < price) {
                current.copy(latestMessage = "\u91d1\u5e01\u4e0d\u8db3\uff0c\u65e0\u6cd5\u8d2d\u4e70${item.title}")
            } else {
                success = true
                current.copy(
                    coins = current.coins - price,
                    inventory = current.inventory.updated(item.id, 1),
                    latestMessage = "\u5df2\u8d2d\u4e70${item.title}"
                )
            }
        }
        return success
    }

    fun warehouseItems(): List<MarketItem> {
        val lookup = marketCatalog.values.flatten().associateBy { it.id }
        return seedIds.mapNotNull { id ->
            val count = state.value.inventory[id].orZero()
            if (count <= 0) null else {
                val source = lookup[id] ?: return@mapNotNull null
                MarketItem(
                    id = source.id,
                    title = source.title.removeSuffix("\u79cd\u5b50"),
                    price = 0,
                    imageUrl = source.imageUrl,
                    count = count,
                    showActionButton = false
                )
            }
        }
    }

    private fun plantSeed(current: GameState, plot: GardenPlot, seedId: String): GameState {
        if (seedId !in seedIds) {
            return current.copy(latestMessage = "\u8fd9\u4e2a\u79cd\u5b50\u6682\u65f6\u4e0d\u80fd\u79cd\u690d")
        }
        if (current.inventory[seedId].orZero() <= 0) {
            return current.copy(latestMessage = "\u4ed3\u5e93\u6ca1\u6709\u8fd9\u79cd\u79cd\u5b50")
        }
        val seed = marketCatalog["seed"].orEmpty().firstOrNull { it.id == seedId }
            ?: return current.copy(latestMessage = "\u8fd9\u4e2a\u79cd\u5b50\u6682\u65f6\u4e0d\u80fd\u79cd\u690d")
        val now = System.currentTimeMillis()
        val bloomAt = now + BLOOM_DURATION_MILLIS
        val plantedPlot = plot.copy(
            state = GardenPlotState.GROWING,
            plantId = seedId,
            imageUrl = seed.imageUrl,
            hasBug = false,
            plantedAtMillis = now,
            bloomAtMillis = bloomAt,
            needsWater = true,
            statusText = formatGrowingStatus(bloomAt, now, true)
        )
        return current.copy(
            plots = current.plots.replace(plantedPlot),
            inventory = current.inventory.updated(seedId, -1),
            latestMessage = "\u5df2\u79cd\u4e0b${seed.title}\uff0c\u8bf7\u53ca\u65f6\u6d47\u6c34"
        )
    }

    private fun harvest(current: GameState, plot: GardenPlot): GameState {
        val plantId = plot.plantId ?: return current.copy(latestMessage = "\u8fd9\u5757\u571f\u5730\u6682\u65e0\u53ef\u6536\u83b7\u7269")
        val name = marketCatalog["seed"].orEmpty().firstOrNull { it.id == plantId }?.title?.removeSuffix("\u79cd\u5b50")
            ?: "\u9c9c\u82b1"
        return current.copy(
            coins = current.coins + 60,
            plots = current.plots.replace(
                plot.copy(
                    state = GardenPlotState.EMPTY,
                    plantId = null,
                    imageUrl = null,
                    hasBug = false,
                    plantedAtMillis = null,
                    bloomAtMillis = null,
                    needsWater = false,
                    statusText = "\u5f85\u79cd\u690d"
                )
            ),
            inventory = current.inventory.updated(plantId, 1),
            latestMessage = "\u5df2\u6536\u83b7$name\uff0c\u83b7\u5f97 60 \u91d1\u5e01\uff0c\u82b1\u7530\u53ef\u518d\u6b21\u79cd\u690d"
        )
    }

    private fun waterPlot(current: GameState, plot: GardenPlot): GameState {
        val now = System.currentTimeMillis()
        val shouldBloom = plot.bloomAtMillis?.let { now >= it } == true
        val wateredPlot = plot.copy(
            state = if (shouldBloom) GardenPlotState.BLOOMING else GardenPlotState.GROWING,
            needsWater = false,
            statusText = if (shouldBloom) {
                "\u82b1\u857e\u671f\uff0c\u53ef\u6536\u53d6"
            } else {
                formatGrowingStatus(plot.bloomAtMillis, now, false)
            }
        )
        return current.copy(
            plots = current.plots.replace(wateredPlot),
            latestMessage = if (shouldBloom) {
                "\u6d47\u6c34\u6210\u529f\uff0c\u690d\u7269\u5df2\u8fdb\u5165\u82b1\u857e\u671f"
            } else {
                "\u6d47\u6c34\u6210\u529f\uff0c\u690d\u7269\u6062\u590d\u751f\u957f"
            }
        )
    }

    private fun refreshGrowingPlot(plot: GardenPlot, nowMillis: Long): GardenPlot {
        val bloomAt = plot.bloomAtMillis
        if (!plot.needsWater && bloomAt != null && nowMillis >= bloomAt) {
            return plot.copy(
                state = GardenPlotState.BLOOMING,
                statusText = if (plot.hasBug) "\u82b1\u857e\u671f\uff0c\u5f85\u9664\u866b" else "\u82b1\u857e\u671f\uff0c\u53ef\u6536\u53d6"
            )
        }
        val nextStatus = formatGrowingStatus(bloomAt, nowMillis, plot.needsWater)
        return if (plot.statusText == nextStatus) plot else plot.copy(statusText = nextStatus)
    }

    private fun formatGrowingStatus(
        bloomAtMillis: Long?,
        nowMillis: Long,
        needsWater: Boolean
    ): String {
        val suffix = if (needsWater) "\uff0c\u5f85\u6d47\u6c34" else "\uff0c\u751f\u957f\u4e2d"
        if (bloomAtMillis == null) return if (needsWater) "\u5f85\u6d47\u6c34" else "\u751f\u957f\u4e2d"
        val remaining = (bloomAtMillis - nowMillis).coerceAtLeast(0L)
        return "${formatDuration(remaining)}\u540e\u8fdb\u5165\u82b1\u857e\u671f$suffix"
    }

    private fun formatDuration(durationMillis: Long): String {
        val totalSeconds = durationMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return "${hours}\u65f6${minutes}\u5206${seconds}\u79d2"
    }

    private fun List<GardenPlot>.replace(newPlot: GardenPlot): List<GardenPlot> =
        map { if (it.id == newPlot.id) newPlot else it }

    private fun Map<String, Int>.updated(key: String, delta: Int): Map<String, Int> {
        val next = (this[key] ?: 0) + delta
        return toMutableMap().apply { put(key, next.coerceAtLeast(0)) }
    }

    private fun Int?.orZero(): Int = this ?: 0
}
