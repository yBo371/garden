package com.example.gardenapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gardenapp.ui.navigation.bottomDestinations
import com.example.gardenapp.ui.screens.GardenScreen
import com.example.gardenapp.ui.screens.MarketScreen
import com.example.gardenapp.ui.screens.ProfileScreen
import com.example.gardenapp.viewmodel.MainViewModel

@Composable
fun GardenApp(viewModel: MainViewModel = viewModel()) {
    val route by viewModel.selectedRoute.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        FakeStatusBar()
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (route) {
                "garden" -> GardenScreen()
                "market" -> MarketScreen()
                "profile" -> ProfileScreen()
            }
        }
        NavigationBar(
            containerColor = Color.White
        ) {
            bottomDestinations.forEach { destination ->
                val selected = route == destination.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { viewModel.selectRoute(destination.route) },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4CAF50),
                        selectedTextColor = Color(0xFF4CAF50),
                        unselectedIconColor = Color(0xFF9CA3AF),
                        unselectedTextColor = Color(0xFF9CA3AF),
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
private fun FakeStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 20.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "9:41",
            color = Color(0xFF374151),
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF374151))
            )
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF374151))
            )
        }
    }
}
