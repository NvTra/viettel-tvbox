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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.BG_1A1A1A
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.ColorTransparent
import com.viettel.tvbox.theme.GapH6
import com.viettel.tvbox.theme.GapW4
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.theme.ViettelRed50
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.view_model.AuthViewModel

enum class AccountManagementItem(
    val icon: Int, val route: String, val title: String
) {
    ACCOUNT_DETAIL(
        R.drawable.ic_user,
        "account_detail",
        "Thông tin tài khoản"
    ),
    CHANGE_PASSWORD(R.drawable.ic_lock, "change_password", "Đổi mật khẩu")
}

enum class HistoryItem(
    val icon: Int, val route: String, val title: String
) {
    GAME_HISTORY(
        R.drawable.ic_history,
        "game_history",
        "Lịch sử chơi game"
    ),
    ACCESS_HISTORY(
        R.drawable.ic_clock,
        "access_history",
        "Lịch sử truy cập"
    ),
    PAY_HISTORY(R.drawable.ic_credit_card, "pay_history", "Lịch sử thanh toán")
}

@Composable
fun MyAccountSideBar(
    onLogout: () -> Unit,
    navController: NavController,
    selectedRoute: String,
    onItemSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
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
            .padding(horizontal = 10.dp, vertical = 12.dp)) {
        Column {
            Text(
                "Tài khoản của tôi",
                style = Typography.titleSmall,
                color = WhiteColor,
                modifier = Modifier.padding(8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color(0xFF3A3A3A))
            )
            GapH6()
            AccountManagementItem.entries.forEach { item ->
                SideBarAccountIcon(
                    title = item.title,
                    icon = item.icon,
                    selected = item.route == selectedRoute,
                    onClick = { onItemSelected(item.route) })
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color(0xFF3A3A3A))
            )
            GapH6()
            Text(
                "Lịch sử",
                style = Typography.labelSmall.copy(fontSize = 6.sp),
                fontWeight = FontWeight.Bold,
                color = BG_E0E0E0E.copy(0.8f),
                modifier = Modifier.padding(8.dp)
            )
            HistoryItem.entries.forEach { item ->
                SideBarAccountIcon(
                    title = item.title,
                    icon = item.icon,
                    selected = item.route == selectedRoute,
                    onClick = { onItemSelected(item.route) })
            }
        }
        LogoutButton(onLogout)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SideBarAccountIcon(
    title: String, icon: Int, selected: Boolean, onClick: () -> Unit
) {
    var isFocus by remember { mutableStateOf(false) }

    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFocus) BG_1A1A1A else Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            if (isFocus || selected) 0.5.dp else 0.3.dp,
            color = when {
                isFocus -> ViettelPrimaryColor
                selected -> ViettelPrimaryColor
                else -> ColorTransparent
            },
        ),
        modifier = Modifier
            .padding(bottom = 2.dp)
            .height(25.dp)
            .fillMaxWidth()
            .onFocusChanged { isFocus = it.isFocused },
    ) {
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = 6.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = when {
                    isFocus -> ViettelPrimaryColor
                    selected -> ViettelPrimaryColor
                    else -> BG_E0E0E0E
                },
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.CenterVertically)
            )
            GapW4()
            Text(
                text = title,
                color = when {
                    isFocus -> ViettelPrimaryColor
                    selected -> ViettelPrimaryColor
                    else -> BG_E0E0E0E
                },
                style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Icon(
                painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = when {
                    isFocus -> ViettelPrimaryColor
                    selected -> ViettelPrimaryColor
                    else -> Color.Gray
                },
                modifier = Modifier
                    .size(9.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LogoutButton(onLogout: () -> Unit) {
    var isFocus by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val authModel: AuthViewModel = viewModel()
    Button(
        onClick = {
            UserPreferences.getInstance(context).clearAuth()
            authModel.logout()
            onLogout()
        },
        border =
            BorderStroke(
                width = if (isFocus) 0.5.dp else 0.3.dp,
                color = if (isFocus) ViettelPrimaryColor else ViettelPrimaryColor,
            ),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFocus) BG_1A1A1A else ViettelRed50
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .height(30.dp)
            .fillMaxWidth()
            .padding(start = 8.dp, top = 4.dp)
            .onFocusChanged { isFocus = it.isFocused },
    ) {
        Row(
            modifier = Modifier
                .background(Color.Transparent, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = null,
                tint = if (isFocus) WhiteColor else ViettelPrimaryColor,
                modifier = Modifier
                    .size(9.dp)
                    .align(Alignment.CenterVertically)
            )
            GapW4()
            Text(
                text = "Đăng xuất",
                color = if (isFocus) WhiteColor else ViettelPrimaryColor,
                style = Typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}