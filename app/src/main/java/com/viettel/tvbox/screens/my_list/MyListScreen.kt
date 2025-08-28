package com.viettel.tvbox.screens.my_list

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.BG_DB27777
import com.viettel.tvbox.theme.GapW4
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.view_model.UserViewModel
import com.viettel.tvbox.widgets.CustomScaffold
import com.viettel.tvbox.widgets.GameCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyListScreen(label: String, navController: NavController) {
    val selected = remember { mutableIntStateOf(1) }


    CustomScaffold(label) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                GameButton(
                    iconResId = R.drawable.ic_clock, title = "Chơi gần đây", onClick = {
                        selected.intValue = 1
                    }, selected = selected.intValue == 1
                )

                GameButton(
                    iconResId = R.drawable.ic_heart,
                    title = "Yêu thích",
                    onClick = { selected.intValue = 2 },
                    selected = selected.intValue == 2
                )
            }

            if (selected.intValue == 1) {
                GapW4()
                GamePlayHistory(navController = navController)
            } else if (selected.intValue == 2) {
                GapW4()
                FavoriteGame(navController = navController)
            }
        }

    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GameButton(
    iconResId: Int, title: String, onClick: () -> Unit, selected: Boolean? = false
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = if (selected == true) BG_DB27777 else Color.Transparent),
        shape = RoundedCornerShape(size = 30.dp),
        modifier = Modifier.height(30.dp),
        border = BorderStroke(
            width = if (selected == true) 0.dp else 1.dp,
            color = if (selected == true) Color.Transparent else BG_DB27777
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (selected == true) WhiteColor else BG_DB27777
            )
            GapW4()
            Text(
                text = title, style = Typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                ), color = if (selected == true) WhiteColor else BG_DB27777
            )
        }
    }
}

@Composable
fun GamePlayHistory(navController: NavController) {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }
    val viewModel: UserViewModel = viewModel()
    val gamePlayHistory = viewModel.gamePlayHistory
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    LaunchedEffect(Unit) {
        val endDate = Date()
        val startDate = Date(0L)
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val startDateStr = formatter.format(startDate)
        val endDateStr = formatter.format(endDate)
        viewModel.getGamePlayHistory(
            startDate = startDateStr,
            endDate = endDateStr,
            forAge = userPres.getUserInformation()?.forAge ?: ""
        )
    }

    Box(
        modifier = Modifier
            .height(155.dp)
            .background(Color.Transparent)
    ) {
        when {
            isLoading -> {
                LoadingIndicator()
            }

            error != null -> {
                Text(text = "Error: $error", color = Color.White)
            }

            gamePlayHistory == null -> {
                Text(
                    text = "Chưa có lịch sử chơi",
                    style = Typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                )
            }

            true -> {
                if (gamePlayHistory.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .background(Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        itemsIndexed(
                            gamePlayHistory
                        ) { index, game ->
                            GameCard(
                                game.gid ?: "",
                                game.name ?: "",
                                game.imageHTML ?: "",
                                navController
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Chưa có game đã chơi",
                        style = Typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteGame(navController: NavController) {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }
    val viewModel: UserViewModel = viewModel()
    val favoriteGame = viewModel.favoriteGame
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    LaunchedEffect(Unit) {
        viewModel.getFavoriteGame()
    }

    Box(
        modifier = Modifier
            .height(155.dp)
            .background(Color.Transparent)
    ) {
        when {
            isLoading -> {
                LoadingIndicator()
            }

            error != null -> {
                Text(text = "Error: $error", color = Color.White)
            }

            favoriteGame == null -> {
                Text(
                    text = "Chưa có game yêu thích",
                    style = Typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                )
            }

            true -> {
                if (favoriteGame.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        itemsIndexed(favoriteGame) { index, game ->
                            GameCard(
                                game.id ?: "",
                                game.title ?: "",
                                game.imageScreen ?: "",
                                navController
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Chưa có game yêu thích",
                        style = Typography.headlineMedium,
                        color = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}