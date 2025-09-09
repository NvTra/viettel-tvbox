package com.viettel.tvbox.screens.my_account.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.models.PayHistoryDetail
import com.viettel.tvbox.screens.my_account.MyAccountLayout
import com.viettel.tvbox.theme.BG_2E2E2E
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.GapH2
import com.viettel.tvbox.theme.GapW8
import com.viettel.tvbox.theme.Green400
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.view_model.UserViewModel
import com.viettel.tvbox.widgets.LoadingIndicator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PayHistory(navController: NavController) {
    MyAccountLayout(
        title = "Lịch sử thanh toán",
        subTitle = "Xem lại lịch sử thanh toán",
        body = {
            val viewModel: UserViewModel = viewModel()
            val payHistory = viewModel.payHistory
            val isLoading = viewModel.isLoading
            val error = viewModel.error

            LaunchedEffect(Unit) {
                val endDate = Date()
                val startDate = Date(0L)
                val startDateMillis = startDate.time
                val endDateMillis = endDate.time
                viewModel.getPayHistory(
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

                payHistory == null -> {
                    Text(text = "Không có dữ liệu", color = Color.White)
                }

                true -> {
                    LazyColumn(
                        modifier = Modifier.background(Color.Transparent),
                        contentPadding = PaddingValues(10.dp),
                    ) {
                        itemsIndexed(
                            payHistory
                        ) { index, item ->
                            PayHistory(item)
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Bạn đã xem hết lịch sử thanh toán",
                                    color = BG_E0E0E0E.copy(alpha = 0.6f),
                                    style = Typography.bodyMedium
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
fun PayHistory(
    item: PayHistoryDetail
) {
    fun convertDateTime(input: Long): String {
        return try {
            val date = Date(input)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            input.toString()
        }
    }

    fun convertTime(input: Long): String {
        return try {
            val date = Date(input)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            input.toString()
        }
    }

    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
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
            .fillMaxWidth(),
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
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(BG_2E2E2E, shape = RoundedCornerShape(4.dp))
                    .border(0.dp, Color.Transparent, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_credit_card),
                    contentDescription = null,
                    tint = ViettelPrimaryColor,
                    modifier = Modifier.size(12.dp)
                )
            }
            GapW8()
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Thời gian giao dịch",
                    style = Typography.labelSmall,
                    color = BG_E0E0E0E.copy(alpha = 0.6f)
                )
                GapH2()
                Text(
                    text = convertDateTime(item.createdDate ?: 0),
                    style = Typography.bodyMedium,
                    color = BG_E0E0E0E
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Số tiền",
                    style = Typography.labelSmall,
                    color = BG_E0E0E0E.copy(alpha = 0.6f)
                )
                GapH2()
                Text(
                    text = "${item.price} VNĐ",
                    style = Typography.bodyMedium,
                    color = BG_E0E0E0E
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hình thức",
                    style = Typography.labelSmall,
                    color = BG_E0E0E0E.copy(alpha = 0.6f)
                )
                GapH2()
                Text(
                    text = item.channel ?: "",
                    style = Typography.bodyMedium,
                    color = BG_E0E0E0E
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Gói cước",
                    style = Typography.labelSmall,
                    color = BG_E0E0E0E.copy(alpha = 0.6f)
                )
                GapH2()
                Text(
                    text = item.subName ?: "",
                    style = Typography.bodyMedium,
                    color = BG_E0E0E0E
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Thời hạn",
                    style = Typography.labelSmall,
                    color = BG_E0E0E0E.copy(alpha = 0.6f)
                )
                GapH2()
                Text(
                    text = convertTime(item.expiredTime ?: 0),
                    style = Typography.bodyMedium,
                    color = BG_E0E0E0E
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Trạng thái",
                    style = Typography.labelSmall,
                    color = BG_E0E0E0E.copy(alpha = 0.6f)
                )
                GapH2()
                if (item.status == 2) Box(
                    modifier = Modifier
                        .background(
                            Green400.copy(alpha = 0.2f), RoundedCornerShape(30.dp)
                        )
                        .border(
                            width = 0.2.dp, color = Green400, shape = RoundedCornerShape(30.dp)
                        )
                        .padding(vertical = 2.dp, horizontal = 6.dp),
                ) {
                    Text(
                        text = "Thành công", style = Typography.labelSmall, color = Green400
                    )
                }
                else Box(
                    modifier = Modifier
                        .background(
                            ViettelPrimaryColor.copy(alpha = 0.2f), RoundedCornerShape(30.dp)
                        )
                        .border(
                            width = 0.2.dp,
                            color = ViettelPrimaryColor,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(vertical = 3.dp, horizontal = 6.dp),

                    ) {
                    Text(
                        text = "Thất bại",
                        style = Typography.labelSmall,
                        color = ViettelPrimaryColor
                    )
                }
            }
        }
    }
}