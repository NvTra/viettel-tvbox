package com.viettel.tvbox.screens.my_account

import UserPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.BG_1A1A1A
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.GapH6
import com.viettel.tvbox.theme.GapW4
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.theme.WhiteColor

enum class AccountManagementItem(
    val icon: Int,
    val route: String,
    val title: String
) {
    ACCOUNT_DETAIL(R.drawable.ic_user, "account_detail", "Thông tin tài khoản"),
    CHANGE_PASSWORD(R.drawable.ic_lock, "change_password", "Đổi mật khẩu")
}

enum class HistoryItem(
    val icon: Int,
    val route: String,
    val title: String
) {
    GAME_HISTORY(R.drawable.ic_history, "game_history", "Lịch sử chơi game"),
    ACCESS_HISTORY(R.drawable.ic_clock, "access_history", "Lịch sử truy cập"),
    PAY_HISTORY(R.drawable.ic_credit_card, "pay_history", "Lịch sử thanh toán")
}

@Composable
fun MyAccountSideBar(navController: NavController) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(140.dp)
            .fillMaxHeight()
            .drawBehind {
                val strokeWidth = 0.5.dp.toPx()
                val x = size.width - strokeWidth / 2
                drawLine(
                    color = WhiteColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        Column {
            GapH12()
            Text(
                "Tài khoản của tôi",
                style = Typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF3A3A3A))
            )
            GapH6()
            AccountManagementItem.entries.forEachIndexed { index, item ->
                SideBarAccountIcon(title = item.title, icon = item.icon)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF3A3A3A))
            )
            GapH6()
            HistoryItem.entries.forEachIndexed { index, item ->
                SideBarAccountIcon(title = item.title, icon = item.icon)
            }
            
        }
        LogoutButton(navController)

    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SideBarAccountIcon(title: String, icon: Int) {
    var isFocus by remember { mutableStateOf(false) }
    Button(
        onClick = {},
        colors = ButtonDefaults.colors(
            containerColor = Color.Transparent,
            focusedContainerColor = BG_1A1A1A,
        ),
        contentPadding = PaddingValues(0.dp),
        border = ButtonDefaults.border(
            focusedBorder = Border(BorderStroke(1.dp, VietelPrimaryColor)),
        ),
        shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp),
        scale = ButtonDefaults.scale(focusedScale = 1f),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = if (isFocus) Color.White else Color.Gray,
                modifier = Modifier
                    .size(9.dp)
                    .align(Alignment.CenterVertically)
            )
            GapW4()
            Text(
                text = title,
                color = if (isFocus) VietelPrimaryColor else WhiteColor,
                style = Typography.bodySmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = if (isFocus) VietelPrimaryColor else Color.Gray,
                modifier = Modifier
                    .size(9.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LogoutButton(navController: NavController) {
    var isFocus by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Button(
        onClick = {
            UserPreferences.getInstance(context).clearAuth()
            navController.navigate("login_screen") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        },
        colors = ButtonDefaults.colors(
            containerColor = Color.Transparent,
            focusedContainerColor = BG_1A1A1A,
        ),
        contentPadding = PaddingValues(0.dp),
        border = ButtonDefaults.border(
            focusedBorder = Border(BorderStroke(1.dp, VietelPrimaryColor)),
        ),
        shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp),
        scale = ButtonDefaults.scale(focusedScale = 1f),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .background(Color.Transparent)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = null,
                tint = if (isFocus) Color.White else Color.Gray,
                modifier = Modifier
                    .size(9.dp)
                    .align(Alignment.CenterVertically)
            )
            GapW4()
            Text(
                text = "Đăng xuất",
                color = if (isFocus) VietelPrimaryColor else WhiteColor,
                style = Typography.bodySmall,
                modifier = Modifier.weight(1f)
            )

        }
    }
}