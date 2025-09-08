package com.viettel.tvbox.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.viettel.tvbox.theme.SidebarSelect
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl

class ParallelogramShape(
    private val offsetFraction: Float = 0.1f,
    private val topLeftRadiusDp: Float = 1f,
    private val topRightRadiusDp: Float = 2f,
    private val bottomRightRadiusDp: Float = 1f,
    private val bottomLeftRadiusDp: Float = 2f
) : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: androidx.compose.ui.unit.Density
    ): Outline {
        val offset = size.width * offsetFraction
        val topLeft = androidx.compose.ui.geometry.Offset(offset / 2, 0f)
        val topRight = androidx.compose.ui.geometry.Offset(size.width, 0f)
        val bottomRight = androidx.compose.ui.geometry.Offset(size.width - offset / 2, size.height)
        val bottomLeft = androidx.compose.ui.geometry.Offset(0f, size.height)

        // Edge vectors
        val vTop = (topRight - topLeft)
        val vRight = (bottomRight - topRight)
        val vBottom = (bottomLeft - bottomRight)
        val vLeft = (topLeft - bottomLeft)

        // Edge lengths
        val topEdge = vTop.getDistance()
        val rightEdge = vRight.getDistance()
        val bottomEdge = vBottom.getDistance()
        val leftEdge = vLeft.getDistance()

        // Clamp radii
        val topLeftRadius =
            minOf(with(density) { topLeftRadiusDp.dp.toPx() }, topEdge / 2, leftEdge / 2)
        val topRightRadius =
            minOf(with(density) { topRightRadiusDp.dp.toPx() }, topEdge / 2, rightEdge / 2)
        val bottomRightRadius =
            minOf(with(density) { bottomRightRadiusDp.dp.toPx() }, bottomEdge / 2, rightEdge / 2)
        val bottomLeftRadius =
            minOf(with(density) { bottomLeftRadiusDp.dp.toPx() }, bottomEdge / 2, leftEdge / 2)

        val path = Path().apply {
            moveTo(topLeft.x + topLeftRadius, topLeft.y)
            lineTo(topRight.x - topRightRadius, topRight.y)
            arcTo(
                Rect(
                    topRight.x - 2 * topRightRadius,
                    topRight.y,
                    topRight.x,
                    topRight.y + 2 * topRightRadius
                ),
                270f,
                90f,
                false
            )
            lineTo(bottomRight.x, bottomRight.y - bottomRightRadius)
            arcTo(
                Rect(
                    bottomRight.x - 2 * bottomRightRadius,
                    bottomRight.y - 2 * bottomRightRadius,
                    bottomRight.x,
                    bottomRight.y
                ),
                0f,
                90f,
                false
            )
            lineTo(bottomLeft.x + bottomLeftRadius, bottomLeft.y)
            arcTo(
                Rect(
                    bottomLeft.x,
                    bottomLeft.y - 2 * bottomLeftRadius,
                    bottomLeft.x + 2 * bottomLeftRadius,
                    bottomLeft.y
                ),
                90f,
                90f,
                false
            )
            lineTo(topLeft.x, topLeft.y + topLeftRadius)
            arcTo(
                Rect(
                    topLeft.x,
                    topLeft.y,
                    topLeft.x + 2 * topLeftRadius,
                    topLeft.y + 2 * topLeftRadius
                ),
                180f,
                90f,
                false
            )
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun CategoryCard(
    id: String,
    icon: String,
    title: String,
    navController: NavController
) {
    var isFocus by remember { mutableStateOf(false) }
    Card(
        onClick = {
            navController.navigate("category_detail/${id}")
        },
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer(
                scaleX = if (isFocus) 1.06f else 1f,
                scaleY = if (isFocus) 1.06f else 1f
            )
            .border(
                width = if (isFocus) 2.dp else 0.dp,
                color = if (isFocus) ViettelPrimaryColor else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .onFocusChanged { focusState -> isFocus = focusState.isFocused },
        colors = CardDefaults.cardColors(SidebarSelect),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = getImageUrl(icon),
                contentDescription = null,
                modifier = Modifier
                    .width(40.dp),
            )
            Text(
                text = title,
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                ),
                color = WhiteColor
            )
        }
    }
}

@Composable
fun FeaturedCategoryCard(
    id: String,
    icon: String,
    title: String,
    backGround: Color = ViettelPrimaryColor,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isFocus by remember { mutableStateOf(false) }
    val shape = ParallelogramShape(0.4f)
    Box(
        modifier = modifier
            .zIndex(if (isFocus) 1f else 0f)
            .graphicsLayer(
                scaleX = if (isFocus) 1.15f else 1f,
                scaleY = if (isFocus) 1.15f else 1f
            )
            .then(
                if (isFocus) Modifier
                    .clip(shape)
                    .background(Color.Transparent)
                    .border(2.dp, ViettelPrimaryColor, shape)
                else Modifier
                    .clip(shape)
            )
    ) {
        Card(
            onClick = {
                navController.navigate("category_detail/${id}")
            },
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(backGround)
                .onFocusChanged { focusState -> isFocus = focusState.isFocused },
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = title,
                    style = Typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    ),
                    color = WhiteColor
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
                scaleX = if (isFocus) 1.05f else 1f,
                scaleY = if (isFocus) 1.05f else 1f
            )
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
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Text(
                text = title,
                style = Typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                color = WhiteColor
            )
        }
    }
}