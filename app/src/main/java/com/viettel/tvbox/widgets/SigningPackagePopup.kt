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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
    val showRegistrationInfo = remember { mutableStateOf(false) }
    var isCancelFocus by remember { mutableStateOf(false) }
    var isSubmitFocus by remember { mutableStateOf(false) }

    if (showRegistrationInfo.value) {
        RegistrationInfoPopup(onDismiss = {
            showRegistrationInfo.value = false
            onDismiss()
        })
    } else {
        Dialog(
            onDismissRequest = onDismiss, properties = DialogProperties(
                dismissOnBackPress = true, usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
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
                                    containerColor = if (isCancelFocus) WhiteColor else BG_1A1A1A,
                                ),
                                modifier = Modifier
                                    .height(30.dp)
                                    .weight(1f)
                                    .onFocusChanged { focusState ->
                                        isCancelFocus = focusState.isFocused
                                    }
                            ) {
                                Text(
                                    "Bỏ qua",
                                    style = Typography.bodySmall,
                                    color = if (isCancelFocus) BlackColor else WhiteColor,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Button(
                                onClick = {
                                    showRegistrationInfo.value = true
                                },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSubmitFocus) WhiteColor else ViettelPrimaryColor,
                                ),
                                modifier = Modifier
                                    .height(30.dp)
                                    .weight(1f)
                                    .onFocusChanged { focusState ->
                                        isSubmitFocus = focusState.isFocused
                                    }
                            ) {
                                Text(
                                    "Đăng ký",
                                    style = Typography.bodySmall,
                                    color = if (isSubmitFocus) BlackColor else WhiteColor,
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
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RegistrationInfoPopup(onDismiss: () -> Unit) {
    var isFocus by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = true, usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = BlackColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(430.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Dịch vụ chưa được đăng ký trên tài khoản của Quý khách.",
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = WhiteColor,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            "Vui lòng đăng ký bằng điện thoại để sử dụng dịch vụ CloudGame.",
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = WhiteColor,
                            textAlign = TextAlign.Center
                        )

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Hoặc truy cập đường link ",
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = WhiteColor,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                "cloudgame.vn/subcriptions",
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = ViettelPrimaryColor,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                " để xem hướng dẫn chi tiết.",
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = WhiteColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    GapH12()

                    Button(
                        onClick = { onDismiss() },
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFocus) WhiteColor else BG_1A1A1A,
                        ),
                        modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Đóng",
                            style = Typography.bodySmall,
                            color = if (isFocus) BlackColor else WhiteColor,
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