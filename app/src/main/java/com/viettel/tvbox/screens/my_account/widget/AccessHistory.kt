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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.viettel.tvbox.models.AccessHistoryDetail
import com.viettel.tvbox.screens.my_account.MyAccountLayout
import com.viettel.tvbox.theme.BG_2E2E2E
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.GapH2
import com.viettel.tvbox.theme.GapW8
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.view_model.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccessHistory() {
    MyAccountLayout(
        title = "Lịch sử truy cập",
        subTitle = "Theo dõi các lần đăng nhập tài khoản",
        isBodyScrollable = false,
        body = {
            val context = LocalContext.current
            val userPres = remember { UserPreferences.getInstance(context) }
            val viewModel: UserViewModel = viewModel()
            val accessHistory = viewModel.accessHistory
            val isLoading = viewModel.isLoading
            val error = viewModel.error

            LaunchedEffect(Unit) {
                val endDate = Date()
                val startDate = Date(0L)
                val startDateMillis = startDate.time
                val endDateMillis = endDate.time
                viewModel.getAccessHistory(
                    startDate = startDateMillis,
                    endDate = endDateMillis,
                )
            }

            when {
                isLoading -> {
                    LoadingIndicator()
                }

                error != null -> {
                    Text(text = "Error: $error", color = Color.White)
                }

                accessHistory == null -> {
                    Text(text = "Không có dữ liệu", color = Color.White)
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.background(Color.Transparent),
                        contentPadding = PaddingValues(0.dp),

                        ) {
                        itemsIndexed(
                            accessHistory
                        ) { index, item ->
                            AccessHistoryItem(item)
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Bạn đã xem hết lịch sử",
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AccessHistoryItem(
    item: AccessHistoryDetail
) {
    fun getIcon(os: String): Int {
        return when {
            os.contains("Windows", ignoreCase = true) -> {
                com.viettel.tvbox.R.drawable.ic_pc_case
            }

            os.contains("Mac", ignoreCase = true) -> {
                com.viettel.tvbox.R.drawable.ic_pc_case
            }

            else -> {
                com.viettel.tvbox.R.drawable.ic_table

            }
        }
    }

    fun convertTime(input: Long): String {
        return try {
            val date = Date(input)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            input.toString()
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
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(BG_2E2E2E, shape = RoundedCornerShape(4.dp))
                .border(0.dp, Color.Transparent, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(
                    getIcon(item.os),
                ),
                contentDescription = null,
                tint = VietelPrimaryColor,
                modifier = Modifier.size(12.dp)
            )
        }
        GapW8()
        Column {
            Text(
                text = "${item.os} - ${if (item.action == "LOGIN") "Đăng nhập" else "Đăng xuất"}",
                style = Typography.labelSmall.copy(lineHeight = 14.sp)
            )
            GapH2()
            Text(
                text = "${item.browser} - ${convertTime(item.createdDate)}",
                style = Typography.bodySmall
            )
        }
    }
}