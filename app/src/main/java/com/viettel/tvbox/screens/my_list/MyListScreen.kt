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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.viettel.tvbox.theme.GapW4
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
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
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)
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
    var isFocus by remember { mutableStateOf(false) }
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = if (selected == true) ViettelPrimaryColor else Color.Transparent),
        shape = RoundedCornerShape(size = 30.dp),
        modifier = Modifier
            .height(25.dp)
            .graphicsLayer(
                scaleX = if (isFocus) 1.15f else 1f,
                scaleY = if (isFocus) 1.15f else 1f
            )
            .onFocusChanged { focusState -> isFocus = focusState.isFocused },
        contentPadding = PaddingValues(horizontal = 12.dp),
        border = when {
            selected == true && isFocus -> BorderStroke(0.5.dp, WhiteColor)
            isFocus -> BorderStroke(0.5.dp, WhiteColor)
            else -> BorderStroke(0.5.dp, ViettelPrimaryColor)
        },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(8.dp),
                tint = when {
                    selected == true -> WhiteColor
                    isFocus -> WhiteColor
                    else -> ViettelPrimaryColor
                }
            )
            GapW4()
            Text(
                text = title, style = Typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ), color = when {
                    selected == true -> WhiteColor
                    isFocus -> WhiteColor
                    else -> ViettelPrimaryColor
                }
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
            .height(140.dp)
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
    val viewModel: UserViewModel = viewModel()
    val favoriteGame = viewModel.favoriteGame
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    LaunchedEffect(Unit) {
        viewModel.getFavoriteGame()
    }

    Box(
        modifier = Modifier
            .height(140.dp)
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
                            .background(Color.Transparent),
                        contentPadding = PaddingValues(horizontal = 12.dp),
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