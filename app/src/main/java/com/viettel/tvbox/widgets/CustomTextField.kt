package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viettel.tvbox.theme.GapW8
import com.viettel.tvbox.theme.Grey50
import com.viettel.tvbox.theme.PinkSecondary
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.theme.Typography

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Painter,
    trailingIcon: Painter? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    backgroundColor: Color = Color.Transparent,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    var isFocus by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .height(25.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isFocus) PinkSecondary else Grey50,
                shape = RoundedCornerShape(50.dp)
            )
            .background(backgroundColor, RoundedCornerShape(50.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                tint = WhiteColor,
                modifier = Modifier.size(8.dp)
            )
            GapW8()
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = Typography.bodySmall.copy(color = WhiteColor, fontSize = 8.sp),
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (value.isEmpty()) {
                            Text(
                                placeholder, style = Typography.bodySmall.copy(
                                    color = WhiteColor, fontSize = 8.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState -> isFocus = focusState.isFocused })
            if (trailingIcon != null) {
                GapW8()
                Icon(
                    painter = trailingIcon,
                    contentDescription = null,
                    tint = WhiteColor,
                    modifier = Modifier.size(8.dp).let {
                        if (onTrailingIconClick != null) {
                            it.clickable { onTrailingIconClick() }
                        } else it
                    }
                )
            }
        }
    }
}