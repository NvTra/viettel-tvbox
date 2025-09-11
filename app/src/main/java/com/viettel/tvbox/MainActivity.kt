package com.viettel.tvbox

import UserPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.viettel.tvbox.navigation.AppNavGraph
import com.viettel.tvbox.screens.auths.LoginScreen
import com.viettel.tvbox.screens.splash.SplashScreen
import com.viettel.tvbox.theme.VietteltvTheme
import com.viettel.tvbox.widgets.ToastMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private var doubleBackToExit = false
    private var backPressJob: Job? = null

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBackPressHandler()
        setContent {
            VietteltvTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    var showSplash by remember { mutableStateOf(true) }

                    if (showSplash) {
                        SplashScreen(
                            onSplashFinished = { showSplash = false }
                        )
                    } else {
                        MainContent()
                    }
                }

            }
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleDoubleBackPress()
            }
        })
    }

    private fun handleDoubleBackPress() {
        if (doubleBackToExit) {
            finish()
        } else {
            doubleBackToExit = true
            ToastMessage.success("Ấn BACK lần nữa để thoát")

            backPressJob?.cancel()
            backPressJob = CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                doubleBackToExit = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressJob?.cancel()
    }

    @Composable
    private fun MainContent() {
        val context = LocalContext.current
        val userPrefs = remember { UserPreferences.getInstance(context) }
        var isLoggedIn by remember { mutableStateOf(false) }
        val logoutEvent by userPrefs.logoutEvent.collectAsState()
        LaunchedEffect(Unit) {
            isLoggedIn = UserPreferences.getInstance(context).isLogin()
        }
        LaunchedEffect(logoutEvent) {
            if (logoutEvent) {
                isLoggedIn = false
                userPrefs.resetLogoutEvent()
            }
        }
        if (!isLoggedIn) {
            LoginScreen(onLoginSuccess = { isLoggedIn = true })
        } else {
            val navController = rememberNavController()
            AppNavGraph(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                onLogout = { isLoggedIn = false }
            )
        }

        ToastMessage.Show()
    }
}