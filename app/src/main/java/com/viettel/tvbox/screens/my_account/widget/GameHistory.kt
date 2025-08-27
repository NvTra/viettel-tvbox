package com.viettel.tvbox.screens.my_account.widget

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.viettel.tvbox.screens.my_account.MyAccountLayout
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.ColorTransparent
import com.viettel.tvbox.theme.GapH2
import com.viettel.tvbox.theme.GapW8
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.utils.getImageUrl
import com.viettel.tvbox.view_model.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GameHistory() {
    MyAccountLayout(
        title = "Lịch sử chơi game",
        subTitle = "Xem lại các game bạn đã chơi",
        isBodyScrollable = false,
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
                    Text(text = "Error: $error", color = Color.White)
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
                                title = game.name ?: "",
                                image = game.imageHTML ?: "",
                                time = game.time ?: ""
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
    title: String,
    image: String,
    time: String,
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
    Row(
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
            .fillMaxWidth()
            .padding(vertical = 8.dp)) {
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
            Text(text = title, style = Typography.labelSmall.copy(lineHeight = 14.sp))
            GapH2()
            Text(text = "Chơi lần cuối ${convertTime(time)}", style = Typography.bodySmall)
        }
    }
}