package com.example.gardenapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenapp.data.GameStore
import com.example.gardenapp.data.GardenRepository
import com.example.gardenapp.model.MarketCategory
import com.example.gardenapp.model.MarketItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class MarketUiState(
    val title: String = "\u9c9c\u82b1\u5e02\u573a",
    val categories: List<MarketCategory> = emptyList(),
    val selectedCategoryId: String = "seed",
    val items: List<MarketItem> = emptyList(),
    val coins: String = "320",
    val message: String = ""
)

class MarketViewModel : ViewModel() {
    private val catalog = GardenRepository.marketItems()
    private val selectedCategoryId = MutableStateFlow("seed")

    val uiState: StateFlow<MarketUiState> =
        combine(GameStore.state, selectedCategoryId) { gameState, categoryId ->
            MarketUiState(
                categories = GardenRepository.marketCategories(),
                selectedCategoryId = categoryId,
                items = catalog[categoryId].orEmpty(),
                coins = "%,d".format(gameState.coins),
                message = gameState.latestMessage
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MarketUiState(
                categories = GardenRepository.marketCategories(),
                items = catalog.getValue("seed")
            )
        )

    fun selectCategory(categoryId: String) {
        selectedCategoryId.value = categoryId
    }

    fun buyItem(itemId: String) = GameStore.buyItem(itemId)
}
