package com.viettel.tvbox.screens.auths


import UserPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viettel.tvbox.R.drawable
import com.viettel.tvbox.models.LoginRequest
import com.viettel.tvbox.services.RetrofitInstance
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.GapH24
import com.viettel.tvbox.theme.GapH8
import com.viettel.tvbox.theme.Grey50
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.widgets.CustomTextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.awaitResponse

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = drawable.login_tv_background),
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "CloudGameTV", style = Typography.displayLarge, color = WhiteColor)
            }

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(250.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    LoginForm(modifier = Modifier.fillMaxHeight(), onLoginSuccess = onLoginSuccess)
                }
                Box(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    QRLoginBox(modifier = Modifier.fillMaxHeight())
                }
            }
            GapH24()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Copyright © Cloud Game | All Rights Reserved",
                    style = Typography.labelLarge,
                    color = WhiteColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun LoginForm(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }

    var buttonFocus by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences.getInstance(context) }

    fun onSubmit() {
        usernameError = null
        passwordError = null
        loginError = null
        var hasError = false
        if (username.isBlank()) {
            usernameError = "Vui lòng nhập số điện thoại"
            hasError = true
        }
        if (password.isBlank()) {
            passwordError = "Vui lòng nhập mật khẩu"
            hasError = true
        }
        if (hasError) return

        coroutineScope.launch(Dispatchers.IO) {
            try {
                val request = LoginRequest(username = username, password = password)
                val response = RetrofitInstance.authService.login(request).awaitResponse()
                if (response.isSuccessful && response.body()?.responseCode?.toInt() == 6999) {
                    val body = response.body()
                    if (body != null) {
                        userPrefs.saveToken(body.accessToken)
                        userPrefs.saveRefreshToken(body.refreshToken)
                        // Fetch user information after login
                        val userInfoResponse =
                            RetrofitInstance.userService.getUserInformation().awaitResponse()
                        if (userInfoResponse.isSuccessful) {
                            val userInfo = userInfoResponse.body()
                            if (userInfo != null) {
                                userPrefs.saveUserInformation(userInfo)
                            }
                        }
                    }
                    kotlinx.coroutines.withContext(Dispatchers.Main) {
                        loginError = null
                        onLoginSuccess()
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorCode = try {
                        errorBodyString?.let { JSONObject(it).optInt("code") } ?: -1
                    } catch (e: Exception) {
                        -1
                    }
                    kotlinx.coroutines.withContext(Dispatchers.Main) {
                        loginError = when (errorCode) {
                            9998 -> "Tài khoản hoặc mật khẩu không đúng"
                            9984 -> "Tài khoản đã bị khóa"
                            else -> "Đăng nhập thất bại ($errorCode)"
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                kotlinx.coroutines.withContext(Dispatchers.Main) {
                    loginError = "Lỗi đăng nhập: ${e.message}"
                }
            }
        }
    }

    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .border(
                width = 0.5.dp, color = Grey50, shape = RoundedCornerShape(12.dp)
            )
            .width(272.dp)
            .fillMaxHeight()
            .padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Đăng nhập với mật khẩu",
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = WhiteColor,
            )
        }
        // Username Input
        Text("Số điện thoại", style = Typography.titleSmall, color = WhiteColor)
        CustomTextField(
            value = username,
            onValueChange = {
                username = it
                if (usernameError != null) usernameError = null
            },
            backgroundColor = Color.Black.copy(alpha = 0.7f),
            placeholder = "Nhập số điện thoại của bạn",
            keyboardType = KeyboardType.Phone,
            leadingIcon = painterResource(drawable.ic_phone),
            modifier = Modifier.padding(vertical = 6.dp)
        )
        if (usernameError != null) {
            Text(
                text = usernameError ?: "",
                color = Color.Red,
                style = Typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        GapH12()
        // Password Input
        Text("Nhập mật khẩu", style = Typography.titleSmall, color = WhiteColor)
        CustomTextField(
            value = password,
            onValueChange = {
                password = it
                if (passwordError != null) passwordError = null
            },
            backgroundColor = Color.Black.copy(alpha = 0.7f),
            placeholder = "Nhập mật khẩu của bạn",
            keyboardType = KeyboardType.Password,
            leadingIcon = painterResource(drawable.ic_lock),
            trailingIcon = painterResource(drawable.ic_eye),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (passwordError != null) {
            Text(
                text = passwordError ?: "",
                color = Color.Red,
                style = Typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        if (loginError != null) {
            Text(
                text = loginError ?: "",
                color = Color.Red,
                style = Typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            )
        }
        GapH12()
        // Login Button
        OutlinedButton(
            onClick = { onSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
                .onFocusChanged { buttonFocus = it.isFocused },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (buttonFocus) ViettelPrimaryColor else Color.Black.copy(alpha = 0.7f),
                contentColor = WhiteColor
            ),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(
                width = 0.5.dp, color = if (buttonFocus) Color.Transparent else ViettelPrimaryColor
            ),
        ) {
            Text(
                text = "Đăng nhập",
                style = Typography.titleSmall,
                color = WhiteColor,

                )
        }

    }
}


@Composable
fun QRLoginBox(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .border(
                width = 0.5.dp, color = Grey50, shape = RoundedCornerShape(12.dp)
            )
            .width(272.dp)
            .fillMaxHeight() // Ensure the column fills the parent height
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Optional: center content vertically
    ) {
        Text(
            "Quét mã QR để đăng nhập",
            color = Color.White,
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(9.dp))
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                .border(
                    width = 0.5.dp, color = Grey50, shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(drawable.ic_qr_code),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
                GapH8()
//                Text(
//                    "Hoặc sử dụng mã OTP: 123456",
//                    style = Typography.titleLarge,
//                    color = WhiteColor.copy(alpha = 0.5f)
//                )
            }
        }
        GapH12()
        Text(
            "Quét mã QR bằng App CloudGame\ntrên điện thoại để đăng nhập",
            color = Color.White.copy(alpha = 0.6f),
            style = Typography.labelLarge.copy(letterSpacing = 0.1.sp),
            textAlign = TextAlign.Center
        )
        Text(
            "Hoặc sử dụng mã OTP: 123456",
            style = Typography.titleLarge,
            color = WhiteColor.copy(alpha = 0.5f)
        )
    }
}
