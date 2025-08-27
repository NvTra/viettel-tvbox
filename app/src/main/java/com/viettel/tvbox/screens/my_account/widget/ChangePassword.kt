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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.viettel.tvbox.screens.my_account.MyAccountLayout
import com.viettel.tvbox.theme.BG_2E2E2E
import com.viettel.tvbox.theme.BG_E0E0E0E
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.view_model.UserViewModel
import com.viettel.tvbox.widgets.CustomTextField

@Composable
fun ChangePassword() {
    val viewModel: UserViewModel = viewModel()
    var currentPwd by remember { mutableStateOf("") }
    var newPwd by remember { mutableStateOf("") }
    var confirmNewPwd by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(emptyMap<String, String>()) }

    fun onSubmit() {
        val validationErrors = mutableMapOf<String, String>()

        if (currentPwd.isBlank()) validationErrors["current"] = "Nhập mật khẩu hiện tại"

        when {
            newPwd.isBlank() -> validationErrors["new"] = "Nhập mật khẩu mới"
//            newPwd.length < 8 -> validationErrors["new"] = "Tối thiểu 8 ký tự"
            !newPwd.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*]).{8,}$")) ->
                validationErrors["new"] = "Cần có chữ hoa, thường, số và ký tự đặc biệt"

            newPwd == currentPwd -> validationErrors["new"] = "Không được trùng mật khẩu cũ"
        }

        if (confirmNewPwd != newPwd) {
            validationErrors["new"] = "Mật khẩu không khớp"
            validationErrors["confirm"] = "Mật khẩu không khớp"
        }

        errors = validationErrors

        if (errors.isEmpty()) {
            val changePasswordRequest = mapOf(
                "currentPwd" to currentPwd,
                "newPwd" to newPwd,
                "confirmNewPwd" to confirmNewPwd
            )
            viewModel.changePassword(changePasswordRequest)
        }
    }

    fun onClearText() {
        currentPwd = ""
        newPwd = ""
        confirmNewPwd = ""
        errors = emptyMap()
    }

    MyAccountLayout(
        title = "Đổi mật khẩu",
        subTitle = "Thay đổi mật khẩu đăng nhập của bạn",
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
                if (errors.containsKey("current")) {
                    ErrorText(errors["current"]!!)
                }

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
                if (errors.containsKey("new")) {
                    ErrorText(errors["new"]!!)
                }

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
                if (errors.containsKey("confirm")) {
                    ErrorText(errors["confirm"]!!)
                }

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
                            style = Typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ErrorText(message: String) {
    Text(
        message,
        color = Color.Red,
        fontSize = 7.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}