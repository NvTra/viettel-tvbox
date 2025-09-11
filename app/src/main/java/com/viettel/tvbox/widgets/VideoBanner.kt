package com.viettel.tvbox.widgets

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getVideoUrl
import com.viettel.tvbox.view_model.BannerViewModel

@OptIn(UnstableApi::class)
@Composable
fun VideoBanner() {
    val bannerViewModel: BannerViewModel = viewModel()
    val isLoading = bannerViewModel.isLoading
    val bannerVideo = bannerViewModel.bannerVideo
    val error = bannerViewModel.error

    LaunchedEffect(Unit) {
        bannerViewModel.getAllBanner()
    }

    when {
        isLoading -> {
            Text("Đang tải banner...")
        }

        error != null -> {
            Text("Lỗi: $error")
        }

        bannerVideo != null && bannerVideo.isNotEmpty() -> {
            val context = LocalContext.current
            val image = bannerVideo[0].image
            if (!image.isNullOrBlank()) {
                val videoUrl = getVideoUrl(image)
                val exoPlayer = remember(videoUrl) {
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(MediaItem.fromUri(videoUrl.toUri()))
                        repeatMode = ExoPlayer.REPEAT_MODE_ALL
                        playWhenReady = true
                        volume = 0f
                        prepare()
                    }
                }
                DisposableEffect(exoPlayer) {
                    onDispose { exoPlayer.release() }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clipToBounds()
                ) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .align(Alignment.TopCenter),
                        factory = { context ->
                            PlayerView(context).apply {
                                player = exoPlayer
                                useController = false
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                                layoutParams = android.view.ViewGroup.LayoutParams(
                                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                                )

                                setKeepContentOnPlayerReset(true)
                                exoPlayer.addListener(object :
                                    androidx.media3.common.Player.Listener {
                                    override fun onVideoSizeChanged(videoSize: androidx.media3.common.VideoSize) {
                                        post {
                                            requestLayout()
                                        }
                                    }
                                })
                            }
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.2f))
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Thế giới game - trải nghiệm không giới hạn",
                            color = WhiteColor,
                            style = Typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            } else {
                Text("Không tìm thấy video hợp lệ cho banner.")
            }
        }
    }
}