package com.viettel.tvbox.screens.search

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.screens.keyboard.KeyboardView
import com.viettel.tvbox.screens.keyboard.KeyboardViewModel
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.GapH16
import com.viettel.tvbox.theme.GapH6
import com.viettel.tvbox.theme.Grey400
import com.viettel.tvbox.theme.Grey800
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl
import com.viettel.tvbox.view_model.GameViewModel
import com.viettel.tvbox.widgets.CustomScaffold
import com.viettel.tvbox.widgets.GameCard

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SearchScreen(label: String, navController: NavController) {
    val keyboardViewModel: KeyboardViewModel = viewModel()
    var inputText by remember { mutableStateOf("") }

    val viewModel: GameViewModel = viewModel()
    val gameSearchHistory = viewModel.gameSearchHistory
    val gameSmartSearch = viewModel.gameSmartSearchResults
    val isSmartLoading = viewModel.isSmartLoading
    val error = viewModel.error


    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }

    LaunchedEffect(Unit) {
        viewModel.getGameSearchHistory(userPres.getUserInformation()?.forAge ?: "ALL")
    }

    CustomScaffold(label) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            GapH16()
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Grey400, shape = RoundedCornerShape(15.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inputText,
                            style = Typography.displaySmall,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 6.dp, horizontal = 8.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            tint = Grey400,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp)
                        )
                    }
                    if (gameSearchHistory.isNotEmpty()) {
                        GapH12()
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painterResource(id = R.drawable.ic_clock),
                                contentDescription = null,
                                tint = WhiteColor,
                                modifier = Modifier.size(9.dp)
                            )
                            Text(text = "  Lịch sử tìm kiếm", style = Typography.titleSmall)
                        }
                        GapH6()
                        Column {
                            LazyRow(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                items(gameSearchHistory) { game ->
                                    Button(
                                        {},
                                        colors = ButtonDefaults.buttonColors(containerColor = Grey800),
                                        modifier = Modifier
                                            .height(25.dp),
                                        contentPadding = PaddingValues(
                                            horizontal = 8.dp,
                                            vertical = 0.dp
                                        ),
                                    ) {
                                        Text(game.title ?: "", style = Typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    KeyboardView(
                        inputText = inputText,
                        onInputChanged = { inputText = it },
                        viewModel = keyboardViewModel,
                        onEnter = { query ->
                            viewModel.getGameSmartSearchResults(
                                textSearch = query,
                                type = userPres.getUserInformation()?.forAge ?: "ALL"
                            )
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    )
                }
            }


            when {
                isSmartLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                error != null -> {}

                else -> {
                    Column {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Text(
                                text = "Gợi ý tìm kiểm",
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        GapH12()
                        Box(
                            modifier = Modifier
                                .height(155.dp)
                                .background(Color.Transparent)
                        ) {
                            LazyRow(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(Color.Transparent),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                itemsIndexed(
                                    gameSmartSearch
                                ) { index, game ->
                                    GameCard(
                                        game.id ?: "",
                                        game.title ?: "",
                                        getImageUrl(game.imageScreen ?: ""),
                                        navController
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}