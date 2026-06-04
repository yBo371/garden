package com.example.gardenapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gardenapp.ui.components.CategoryChip
import com.example.gardenapp.ui.components.MarketItemCard
import com.example.gardenapp.viewmodel.MarketViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MarketScreen(viewModel: MarketViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF5EB))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(text = uiState.title, color = Color(0xFF78350F), fontWeight = FontWeight.Bold)
        Text(
            text = "\u91d1\u5e01\uff1a${uiState.coins}",
            modifier = Modifier.padding(top = 8.dp),
            color = Color(0xFFD97706),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = uiState.message,
            modifier = Modifier.padding(top = 8.dp),
            color = Color(0xFF9A3412)
        )

        FlowRow(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.categories.forEach { category ->
                CategoryChip(
                    category = category,
                    selected = uiState.selectedCategoryId == category.id,
                    onClick = { viewModel.selectCategory(category.id) }
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(1f)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            items(uiState.items) { item ->
                MarketItemCard(
                    item = item,
                    onActionClick = if (item.isLocked || !item.showActionButton) null else {
                        { viewModel.buyItem(item.id) }
                    }
                )
            }
        }
    }
}
