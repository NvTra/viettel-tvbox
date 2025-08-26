package com.viettel.tvbox.screens.category

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
import com.viettel.tvbox.view_model.CategoryViewModel
import com.viettel.tvbox.widgets.CategoryCard
import com.viettel.tvbox.widgets.CustomScaffold

@Composable
fun CategoryScreen(label: String, navController: NavController) {
    val viewModel: CategoryViewModel = viewModel()
    val categories = viewModel.categories
    val isLoading = viewModel.isLoading
    val error = viewModel.error
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }

    LaunchedEffect(Unit) {

        viewModel.fetchCategories(
            type = userPres.getUserInformation()?.forAge ?: "ALL",
            titleId = null
        )
    }
    CustomScaffold(label) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            when {
                isLoading -> {
                    LoadingIndicator()
                }

                error != null -> {
                    Text("Error: $error")
                }

                categories != null -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(6),
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(24.dp),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(categories.cloud ?: emptyList()) { index, category ->
                            CategoryCard(
                                id = category.id ?: "",
                                icon = category.icon ?: "",
                                title = category.type ?: "",
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}