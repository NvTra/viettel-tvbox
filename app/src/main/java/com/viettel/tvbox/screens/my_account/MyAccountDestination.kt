package com.viettel.tvbox.screens.my_account

sealed class MyAccountDestination(val route: String) {
    object AccountDetail : MyAccountDestination("account_detail")
    object ChangePassword : MyAccountDestination("change_password")
    object GameHistory : MyAccountDestination("game_history")
    object AccessHistory : MyAccountDestination("access_history")
    object PayHistory : MyAccountDestination("pay_history")

    companion object {
        val all = listOf(AccountDetail, ChangePassword, GameHistory, AccessHistory, PayHistory)
    }
}

