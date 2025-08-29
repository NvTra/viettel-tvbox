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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.viettel.tvbox.theme.SidebarSelect
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.theme.VietelSecondary
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl

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
                color = if (isFocus) VietelSecondary else Color.Transparent,
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
                    fontSize = 12.sp
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
            .onFocusChanged { focusState -> isFocus = focusState.isFocused },
        colors = CardDefaults.cardColors(VietelPrimaryColor),
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
            .onFocusChanged { focusState -> isFocus = focusState.isFocused },
        colors = CardDefaults.cardColors(VietelPrimaryColor),
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