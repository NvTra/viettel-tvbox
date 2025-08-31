package com.viettel.tvbox.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.viettel.tvbox.screens.auths.LoginScreen
import com.viettel.tvbox.screens.category.CategoryDetailScreen
import com.viettel.tvbox.screens.category.CategoryScreen
import com.viettel.tvbox.screens.home.AllGameScreen
import com.viettel.tvbox.screens.home.GameDetail
import com.viettel.tvbox.screens.home.GameHomeScreen
import com.viettel.tvbox.screens.layouts.SidebarDestination
import com.viettel.tvbox.screens.my_list.MyListScreen
import com.viettel.tvbox.screens.promotion.PromotionDetailScreen
import com.viettel.tvbox.screens.promotion.PromotionScreen
import com.viettel.tvbox.screens.search.SearchScreen
import com.viettel.tvbox.theme.BlackColor
import com.viettel.tvbox.widgets.PlayBlacknutWebViewScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier.background(color = BlackColor)
    ) {

        // Main navigation with sidebar
        navigation(
            startDestination = SidebarDestination.HOME.route,
            route = "main"
        ) {
            SidebarDestination.entries.forEach { destination ->
                composable(destination.route) {
                    Row(modifier = Modifier.fillMaxHeight()) {
                        com.viettel.tvbox.screens.layouts.Sidebar(
                            navController = navController,
                            modifier = Modifier.width(40.dp)
                        )
                        when (destination) {
                            SidebarDestination.HOME -> GameHomeScreen("CloudGameTV", navController)
                            SidebarDestination.SEARCH -> SearchScreen("Tìm Kiếm", navController)
                            SidebarDestination.CATEGORY -> CategoryScreen("Thể loại", navController)
                            SidebarDestination.MY_LIST -> MyListScreen(
                                "Danh sách của tôi",
                                navController
                            )

                            SidebarDestination.PROMOTION -> PromotionScreen(
                                "Tin tức",
                                navController
                            )

                            SidebarDestination.MY_ACCOUNT -> com.viettel.tvbox.screens.my_account.MyAccountScreen(
                                navController,
                                onLogout
                            )
                        }
                    }
                }
            }
        }

        // Fullscreen screens (no sidebar)
        navigation(
            startDestination = "login_screen",
            route = "auth"
        ) {
            composable("login_screen") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
        }

        composable(route = "promotion_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            PromotionDetailScreen(id, navController = navController)
        }
        composable(route = "category_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            // category_detail vẫn có sidebar
            Row(modifier = Modifier.fillMaxHeight()) {
                com.viettel.tvbox.screens.layouts.Sidebar(
                    navController = navController,
                    modifier = Modifier.width(40.dp)
                )
                CategoryDetailScreen(id, navController = navController)
            }
        }
        composable(route = "game_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            GameDetail(id, navController = navController)
        }
        composable(route = "all_game_by_title/{id}/{title}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            Row(modifier = Modifier.fillMaxHeight()) {
                com.viettel.tvbox.screens.layouts.Sidebar(
                    navController = navController,
                    modifier = Modifier.width(40.dp)
                )
                AllGameScreen(id, title, navController = navController)
            }
        }
        composable(
            route = "play_blacknut_webview/{encodedUrl}",
            arguments = listOf(navArgument("encodedUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("encodedUrl") ?: ""
            // Decode the URL if needed
            val url = java.net.URLDecoder.decode(
                encodedUrl,
                java.nio.charset.StandardCharsets.UTF_8.toString()
            )
            PlayBlacknutWebViewScreen(url = url)
        }
    }
}