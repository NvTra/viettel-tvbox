package com.viettel.tvbox.screens.my_account

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MyAccountScreen(navController: NavController) {
    Row { MyAccountSideBar(navController) }

}