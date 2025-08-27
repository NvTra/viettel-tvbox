package com.viettel.tvbox.screens.my_account

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viettel.tvbox.screens.my_account.widget.AccessHistory
import com.viettel.tvbox.screens.my_account.widget.AccountDetail
import com.viettel.tvbox.screens.my_account.widget.ChangePassword
import com.viettel.tvbox.screens.my_account.widget.GameHistory
import com.viettel.tvbox.screens.my_account.widget.PayHistory

@Composable
fun MyAccountScreen(parentNavController: NavController, onLogout: () -> Unit) {
    val navController = rememberNavController()
    var selectedRoute by remember { mutableStateOf(MyAccountDestination.AccountDetail.route) }
    Row {
        MyAccountSideBar(
            onLogout = onLogout,
            navController = navController,
            selectedRoute = selectedRoute,
            onItemSelected = { route ->
                selectedRoute = route
                navController.navigate(route) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = navController,
                startDestination = MyAccountDestination.AccountDetail.route
            ) {
                composable(MyAccountDestination.AccountDetail.route) { AccountDetail() }
                composable(MyAccountDestination.ChangePassword.route) { ChangePassword() }
                composable(MyAccountDestination.GameHistory.route) { GameHistory() }
                composable(MyAccountDestination.AccessHistory.route) { AccessHistory() }
                composable(MyAccountDestination.PayHistory.route) { PayHistory() }
            }
        }
    }

}