package com.viettel.tvbox.screens.my_account.widget

import UserPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.viettel.tvbox.screens.my_account.MyAccountLayout
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.ColorTransparent
import com.viettel.tvbox.theme.GapH2
import com.viettel.tvbox.theme.GapW8
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl
import com.viettel.tvbox.view_model.UserViewModel
import com.viettel.tvbox.widgets.LoadingIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GameHistory(navController: NavController) {
    MyAccountLayout(
        title = "Lịch sử chơi game",
        subTitle = "Xem lại các game bạn đã chơi",
        body = {
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


            when {
                isLoading -> {
                    LoadingIndicator()
                }

                error != null -> {
                    Text(text = "Có lỗi xảy ra", color = Color.White)
                }

                gamePlayHistory == null -> {
                    Text(text = "Bạn chưa chơi game nào", color = Color.White)
                }

                true -> {
                    LazyColumn(
                        modifier = Modifier.background(Color.Transparent),
                        contentPadding = PaddingValues(0.dp),

                        ) {
                        itemsIndexed(
                            gamePlayHistory
                        ) { index, game ->
                            GamePlayHistory(
                                id = game.gid ?: "",
                                title = game.name ?: "",
                                image = game.imageHTML ?: "",
                                time = game.time ?: "",
                                navController
                            )
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Bạn đã xem hết lịch sử chơi game",
                                    color = Color.White,
                                    style = Typography.bodySmall
                                )
                            }
                        }
                    }

                }
            }
        })
}

@Composable
fun GamePlayHistory(
    id: String,
    title: String,
    image: String,
    time: String,
    navController: NavController
) {
    fun convertTime(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
            val date = inputFormat.parse(input)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
            outputFormat.format(date!!)
        } catch (e: Exception) {
            input
        }
    }

    var isFocused by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 0.5.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = BG_E0E0E0E.copy(alpha = 0.1f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 4.dp)
            .background(Color.Transparent)
            .onFocusChanged { isFocused = it.isFocused }
            .clickable {
                navController.navigate("game_detail/$id")
            }
    ) {
        Row(
            modifier = Modifier
                .border(
                    if (isFocused) 0.5.dp else 0.dp,
                    if (isFocused) ViettelPrimaryColor else Color.Transparent,
                    RoundedCornerShape(4.dp)
                )
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            AsyncImage(
                model = getImageUrl(image),
                contentDescription = "game",
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(0.dp, ColorTransparent, RoundedCornerShape(4.dp)),
            )
            GapW8()
            Column {
                Text(
                    text = title,
                    style = Typography.labelLarge.copy(lineHeight = 14.sp),
                    color = WhiteColor
                )
                GapH2()
                Text(
                    text = "Chơi lần cuối ${convertTime(time)}",
                    style = Typography.labelSmall,
                    color = BG_E0E0E0E
                )
            }
        }
    }
}