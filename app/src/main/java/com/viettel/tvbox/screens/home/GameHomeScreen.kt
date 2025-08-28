package com.viettel.tvbox.screens.home

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.Text
import com.viettel.tvbox.theme.GapH24
import com.viettel.tvbox.view_model.HomeViewModel
import com.viettel.tvbox.view_model.HomeViewModelFactory
import com.viettel.tvbox.widgets.CustomScaffold
import com.viettel.tvbox.widgets.ListGameHorizontal
import com.viettel.tvbox.widgets.VideoBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameHomeScreen(label: String, navController: NavController) {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(userPres))
    val managerHomeConfig = viewModel.managerHomeConfig
    val gameHot = viewModel.gameHot
    val allGame = viewModel.allGame
    val gamePlayed = viewModel.gamePlayed
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    LaunchedEffect(Unit) {
        viewModel.getManagerHomeConfigData()
    }
    CustomScaffold(label) { innerPadding ->
        when {
            isLoading -> {
                LoadingIndicator()
            }

            error != null -> {
                Text("Error: $error")
            }

            managerHomeConfig != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(
                            rememberScrollState(

                            )
                        )
                ) {
                    VideoBanner()
                    GapH24()
                    if (gamePlayed?.items?.isNotEmpty() == true) {
                        ListGameHorizontal(gamePlayed, navController)
                    }
                    GapH24()
                    ListGameHorizontal(allGame, navController)
                    GapH24()
                    ListGameHorizontal(gameHot, navController)
                    GapH24()
                }
            }
        }
    }

}