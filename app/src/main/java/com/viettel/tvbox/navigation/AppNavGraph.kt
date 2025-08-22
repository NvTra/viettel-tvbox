package com.viettel.tvbox.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.viettel.tvbox.screens.auths.LoginScreen
import com.viettel.tvbox.screens.category.CategoryScreen
import com.viettel.tvbox.screens.home.AllGameScreen
import com.viettel.tvbox.screens.layouts.SidebarDestination
import com.viettel.tvbox.screens.my_list.MyListScreen
import com.viettel.tvbox.screens.promotion.PromotionDetailScreen
import com.viettel.tvbox.screens.promotion.PromotionScreen
import com.viettel.tvbox.screens.search.SearchScreen
import com.viettel.tvbox.theme.BlackColor

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
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
            composable(SidebarDestination.HOME.route) { AllGameScreen("CloudGame Tv") }
            composable(SidebarDestination.SEARCH.route) { SearchScreen("Tìm Kiếm") }
            composable(SidebarDestination.CATEGORY.route) { CategoryScreen("Thể loại") }
            composable(SidebarDestination.MY_LIST.route) { MyListScreen("Danh sách của tôi") }
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
            composable("login_screen") { LoginScreen(navController as () -> Unit) }
        }

        composable(route = "promotion_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            PromotionDetailScreen(id, navController = navController)
        }
    }
}