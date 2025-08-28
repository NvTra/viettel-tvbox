package com.viettel.tvbox.screens.my_account.widget

import UserPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.viettel.tvbox.R
import com.viettel.tvbox.screens.my_account.MyAccountLayout
import com.viettel.tvbox.theme.BG_2E2E2E
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.ColorTransparent
import com.viettel.tvbox.theme.GapH8
import com.viettel.tvbox.theme.SidebarSelect
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.utils.getImageUrl

@Composable
fun AccountDetail() {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }
    val userInfo = userPres.getUserInformation()

    fun gender(gender: String): String {
        if (gender == "MALE") return "Nam"
        if (gender == "FEMALE") return "Nữ"
        return ""
    }

    fun forAge(forAge: String): String {
        if (forAge == "ADULT") return "Người lớn"
        if (forAge == "CHILDREN") return "Trẻ em"
        return ""
    }

    MyAccountLayout(
        title = "Thông tin tài khoản",
        subTitle = "Thông tin chi tiết tài khoản của bạn.",
        body = {
            Column {
                if (userInfo?.avatar?.isNotEmpty() == true) AsyncImage(
                    model = getImageUrl(userInfo.avatar),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .width(62.dp)
                        .height(62.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(0.dp, ColorTransparent, RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                ) else {
                    Box(
                        modifier = Modifier
                            .width(62.dp)
                            .height(62.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(0.dp, ColorTransparent, RoundedCornerShape(4.dp))
                            .background(SidebarSelect),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_user2),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                GapH8()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextView(
                            title = "Tên hiển thị",
                            value = userInfo?.name ?: "",
                        )
                        TextView(
                            title = "Địa chỉ email",
                            value = userInfo?.email ?: "",
                        )
                        TextView(
                            title = "Giới tính",
                            value = gender(userInfo?.gender ?: ""),
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextView(
                            title = "Số điện thoại",
                            value = userInfo?.name ?: "",
                        )
                        TextView(
                            title = "Ngày sinh",
                            value = userInfo?.dob ?: "",
                        )
                        TextView(
                            title = "Loại tài khoản",
                            value = forAge(userInfo?.forAge ?: ""),
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun TextView(title: String, value: String) {
    Column {
        Text(text = title, style = Typography.titleSmall, color = BG_E0E0E0E)
        GapH8()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    0.5.dp,
                    BG_2E2E2E,
                    RoundedCornerShape(30.dp)
                )
        ) {
            Text(
                text = value,
                style = Typography.titleSmall,
                color = BG_E0E0E0E,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
    }
}