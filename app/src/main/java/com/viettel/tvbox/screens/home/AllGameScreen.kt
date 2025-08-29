package com.viettel.tvbox.screens.home

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
import androidx.tv.material3.Text
import com.viettel.tvbox.view_model.HomeViewModel
import com.viettel.tvbox.view_model.HomeViewModelFactory
import com.viettel.tvbox.widgets.BannerImage
import com.viettel.tvbox.widgets.GameCard

@Composable
fun AllGameScreen(id: String, title: String, navController: NavController) {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }

    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(userPres))
    val allGameByTitle = viewModel.allGameByTitle
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    LaunchedEffect(Unit) {
        viewModel.getAllGameByTitle(
            forAge = userPres.getUserInformation()?.forAge ?: "All", title = id,
        )
    }
    when {
        isLoading -> {
            LoadingIndicator()
        }

        error != null -> {
            Text("Error: $error")
        }

        allGameByTitle != null -> {
            Column {
                BannerImage(title = title, subTitle = "")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(allGameByTitle) { index, game ->
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