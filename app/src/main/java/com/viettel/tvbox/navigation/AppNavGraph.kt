package com.viettel.tvbox.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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

        navigation(
            startDestination = SidebarDestination.HOME.route,
            route = "main"
        ) {
            composable(SidebarDestination.HOME.route) {
                GameHomeScreen(
                    "CloudGame Tv",
                    navController
                )
            }
            composable(SidebarDestination.SEARCH.route) { SearchScreen("Tìm Kiếm", navController) }
            composable(SidebarDestination.CATEGORY.route) {
                CategoryScreen(
                    "Thể loại",
                    navController
                )
            }
            composable(SidebarDestination.MY_LIST.route) {
                MyListScreen(
                    "Danh sách của tôi",
                    navController
                )
            }
            composable(SidebarDestination.PROMOTION.route) {
                PromotionScreen(
                    "Tin tức",
                    navController
                )
            }
        }

        navigation(
            startDestination = "login_screen",
            route = "auth"
        ) {
            composable("login_screen") {
                LoginScreen(onLoginSuccess = {
                    navController.navigate("home_screen") {
                        popUpTo("login_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
        }

        // Replace the nested navigation for my_account with a single composable
        composable("my_account") {
            com.viettel.tvbox.screens.my_account.MyAccountScreen(navController, onLogout)
        }

        composable(route = "promotion_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            PromotionDetailScreen(id, navController = navController)
        }
        composable(route = "category_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CategoryDetailScreen(id, navController = navController)
        }

        composable(route = "game_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            GameDetail(id, navController = navController)
        }

        composable(route = "all_game_by_title/{id}/{title}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            AllGameScreen(id, title, navController = navController)
        }
    }
}