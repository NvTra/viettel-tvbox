package com.viettel.tvbox.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VietteltvTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isInDarkTheme) {
        darkColorScheme(
            primary = ViettelPrimaryColor,
            secondary = Grey300,
            tertiary = ViettelPrimaryColor
        )
    } else {
        lightColorScheme(
            primary = ViettelPrimaryColor,
            secondary = Grey300,
            tertiary = ViettelPrimaryColor
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}