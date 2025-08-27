package com.viettel.tvbox.screens.layouts

import UserPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.viettel.tvbox.R
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.Grey
import com.viettel.tvbox.theme.SidebarSelect
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.utils.getImageUrl

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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val selectedIndex =
        SidebarDestination.entries.indexOfFirst { currentRoute?.startsWith(it.route) == true }
    val context = LocalContext.current
    val userInformation = remember { UserPreferences.getInstance(context).getUserInformation() }
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
                navController.navigate("my_account")
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
                            model = getImageUrl(userInformation.avatar),
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
                },
            alwaysShowLabel = false
        )
        GapH12()
        SidebarDestination.entries.forEachIndexed { index, destination ->
            var isItemFocused by rememberSaveable { mutableStateOf(false) }
            NavigationRailItem(
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = VietelPrimaryColor,
                    unselectedIconColor = Color.White,
                    selectedTextColor = VietelPrimaryColor,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color.Transparent
                ),
                selected = selectedIndex == index,
                onClick = {
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
                        tint = if (selectedIndex == index) VietelPrimaryColor else Color.White
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
                    },
                alwaysShowLabel = false
            )
        }
    }
}
