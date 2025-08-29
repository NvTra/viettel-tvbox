package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.viettel.tvbox.theme.BlackColor
import com.viettel.tvbox.theme.Grey800
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.WhiteColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String = "",
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            Box {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                textAlign = TextAlign.Center,
                                color = WhiteColor,
                                style = Typography.titleMedium,
                            )
                        }
                    },
                    colors = topAppBarColors(
                        containerColor = BlackColor,
                        titleContentColor = BlackColor
                    ),
                    modifier = Modifier.requiredHeight(42.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.BottomCenter)
                        .background(Grey800)
                )
            }
        },
        containerColor = BlackColor
    ) { innerPadding ->
        content(innerPadding)
    }
}