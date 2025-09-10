package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.viettel.tvbox.theme.GapH8
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.utils.getImageUrl

data class PromotionFontSize(
    val titleFontSize: TextUnit, val descriptionFontSize: TextUnit
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PromotionCard(
    id: String,
    title: String,
    description: String,
    imageUrl: String,
    fontSize: PromotionFontSize? = null,
    navController: NavController
) {
    var isFocus by remember { mutableStateOf(false) }

    Card(
        onClick = {
            navController.navigate("promotion_detail/$id")
        },
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer(
                scaleX = if (isFocus) 1.03f else 1f,
                scaleY = if (isFocus) 1.03f else 1f,
                shadowElevation = if (isFocus) 4f else 0f
            )
            .zIndex(if (isFocus) 1f else 0f)
            .background(Color.Black.copy(alpha = 0.5f))
            .onFocusChanged { focusState -> isFocus = focusState.isFocused }
            .border(
                width = if (isFocus) 2.dp else 0.dp,
                color = if (isFocus) ViettelPrimaryColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = getImageUrl(imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = if (isFocus) 0.4f else 0.5f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = if (fontSize?.titleFontSize != null)
                        Typography.titleSmall.copy(fontSize = fontSize.titleFontSize) else Typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isFocus) ViettelPrimaryColor else Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                GapH8()

                androidx.compose.material3.Text(
                    text = description,
                    style = if (fontSize?.descriptionFontSize != null) Typography.labelSmall.copy(
                        fontSize = fontSize.descriptionFontSize
                    ) else Typography.labelSmall,
                    color = Color.White.copy(alpha = if (isFocus) 0.9f else 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}