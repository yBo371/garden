package com.example.gardenapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenapp.data.GameStore
import com.example.gardenapp.data.GardenRepository
import com.example.gardenapp.model.GardenPlot
import com.example.gardenapp.model.MarketItem
import com.example.gardenapp.model.ToolAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GardenUiState(
    val level: String = "\u7b2c1\u5929",
    val coins: String = "320",
    val plots: List<GardenPlot> = emptyList(),
    val seeds: List<MarketItem> = emptyList(),
    val actions: List<ToolAction> = emptyList(),
    val selectedToolId: String = "water",
    val message: String = ""
)

class GardenViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            while (true) {
                GameStore.refreshPlotTimers(System.currentTimeMillis())
                delay(1_000)
            }
        }
    }

    val uiState: StateFlow<GardenUiState> =
        combine(
            GameStore.state,
            kotlinx.coroutines.flow.flowOf(GardenRepository.toolActions())
        ) { state, actions ->
            GardenUiState(
                coins = "%,d".format(state.coins),
                plots = state.plots,
                seeds = GameStore.warehouseItems(),
                actions = actions,
                selectedToolId = state.selectedToolId,
                message = state.latestMessage
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GardenUiState(
                plots = GardenRepository.gardenPlots(),
                seeds = GameStore.warehouseItems(),
                actions = GardenRepository.toolActions()
            )
        )

    fun selectTool(toolId: String) = GameStore.selectTool(toolId)

    fun interactWithPlot(plotId: Int) = GameStore.interactWithPlot(plotId)

    fun plantSeed(plotId: Int, seedId: String) = GameStore.plantSeed(plotId, seedId)
}
