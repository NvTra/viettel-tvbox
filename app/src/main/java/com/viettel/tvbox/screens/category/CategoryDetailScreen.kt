package com.viettel.tvbox.screens.category

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.Text
import com.viettel.tvbox.view_model.CategoryViewModel
import com.viettel.tvbox.widgets.BannerImage
import com.viettel.tvbox.widgets.GameCard


@Composable
fun CategoryDetailScreen(id: String, navController: NavController) {
    val viewModel: CategoryViewModel = viewModel()
    val category = viewModel.category
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }

    LaunchedEffect(Unit) {
        viewModel.getDetailCategory(id = id, type = userPres.getUserInformation()?.forAge ?: "ALL")
    }
    when {
        isLoading -> {
            LoadingIndicator()
        }

        error != null -> {
            Text("Error: $error")
        }

        category != null -> {
            Column {
                BannerImage(
                    title = category.type ?: "",
                    subTitle = "Thể loại game - ${category.type ?: ""}"
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(category.games ?: emptyList()) { index, game ->
                        GameCard(
                            game.id ?: "",
                            game.title ?: "",
                            game.imageScreen ?: "",
                            navController
                        )
                    }
                }
            }
        }
    }
}

