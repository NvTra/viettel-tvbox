package com.viettel.tvbox.screens.my_account.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.viettel.tvbox.screens.my_account.MyAccountLayout
import com.viettel.tvbox.theme.BG_2E2E2E
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.widgets.CustomTextField

@Composable
fun ChangePassword() {
    var currentPwd by remember { mutableStateOf("") }
    var newPwd by remember { mutableStateOf("") }
    var confirmNewPwd by remember { mutableStateOf("") }

    fun onSubmit() {
        val changePasswordRequest = mapOf(
            "currentPwd" to currentPwd,
            "newPwd" to newPwd,
            "confirmNewPwd" to confirmNewPwd
        )
        print("Change password: $changePasswordRequest")
    }

    fun onClearText() {
        currentPwd = ""
        newPwd = ""
        confirmNewPwd = ""
    }

    MyAccountLayout(
        title = "Đổi mật khẩu",
        subTitle = "Vui lòng nhập thông tin để đổi mật khẩu.",
        body = {
            Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                Text(
                    text = buildAnnotatedString {
                        append("Mật khẩu hiện tại")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    style = Typography.titleSmall,
                    color = BG_E0E0E0E
                )
                CustomTextField(
                    value = currentPwd,
                    onValueChange = { currentPwd = it },
                    backgroundColor = Color.Black.copy(alpha = 0.7f),
                    placeholder = "Nhập mật khẩu hiện tại",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append("Mật khẩu mới")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    style = Typography.titleSmall,
                    color = BG_E0E0E0E
                )
                CustomTextField(
                    value = newPwd,
                    onValueChange = { newPwd = it },
                    backgroundColor = Color.Black.copy(alpha = 0.7f),
                    placeholder = "Nhập mật khẩu mới",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append("Xác nhận mật khẩu mới")
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    style = Typography.titleSmall,
                    color = BG_E0E0E0E
                )
                CustomTextField(
                    value = confirmNewPwd,
                    onValueChange = { confirmNewPwd = it },
                    backgroundColor = Color.Black.copy(alpha = 0.7f),
                    placeholder = "Nhập lại mật khẩu mới",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { onClearText() },
                        colors = ButtonDefaults.buttonColors(containerColor = BG_2E2E2E),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(25.dp)
                    ) {
                        Text(
                            "Hủy",
                            style = Typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = BG_E0E0E0E
                        )
                    }

                    Button(
                        onClick = { onSubmit() },
                        colors = ButtonDefaults.buttonColors(containerColor = VietelPrimaryColor),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp),
                        modifier = Modifier.height(25.dp)
                    ) {
                        Text(
                            "Đổi mật khẩu",
                            style = Typography.titleSmall, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    )
}