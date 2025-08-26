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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelSecondary
import com.viettel.tvbox.utils.getImageUrl

@Composable
fun GameCard(
    id: String,
    title: String,
    imageUrl: String,
    navController: NavController
) {
    var isFocus by remember { mutableStateOf(false) }
    Card(
        onClick = {
            navController.navigate("game_detail/$id")
        },
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer(
                scaleX = if (isFocus) 1.06f else 1f,
                scaleY = if (isFocus) 1.06f else 1f
            )
            .onFocusChanged { focusState -> isFocus = focusState.isFocused }
            .border(
                width = if (isFocus) 2.dp else 0.dp,
                color = if (isFocus) VietelSecondary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(getImageUrl(imageUrl))
                        .crossfade(true)
                        .size(800, 600)
                        .allowHardware(false)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                error = painterResource(R.drawable.ic_close)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = Typography.titleSmall,
                    color = Color.White,
                    maxLines = 2
                )

            }
        }
    }

}