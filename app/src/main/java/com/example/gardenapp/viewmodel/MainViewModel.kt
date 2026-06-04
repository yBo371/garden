package com.example.gardenapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _selectedRoute = MutableStateFlow("garden")
    val selectedRoute: StateFlow<String> = _selectedRoute.asStateFlow()

    fun selectRoute(route: String) {
        _selectedRoute.value = route
    }
}
