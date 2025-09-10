package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl

@Composable
fun FeaturedCategoryCard(
    id: String,
    icon: String,
    title: String,
    backGround: Color = ViettelPrimaryColor,
    navController: NavController,
    image: Int,
    modifier: Modifier = Modifier
) {
    var isFocus by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = if (isFocus) 1.10f else 1f,
                scaleY = if (isFocus) 1.10f else 1f
            )
            .zIndex(if (isFocus) 1f else 0f)
            .border(
                width = if (isFocus) 2.dp else 0.dp,
                color = if (isFocus) ViettelPrimaryColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Card(
            onClick = {
                navController.navigate("category_detail/${id}")
            },
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { focusState -> isFocus = focusState.isFocused },
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
                    alignment = Alignment.Center
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = if (isFocus) 0.2f else 0.3f))
                )

                Text(
                    text = title,
                    style = Typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    color = if (isFocus) ViettelPrimaryColor else WhiteColor
                )
            }
        }
    }
}


@Composable
fun CategoryImageCard(
    id: String,
    icon: String,
    image: String,
    title: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isFocus by remember { mutableStateOf(false) }

    Card(
        onClick = {
            navController.navigate("category_detail/${id}")
        },
        modifier = modifier
            .graphicsLayer(
                scaleX = if (isFocus) 1.10f else 1f,
                scaleY = if (isFocus) 1.10f else 1f,
                shadowElevation = if (isFocus) 4f else 0f
            )
            .zIndex(if (isFocus) 1f else 0f)
            .border(
                width = if (isFocus) 2.dp else 0.dp,
                color = if (isFocus) ViettelPrimaryColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .onFocusChanged { focusState -> isFocus = focusState.isFocused },
        colors = CardDefaults.cardColors(ViettelPrimaryColor),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        ) {
            AsyncImage(
                model = getImageUrl(image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                alignment = Alignment.Center
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = if (isFocus) 0.2f else 0.3f))
            )

            Text(
                text = title,
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = if (isFocus) ViettelPrimaryColor else WhiteColor
            )
        }
    }
}