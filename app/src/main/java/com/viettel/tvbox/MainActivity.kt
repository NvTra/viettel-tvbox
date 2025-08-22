package com.viettel.tvbox

import UserPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.viettel.tvbox.navigation.AppNavGraph
import com.viettel.tvbox.screens.auths.LoginScreen
import com.viettel.tvbox.screens.layouts.Sidebar
import com.viettel.tvbox.theme.VietteltvTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VietteltvTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    val context = LocalContext.current
                    var isLoggedIn by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        isLoggedIn = UserPreferences.getInstance(context).isLogin()
                    }
                    if (!isLoggedIn) {
                        LoginScreen(onLoginSuccess = { isLoggedIn = true })
                    } else {
                        val navController = rememberNavController()
                        Row(modifier = Modifier.fillMaxHeight()) {
                            Sidebar(navController = navController, modifier = Modifier.width(40.dp))
                            AppNavGraph(
                                navController = navController,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }

            }
        }
    }
}