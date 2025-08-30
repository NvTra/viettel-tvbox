package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.viettel.tvbox.theme.GapH8
import com.viettel.tvbox.theme.Grey200
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl
import com.viettel.tvbox.view_model.BannerViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerImage(
    title: String, subTitle: String
) {
    val bannerViewModel: BannerViewModel = viewModel()
    val isLoading = bannerViewModel.isLoading
    val bannerList = bannerViewModel.bannerImage
    val error = bannerViewModel.error

    val carouselState = rememberCarouselState { bannerList?.size ?: 0 }

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

        bannerList.isNullOrEmpty() -> {
            Text("Không có banner nào!")
        }

        else -> {
            val repeatCount = 1000
            val infiniteList = remember { List(repeatCount) { bannerList }.flatten() }
            val listState =
                rememberLazyListState(initialFirstVisibleItemIndex = infiniteList.size / 2)
            LaunchedEffect(listState) {
                while (true) {
                    delay(6000)
                    val nextIndex = listState.firstVisibleItemIndex + 1
                    listState.animateScrollToItem(nextIndex)
                    // Reset to middle if near start/end
                    if (nextIndex < bannerList.size || nextIndex > infiniteList.size - bannerList.size) {
                        listState.scrollToItem(infiniteList.size / 2)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                LazyRow(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    items(infiniteList.size) { i ->
                        val item = infiniteList[i]
                        AsyncImage(
                            model = getImageUrl(item.image ?: ""),
                            contentDescription = null,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        )
                    }
                }
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
                        text = title,
                        color = WhiteColor,
                        style = Typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    GapH8()
                    Text(
                        text = subTitle,
                        style = Typography.bodyMedium,
                        color = Grey200,
                    )
                }
            }
        }
    }
}
