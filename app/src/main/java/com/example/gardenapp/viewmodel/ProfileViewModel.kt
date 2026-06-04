package com.example.gardenapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenapp.data.GameStore
import com.example.gardenapp.data.GardenRepository
import com.example.gardenapp.model.EncyclopediaEntry
import com.example.gardenapp.model.MarketItem
import com.example.gardenapp.model.ProfileSummary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ProfileUiState(
    val summary: ProfileSummary = GardenRepository.profileSummary(),
    val entries: List<EncyclopediaEntry> = GardenRepository.encyclopediaEntries(),
    val warehouseItems: List<MarketItem> = GardenRepository.marketItems()["warehouse"].orEmpty()
)

class ProfileViewModel : ViewModel() {
    val uiState: StateFlow<ProfileUiState> =
        GameStore.state.map {
            ProfileUiState(
                summary = GardenRepository.profileSummary(),
                entries = GardenRepository.encyclopediaEntries(),
                warehouseItems = GameStore.warehouseItems()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState(warehouseItems = GameStore.warehouseItems())
        )
}
