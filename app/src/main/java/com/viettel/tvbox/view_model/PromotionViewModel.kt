package com.viettel.tvbox.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.auth.Promotions
import com.viettel.tvbox.models.auth.PromotionsDetail
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class PromotionViewModel : ViewModel() {
    var promotion by mutableStateOf<PromotionsDetail?>(null)

    var promotions by mutableStateOf<Promotions?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    private val service = RetrofitInstance.promotionService

    fun fetchPromotions(page: Int = 0, pageSize: Int = 10, type: String = "ALL") {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = service.getData(page, pageSize, type).awaitResponse()
                if (response.isSuccessful) {
                    promotions = response.body()
                } else {
                    error = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun getPromotionById(id: String) {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = service.getDetailNoAuth(id).awaitResponse()
                if (response.isSuccessful) {
                    promotion = response.body()
                } else {
                    error = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun clearData() {
        promotions = null
        isLoading = false
        error = null
    }
}