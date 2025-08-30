package com.viettel.tvbox.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authService = RetrofitInstance.authService
    fun logout() {
        viewModelScope.launch {
            try {
                authService.logout()
            } catch (e: Exception) {

            }

        }
    }
}