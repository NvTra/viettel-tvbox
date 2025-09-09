package com.viettel.tvbox.screens.search

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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.models.LogUserHistory
import com.viettel.tvbox.screens.keyboard.KeyboardView
import com.viettel.tvbox.screens.keyboard.KeyboardViewModel
import com.viettel.tvbox.services.SearchPreferences
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.BlackColor
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.GapH16
import com.viettel.tvbox.theme.GapH4
import com.viettel.tvbox.theme.GapH6
import com.viettel.tvbox.theme.GapH8
import com.viettel.tvbox.theme.Grey400
import com.viettel.tvbox.theme.Grey800
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl
import com.viettel.tvbox.view_model.GameViewModel
import com.viettel.tvbox.view_model.GameViewModelFactory
import com.viettel.tvbox.view_model.HistoryViewModel
import com.viettel.tvbox.widgets.CustomScaffold
import com.viettel.tvbox.widgets.GameCard
import com.viettel.tvbox.widgets.LoadingIndicator

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SearchScreen(label: String, navController: NavController) {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }
    val searchPref = remember { SearchPreferences(context) }
    val keyboardViewModel: KeyboardViewModel = viewModel()
    var inputText by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(searchPref.getSearchHistory()) }
    val historyViewModel: HistoryViewModel = viewModel()
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
            GapH16()
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
                            .border(0.5.dp, Grey400, shape = RoundedCornerShape(15.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inputText.ifEmpty { "Tìm kiếm" },
                            style = Typography.bodySmall,
                            color = BG_E0E0E0E,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 6.dp, horizontal = 8.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            tint = Grey400,
                            modifier = Modifier
                                .size(15.dp)
                                .padding(end = 8.dp)
                        )
                    }

                    GapH12()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = WhiteColor,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = "  Lịch sử tìm kiếm",
                            style = Typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = WhiteColor
                        )
                    }
                    GapH6()
                    SearchHistoryList(
                        history = searchHistory,
                        onKeywordClick = { keyword ->
                            inputText = keyword
                            viewModel.getGameSmartSearchResults(
                                textSearch = keyword,
                                type = userPres.getUserInformation()?.forAge ?: "ALL"
                            )
                        },
                        onClearHistory = {
                            searchPref.clearSearchHistory()
                            searchHistory = emptyList()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

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
                        onSearch = {
                            if (inputText.isNotBlank()) {
                                searchPref.addSearchHistory(inputText)
                                searchHistory = searchPref.getSearchHistory()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            GapH12()
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
                                text = "Kết quả tìm kiếm cho \"$inputText\" (${gameSmartSearch.size} game)",
                                style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = WhiteColor
                            )
                        }
                        GapH8()
                        if (gameSmartSearch.isNotEmpty()) Box(
                            modifier = Modifier
                                .height(140.dp)
                                .background(Color.Transparent)
                        ) {
                            LazyRow(
                                modifier = Modifier.background(Color.Transparent),
                                contentPadding = PaddingValues(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                itemsIndexed(
                                    gameSmartSearch
                                ) { index, game ->
                                    GameCard(
                                        game.id ?: "",
                                        game.title ?: "",
                                        getImageUrl(game.imageScreen ?: ""),
                                        navController,
                                        onClick = {
                                            historyViewModel.saveHistory(
                                                LogUserHistory(
                                                    action = "SEARCH_GAME",
                                                    status = 1,
                                                    targetId = game.id ?: "",
                                                    targetName = game.title ?: "",
                                                    targetType = "GAME",
                                                    userId = userPres.getUserInformation()?.id ?: ""
                                                )
                                            )
                                            viewModel.getGameSearchHistory(
                                                userPres.getUserInformation()?.forAge ?: "ALL"
                                            )
                                        })
                                }
                            }
                        } else Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_search),
                                    contentDescription = null,
                                    tint = Grey400,
                                    modifier = Modifier.size(32.dp)
                                )
                                GapH8()
                                Text(
                                    text = "Không tìm thấy kết quả",
                                    style = Typography.titleMedium,
                                    color = WhiteColor,
                                    textAlign = TextAlign.Center,
                                )
                                GapH4()
                                Text(
                                    text = "Thử tìm kiếm với một từ khoá khác",
                                    style = Typography.bodySmall,
                                    color = Grey400, textAlign = TextAlign.Center,
                                )
                            }
                        }
                    } else {
                        Column {
                            Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                                Text(
                                    text = "Gợi ý tìm kiểm",
                                    style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = WhiteColor
                                )
                            }
                            GapH12()
                            if (gameSearchHistory.isNotEmpty()) Box(
                                modifier = Modifier
                                    .height(140.dp)
                                    .background(Color.Transparent)
                            ) {
                                LazyRow(
                                    modifier = Modifier.background(Color.Transparent),
                                    contentPadding = PaddingValues(horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    itemsIndexed(
                                        gameSearchHistory,
                                        key = { _, game -> game.id ?: "" }) { index, game ->
                                        GameCard(
                                            game.id ?: "",
                                            game.title ?: "",
                                            getImageUrl(game.imageScreen ?: ""),
                                            navController,
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
}

@Composable
fun SearchHistoryList(
    history: List<String>,
    onKeywordClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) return

    BoxWithConstraints(modifier = modifier) {
        val maxWidthPx = constraints.maxWidth.toFloat()
        val textMeasurer = rememberTextMeasurer()
        val density = LocalDensity.current
        val buttonHorizontalPadding = with(density) { 16.dp.toPx() }
        val buttonSpacing = with(density) { 4.dp.toPx() }
        val allItems = history.map { it to false } + (null to true)
        val rowList = mutableListOf<List<Pair<String?, Boolean>>>()
        var currentRow = mutableListOf<Pair<String?, Boolean>>()
        var currentRowWidth = 0f
        var rowCount = 0
        for ((keyword, isClear) in allItems) {
            val text = if (isClear) "Xóa tìm kiếm" else keyword ?: ""
            val textLayoutResult = textMeasurer.measure(
                AnnotatedString(text), style = Typography.bodySmall
            )
            val textWidth = textLayoutResult.size.width.toFloat()
            val buttonWidth = textWidth + buttonHorizontalPadding
            val nextWidth =
                if (currentRow.isEmpty()) buttonWidth else currentRowWidth + buttonSpacing + buttonWidth
            if (nextWidth > maxWidthPx) {
                rowList.add(currentRow)
                rowCount++
                if (rowCount == 3) break
                currentRow = mutableListOf()
                currentRowWidth = buttonWidth
                currentRow.add(keyword to isClear)
            } else {
                currentRow.add(keyword to isClear)
                currentRowWidth = nextWidth
            }
        }
        if (rowCount < 3 && currentRow.isNotEmpty()) {
            rowList.add(currentRow)
        }
        val limitedRowList = rowList.flatten().take(10)
        val displayRows =
            mutableListOf<List<Pair<String?, Boolean>>>()
        var tempRow = mutableListOf<Pair<String?, Boolean>>()
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
                    row.forEach { (keyword, isClear) ->
                        var isFocus by remember { mutableStateOf(false) }
                        if (isClear) {
                            Button(
                                onClick = {
                                    onClearHistory()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isFocus) WhiteColor else Grey800),
                                modifier = Modifier
                                    .height(25.dp)
                                    .onFocusChanged { focusState ->
                                        isFocus = focusState.isFocused
                                    },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            ) {
                                Text(
                                    "Xóa tìm kiếm",
                                    style = Typography.bodySmall.copy(color = if (isFocus) BlackColor else WhiteColor)
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    keyword?.let { onKeywordClick(it) }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = if (isFocus) WhiteColor else Grey800),
                                modifier = Modifier
                                    .height(25.dp)
                                    .onFocusChanged { focusState ->
                                        isFocus = focusState.isFocused
                                    },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            ) {
                                Text(
                                    keyword ?: "",
                                    style = Typography.bodySmall,
                                    color = if (isFocus) BlackColor else WhiteColor
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
