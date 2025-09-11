package com.viettel.tvbox.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.Grey700
import com.viettel.tvbox.theme.WhiteColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoPopup(videoUrl: String, onDismiss: () -> Unit) {
    val scope = rememberCoroutineScope()
    var showCloseButton by remember { mutableStateOf(true) }
    var hideJob by remember { mutableStateOf<Job?>(null) }
    val closeButtonFocusRequester = remember { FocusRequester() }
    val mainBoxFocusRequester = remember { FocusRequester() }

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = true
            volume = 1f
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    fun startHideTimer() {
        hideJob?.cancel()
        hideJob = scope.launch {
            delay(3000)
            showCloseButton = false
        }
    }

    fun showCloseButtonWithTimer() {
        showCloseButton = true
        startHideTimer()
    }

    LaunchedEffect(Unit) {
        showCloseButtonWithTimer()
        delay(100)
        try {
            mainBoxFocusRequester.requestFocus()
        } catch (e: Exception) {
        }
    }

    // LaunchedEffect để handle focus khi button show/hide
    LaunchedEffect(showCloseButton) {
        if (showCloseButton) {
            delay(200)
            try {
                closeButtonFocusRequester.requestFocus()
            } catch (e: Exception) {
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.95f)
                .focusRequester(mainBoxFocusRequester)
                .focusable()
                .pointerInput(Unit) {
                    detectTapGestures {
                        showCloseButtonWithTimer()
                    }
                }
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        when (keyEvent.key) {
                            Key.DirectionUp, Key.DirectionDown,
                            Key.DirectionLeft, Key.DirectionRight,
                            Key.DirectionCenter, Key.Enter -> {
                                showCloseButtonWithTimer()
                                true
                            }

                            Key.Back -> {
                                // Back button tắt popup trực tiếp
                                onDismiss()
                                true
                            }

                            else -> {
                                false
                            }
                        }
                    } else {
                        false
                    }
                }
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .border(0.5.dp, Grey700),
                factory = {
                    PlayerView(it).apply {
                        player = exoPlayer
                        useController = false
                        clipToOutline = true
                        isFocusable = false
                        isFocusableInTouchMode = false

                        layoutParams = android.view.ViewGroup.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                }
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = showCloseButton,
                enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.slideInVertically(),
                exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.slideOutVertically(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    ),
                    contentPadding = PaddingValues(12.dp),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .focusRequester(closeButtonFocusRequester)
                        .onFocusChanged { state ->
                            if (state.isFocused) {
                                showCloseButtonWithTimer()
                            }
                        }
                        .border(0.5.dp, WhiteColor.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Đóng",
                        tint = WhiteColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}