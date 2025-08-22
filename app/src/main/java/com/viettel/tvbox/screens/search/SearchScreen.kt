package com.viettel.tvbox.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.viettel.tvbox.BuildConfig
import com.viettel.tvbox.widgets.CustomScaffold

@Composable
fun SearchScreen(label: String) {
    CustomScaffold(label) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(text = "API_URL: ${BuildConfig.API_BASE_URL}")
            Text(text = "IMAGE_URL: ${BuildConfig.IMAGE_URL}")
            Text(text = "VIDEO_URL: ${BuildConfig.VIDEO_URL}")
            Text(text = "BLACKNUT_URL: ${BuildConfig.BLACKNUT_URL}")
            Text(text = "BLACKNUT_IMAGE_URL: ${BuildConfig.BLACKNUT_IMAGE_URL}")

        }
    }
}