package com.example.gardenapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomDestinations = listOf(
    BottomDestination("garden", "\u82b1\u56ed", Icons.Outlined.LocalFlorist),
    BottomDestination("market", "\u5e02\u573a", Icons.Outlined.ShoppingCart),
    BottomDestination("profile", "\u6211\u7684", Icons.Outlined.AccountCircle)
)
