package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.viettel.tvbox.theme.BG_1A1A1A
import com.viettel.tvbox.theme.BlackColor
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SigningPackagePopup(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = BlackColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Vui lòng đăng ký để sử dụng tính năng",
                        style = Typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = WhiteColor
                    )
                    GapH12()
                    Button(
                        onClick = { onDismiss() },
                        shape = ButtonDefaults.shape(RoundedCornerShape(4.dp)),
                        colors = ButtonDefaults.colors(
                            containerColor = BG_1A1A1A,
                            focusedContainerColor = BG_1A1A1A,
                        ),
                        scale = ButtonDefaults.scale(focusedScale = 1f),
                        modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Bỏ qua",
                            style = Typography.displaySmall,
                            color = WhiteColor,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}