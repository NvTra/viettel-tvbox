package com.viettel.tvbox.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.Grey300

@Composable
fun DeviceIcon(
    devices: List<String>
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        devices.forEach { item ->
            DeviceIconItem(item)
        }
    }
}

@Composable
fun DeviceIconItem(device: String) {
    val color: Color = Grey300
    when (device) {
        "gamepad" -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_gamepad_simple),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(12.dp)
            )
        }

        "keyboard / mouse" -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_keyboard_and_mouse),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(12.dp)
            )
        }

        "touch screen" -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_touch_gesture),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(12.dp)
            )
        }

        "amazon remote control" -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_amazone_remote_control),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(12.dp)
            )
        }

        else -> {
            Icon(
                painter = painterResource(id = R.drawable.ic_game_controller),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(10.dp)
            )
        }
    }
}