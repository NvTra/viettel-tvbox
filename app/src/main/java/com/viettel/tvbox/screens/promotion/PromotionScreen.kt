package com.viettel.tvbox.screens.promotion

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.viettel.tvbox.view_model.PromotionViewModel
import com.viettel.tvbox.widgets.CustomScaffold
import com.viettel.tvbox.widgets.PromotionCard


@Composable
fun PromotionScreen(
    label: String,
    navController: NavController
) {
    val context = LocalContext.current
    remember { UserPreferences.getInstance(context) }
    val viewModel: PromotionViewModel = viewModel()
    val promotions = viewModel.promotions
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    LaunchedEffect(Unit) {
        viewModel.fetchPromotions()
    }

    CustomScaffold(label) { innerPadding ->
        Column {
            when {
                isLoading -> {
                    LoadingIndicator()
                }

                error != null -> {
                    androidx.tv.material3.Text(text = "Error: $error")
                }

                promotions != null -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .padding(innerPadding),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(promotions.data ?: emptyList()) { index, promo ->
                            PromotionCard(
                                id = promo.id,
                                title = promo.title,
                                description = promo.description,
                                imageUrl = promo.image,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
