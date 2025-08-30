package com.viettel.tvbox.screens.promotion

import LoadingIndicator
import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.GapH16
import com.viettel.tvbox.theme.Grey400
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.view_model.PromotionViewModel
import com.viettel.tvbox.widgets.PromotionCard
import com.viettel.tvbox.widgets.PromotionFontSize
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PromotionDetailScreen(id: String, navController: NavController) {
    val viewModel: PromotionViewModel = viewModel()
    viewModel.promotions
    viewModel.isLoading
    viewModel.error

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    LocalFocusManager.current
    val lazyRowFocusRequester = remember { FocusRequester() }
    val density = LocalDensity.current
    var lazyRowY by remember { mutableStateOf(0f) }
    var lazyRowFocused by remember { mutableStateOf(false) }
    with(density) { 130.dp.toPx() }

    LaunchedEffect(scrollState.maxValue) {
        snapshotFlow { scrollState.value }
            .collect { scrollY ->
                if (!lazyRowFocused && lazyRowY > 0f && scrollY + scrollState.maxValue * 0.5 > lazyRowY) {
                    lazyRowFocusRequester.requestFocus()
                    lazyRowFocused = true
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.getPromotionById(id)
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
//                .padding(16.dp)
                .verticalScroll(scrollState)
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.key) {
                            Key.DirectionDown -> {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(scrollState.value + 80)
                                }
                                true
                            }

                            Key.DirectionUp -> {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(
                                        (scrollState.value - 80).coerceAtLeast(
                                            0
                                        )
                                    )
                                }
                                true
                            }

                            Key.PageDown -> {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(
                                        (scrollState.value + 400).coerceAtMost(
                                            scrollState.maxValue
                                        )
                                    )
                                }
                                true
                            }

                            Key.PageUp -> {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(
                                        (scrollState.value - 400).coerceAtLeast(
                                            0
                                        )
                                    )
                                }
                                true
                            }

                            else -> false
                        }
                    } else false
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                viewModel.isLoading -> {
                    LoadingIndicator()
                }

                viewModel.error != null -> {
                    Text(
                        text = "Error: ${viewModel.error}",
                        color = WhiteColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                viewModel.promotion != null -> {
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        GapH16()
                        Text(
                            text = viewModel.promotion?.promotionDetailUser?.title.takeUnless { it.isNullOrBlank() }
                                ?: "No Title",
                            style = Typography.headlineLarge,
                            color = WhiteColor,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        // Date row
                        val announceDate =
                            viewModel.promotion?.promotionDetailUser?.announceDate ?: ""
                        if (announceDate.isNotBlank()) {
                            val formattedDate =
                                com.viettel.tvbox.utils.DateUtils.formatDate(announceDate)
                            Row(modifier = Modifier.align(Alignment.Start)) {
                                Icon(
                                    painterResource(id = R.drawable.ic_calendar),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 8.dp, top = 4.dp, end = 4.dp)
                                        .size(8.dp),
                                    tint = Grey400,
                                )
                                Text(
                                    text = formattedDate,
                                    color = Grey400,
                                    style = Typography.labelSmall,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                                )
                            }
                        }
                        // HTML content
                        val rawHtmlContent = viewModel.promotion?.promotionDetailUser?.content ?: ""
                        val darkCss = """
                        <style>
                            body { background: #000000 !important; color: #ffffff !important; }
                            p, h1, h2, h3, h4, h5, h6, span, div, strong, em {
                                color: #f1f1f1 !important;
                            }
                            img { max-width: 100%; height: auto; }
                            a { color: #8ab4f8 !important; }
                        </style>
                    """.trimIndent()
                        val htmlContent =
                            "<html><head>$darkCss</head><body>$rawHtmlContent</body></html>"
                        if (rawHtmlContent.isBlank()) {
                            Text(
                                text = "",
                            )
                        } else {
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        true.also { settings.javaScriptEnabled = true }
                                        settings.loadWithOverviewMode = true
                                        settings.useWideViewPort = true
                                        settings.domStorageEnabled = true
                                        isVerticalScrollBarEnabled = false
                                        isHorizontalScrollBarEnabled = false
                                        scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
                                        loadDataWithBaseURL(
                                            null, htmlContent, "text/html", "utf-8", null
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                    // list promotion relation
                    val relatedPromotions =
                        viewModel.promotion?.promotionRelationUsers?.take(3) ?: emptyList()
                    if (relatedPromotions.isNotEmpty()) {
                        Text(
                            text = "Tin tức liên quan",
                            color = WhiteColor,
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp, bottom = 12.dp, start = 10.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                        ) {
                            LazyRow(
                                modifier = Modifier
                                    .height(150.dp)
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                                    .focusRequester(lazyRowFocusRequester)
                                    .onFocusChanged { state ->
                                        lazyRowFocused = state.isFocused
                                    }
                                    .onGloballyPositioned { coordinates ->
                                        lazyRowY = coordinates.positionInWindow().y
                                    },
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(
                                    horizontal = 10.dp
                                )
                            ) {
                                items(relatedPromotions) { promotion ->
                                    PromotionCard(
                                        id = promotion.id,
                                        title = promotion.title,
                                        description = promotion.description,
                                        imageUrl = promotion.image,
                                        fontSize = PromotionFontSize(
                                            titleFontSize = 8.sp,
                                            descriptionFontSize = 7.sp
                                        ),
                                        navController = navController
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}