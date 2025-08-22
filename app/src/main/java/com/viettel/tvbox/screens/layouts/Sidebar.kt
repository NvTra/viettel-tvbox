package com.viettel.tvbox.screens.layouts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.viettel.tvbox.BuildConfig
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.Grey
import com.viettel.tvbox.theme.PinkPrimary
import com.viettel.tvbox.theme.SidebarSelect

enum class SidebarDestination(
    val icon: Int,
    val route: String,
) {
    HOME(R.drawable.ic_home, "home_screen"), SEARCH(
        R.drawable.ic_search, "search_screen"
    ),
    CATEGORY(R.drawable.ic_category, "category_screen"), MY_LIST(
        R.drawable.ic_heart, "favorites_screen"
    ),
    PROMOTION(R.drawable.ic_new, "new_screen"),
}

@Composable
fun Sidebar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val context = LocalContext.current
    val userInformation = remember { UserPreferences.getInstance(context).getUserInformation() }
    val IMG_URL = BuildConfig.IMAGE_URL
    Log.d("Sidebar", "userInformation is null: ${userInformation == null}")
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .width(80.dp)
            .background(Color.Black),
        containerColor = Grey
    ) {
        GapH12()
        var isBnItemFocused by rememberSaveable { mutableStateOf(false) }

        NavigationRailItem(
            colors = NavigationRailItemDefaults.colors(
                indicatorColor = Color.Transparent
            ),
            selected = false,
            onClick = {
                UserPreferences.getInstance(context).clearAuth()
                navController.navigate("login_screen") {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = {
                if (userInformation != null && userInformation.avatar?.isNotEmpty() == true) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(SidebarSelect),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = userInformation.avatar.let { "$IMG_URL$it" },
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.ic_user)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(SidebarSelect),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            error = painterResource(R.drawable.ic_user)
                        )
                    }
                }
            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(26.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (isBnItemFocused) SidebarSelect else Color.Transparent
                )
                .onFocusChanged { focusState ->
                    isBnItemFocused = focusState.isFocused
                }
                .focusable(),
            alwaysShowLabel = false)
        GapH12()
        SidebarDestination.entries.forEachIndexed { index, destination ->
            var isItemFocused by rememberSaveable { mutableStateOf(false) }
            NavigationRailItem(
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    unselectedIconColor = Color.White,
                    selectedTextColor = PinkPrimary,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color.Transparent
                ),
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (selectedIndex == index) PinkPrimary else Color.White
                    )
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(26.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        when {
                            selectedIndex == index -> SidebarSelect
                            isItemFocused -> SidebarSelect.copy(alpha = 0.5f)
                            else -> Color.Transparent
                        }
                    )
                    .onFocusChanged { focusState ->
                        isItemFocused = focusState.isFocused
                    }
                    .focusable(),
                alwaysShowLabel = false)
        }
    }
}
