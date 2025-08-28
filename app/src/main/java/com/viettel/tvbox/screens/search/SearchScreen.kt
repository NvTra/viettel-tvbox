package com.viettel.tvbox.screens.search

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.screens.keyboard.KeyboardView
import com.viettel.tvbox.screens.keyboard.KeyboardViewModel
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.GapH6
import com.viettel.tvbox.theme.GapH8
import com.viettel.tvbox.theme.Grey400
import com.viettel.tvbox.theme.Grey800
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl
import com.viettel.tvbox.view_model.GameViewModel
import com.viettel.tvbox.view_model.GameViewModelFactory
import com.viettel.tvbox.widgets.CustomScaffold
import com.viettel.tvbox.widgets.GameCard

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SearchScreen(label: String, navController: NavController) {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }
    val keyboardViewModel: KeyboardViewModel = viewModel()
    var inputText by remember { mutableStateOf("") }

    val viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(userPres))
    val gameSearchHistory = viewModel.gameSearchHistory
    val gameSmartSearch = viewModel.gameSmartSearchResults
    val isSmartLoading = viewModel.isSmartLoading
    val error = viewModel.error




    LaunchedEffect(Unit) {
        viewModel.getGameSearchHistory(userPres.getUserInformation()?.forAge ?: "ALL")
    }

    CustomScaffold(label) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            GapH8()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Grey400, shape = RoundedCornerShape(15.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inputText.ifEmpty { "Tìm kiếm" },
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
                            Text(
                                text = "  Lịch sử tìm kiếm",
                                style = Typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = WhiteColor
                            )
                        }
                        GapH6()
                        HistoryPushList(gameSearchHistory, onClearHistory = {
                            viewModel.clearGameSearchHistory()
                        })
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    KeyboardView(
                        inputText = inputText,
                        onInputChanged = {
                            inputText = it
                            viewModel.getGameSmartSearchResults(
                                textSearch = it,
                                type = userPres.getUserInformation()?.forAge ?: "ALL"
                            )
                        },
                        viewModel = keyboardViewModel,
                        onEnter = { query ->
                            viewModel.getGameSmartSearchResults(
                                textSearch = query,
                                type = userPres.getUserInformation()?.forAge ?: "ALL"
                            )
                        },
                        modifier = Modifier
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
                    if (inputText.isNotEmpty()) Column {
                        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                            Text(
                                text = "Gợi ý tìm kiểm",
                                style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        GapH12()
                        if (gameSmartSearch.isNotEmpty()) Box(
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
                        } else Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không tìm thấy kết quả",
                                style = Typography.headlineMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryPushList(
    gameSearchHistory: List<com.viettel.tvbox.models.GameRelation>,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxWidthPx = constraints.maxWidth.toFloat()
        val textMeasurer = rememberTextMeasurer()
        val density = LocalDensity.current
        val buttonHorizontalPadding = with(density) { 16.dp.toPx() }
        val buttonSpacing = with(density) { 4.dp.toPx() }
        val allItems = gameSearchHistory.map { it to false } + (null to true)
        val rowList = mutableListOf<List<Pair<com.viettel.tvbox.models.GameRelation?, Boolean>>>()
        var currentRow = mutableListOf<Pair<com.viettel.tvbox.models.GameRelation?, Boolean>>()
        var currentRowWidth = 0f
        var rowCount = 0
        for ((game, isClear) in allItems) {
            val text = if (isClear) "Xóa tìm kiếm" else game?.title ?: ""
            val textLayoutResult = textMeasurer.measure(
                AnnotatedString(text),
                style = Typography.bodySmall
            )
            val textWidth = textLayoutResult.size.width.toFloat()
            val buttonWidth = textWidth + buttonHorizontalPadding
            val nextWidth =
                if (currentRow.isEmpty()) buttonWidth else currentRowWidth + buttonSpacing + buttonWidth
            if (nextWidth > maxWidthPx) {
                rowList.add(currentRow)
                rowCount++
                if (rowCount == 2) break
                currentRow = mutableListOf()
                currentRowWidth = buttonWidth
                currentRow.add(game to isClear)
            } else {
                currentRow.add(game to isClear)
                currentRowWidth = nextWidth
            }
        }
        if (rowCount < 2 && currentRow.isNotEmpty()) {
            rowList.add(currentRow)
        }
        val limitedRowList = rowList.flatten().take(10)
        val displayRows =
            mutableListOf<List<Pair<com.viettel.tvbox.models.GameRelation?, Boolean>>>()
        var tempRow = mutableListOf<Pair<com.viettel.tvbox.models.GameRelation?, Boolean>>()
        for ((index, item) in limitedRowList.withIndex()) {
            tempRow.add(item)
            if (displayRows.size < rowList.size && (rowList[displayRows.size].size == tempRow.size || index == limitedRowList.lastIndex)) {
                displayRows.add(tempRow)
                tempRow = mutableListOf()
            }
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            displayRows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { (game, isClear) ->
                        if (isClear) {
                            Button(
                                onClick = onClearHistory,
                                colors = ButtonDefaults.buttonColors(containerColor = Grey800),
                                modifier = Modifier.height(25.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            ) {
                                Text(
                                    "Xóa tìm kiếm",
                                    style = Typography.bodySmall.copy(color = Color.White)
                                )
                            }
                        } else {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = Grey800),
                                modifier = Modifier.height(25.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            ) {
                                Text(
                                    game?.title ?: "",
                                    style = Typography.bodySmall,
                                    color = WhiteColor
                                )
                            }
                        }
                    }
                }
                GapH6()
            }
        }
    }
}
