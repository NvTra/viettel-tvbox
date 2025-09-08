package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.viettel.tvbox.theme.BG_1A1A1A
import com.viettel.tvbox.theme.BlackColor
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.theme.WhiteColor

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SigningPackagePopup(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = true, usePlatformDefaultWidth = false
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
                modifier = Modifier.width(220.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Vui lòng đăng ký để chơi game",
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = WhiteColor
                    )
                    GapH12()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BG_1A1A1A,
                            ),
                            modifier = Modifier
                                .height(30.dp)
                                .weight(1f)
                        ) {
                            Text(
                                "Bỏ qua",
                                style = Typography.bodySmall,
                                color = WhiteColor,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Button(
                            onClick = { onDismiss() },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ViettelPrimaryColor,
                            ),
                            modifier = Modifier
                                .height(30.dp)
                                .weight(1f)
                        ) {
                            Text(
                                "Đăng ký",
                                style = Typography.bodySmall,
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
}