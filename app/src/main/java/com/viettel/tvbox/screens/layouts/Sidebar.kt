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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.viettel.tvbox.theme.Grey
import com.viettel.tvbox.theme.SidebarSelect
import com.viettel.tvbox.theme.ViettelPrimaryColor
import com.viettel.tvbox.utils.getImageUrl
import kotlinx.coroutines.delay

enum class SidebarDestination(
    val icon: Int?,
    val route: String,
    val isAccount: Boolean = false
) {
    MY_ACCOUNT(R.drawable.ic_user2, "my_account", true),
    HOME(R.drawable.ic_home, "home_screen"),
    SEARCH(R.drawable.ic_search, "search_screen"),
    CATEGORY(R.drawable.ic_category, "category_screen"),
    MY_LIST(R.drawable.ic_heart, "favorites_screen"),
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

    val focusRequesters = remember {
        SidebarDestination.entries.map { FocusRequester() }
    }
    var lastClickedIndex by rememberSaveable { mutableStateOf(-1) }
    var isInitialFocus by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(selectedIndex) {
        when {
            isInitialFocus -> {
                val targetIndex =
                    if (selectedIndex >= 0) selectedIndex else 1
                delay(100)
                try {
                    focusRequesters[targetIndex].requestFocus()
                } catch (e: Exception) {
                }
                isInitialFocus = false
            }

            lastClickedIndex >= 0 -> {
                delay(100)
                try {
                    focusRequesters[lastClickedIndex].requestFocus()
                } catch (e: Exception) {
                }
                lastClickedIndex = -1
            }
        }
    }
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .width(80.dp)
            .background(Color.Black),
        containerColor = Grey
    ) {
        SidebarDestination.entries.forEachIndexed { index, destination ->
            var isItemFocused by rememberSaveable { mutableStateOf(false) }
            if (destination.isAccount) {
                NavigationRailItem(
                    colors = NavigationRailItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    ),
                    selected = selectedIndex == index,
                    onClick = {
                        lastClickedIndex = index
                        navController.navigate(destination.route)
                    },
                    icon = {
                        if (userInformation != null && userInformation.avatar?.isNotEmpty() == true) {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
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
                                    error = painterResource(R.drawable.ic_user2)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(25.dp)
                                    .clip(CircleShape)
                                    .background(SidebarSelect),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_user2),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isItemFocused || selectedIndex == index) SidebarSelect else Color.Transparent
                        )
                        .focusRequester(focusRequesters[index])
                        .onFocusChanged { focusState ->
                            isItemFocused = focusState.isFocused
                        },
                    alwaysShowLabel = false
                )
            } else {
                NavigationRailItem(
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = ViettelPrimaryColor,
                        unselectedIconColor = Color.White,
                        selectedTextColor = ViettelPrimaryColor,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color.Transparent
                    ),
                    selected = selectedIndex == index,
                    onClick = {
                        lastClickedIndex = index
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = destination.icon!!),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = if (selectedIndex == index) ViettelPrimaryColor else Color.White
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
                        .focusRequester(focusRequesters[index])
                        .onFocusChanged { focusState ->
                            isItemFocused = focusState.isFocused
                        },
                    alwaysShowLabel = false
                )
            }
        }
    }
}
