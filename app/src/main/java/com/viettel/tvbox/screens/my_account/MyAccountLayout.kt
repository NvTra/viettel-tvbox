package com.viettel.tvbox.screens.my_account

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.viettel.tvbox.theme.BG_2E2E2E
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.GapH4
import com.viettel.tvbox.theme.Typography

@Composable
fun MyAccountLayout(
    title: String,
    subTitle: String,
    body: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 0.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .border(
                    0.5.dp,
                    BG_2E2E2E,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = title,
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = BG_E0E0E0E
                )
                GapH4()
                Text(
                    text = subTitle,
                    style = Typography.bodyMedium,
                    color = BG_E0E0E0E.copy(alpha = 0.5f)
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .border(
                    0.5.dp,
                    BG_2E2E2E,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(12.dp)
        ) {
            body()
        }
    }
}