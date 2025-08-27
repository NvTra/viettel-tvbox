package com.viettel.tvbox.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viettel.tvbox.theme.Green400
import com.viettel.tvbox.theme.VietelPrimaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ToastMessage {
    private var _message by mutableStateOf<String?>(null)
    private var _isError by mutableStateOf(false)
    private var job: Job? = null

    fun success(msg: String) {
        show(msg, false)
    }

    fun error(msg: String) {
        show(msg, true)
    }

    fun hide() {
        _message = null; job?.cancel()
    }

    private fun show(msg: String, isError: Boolean) {
        _message = msg; _isError = isError; job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch { delay(3000); hide() }
    }

    @Composable
    fun Show() {
        Box(Modifier.fillMaxSize(), Alignment.BottomEnd) {
            AnimatedVisibility(
                _message != null,
                enter = slideInHorizontally { it } + fadeIn(tween(300)),
                exit = slideOutHorizontally { it } + fadeOut(tween(200))) {
                _message?.let { msg ->
                    Card(
                        Modifier
                            .padding(16.dp)
                            .clickable { hide() }
                            .widthIn(max = 300.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (_isError)
                                VietelPrimaryColor
                            else Green400
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(6.dp)) {
                        Text(
                            text = msg,
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

