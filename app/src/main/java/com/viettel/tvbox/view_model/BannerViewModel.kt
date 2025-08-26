package com.viettel.tvbox.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.Banner
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class BannerViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)

    var error by mutableStateOf<String?>(null)

    var banner by mutableStateOf<List<Banner>?>(null)

    var bannerVideo by mutableStateOf<List<Banner>?>(null)

    var bannerImage by mutableStateOf<List<Banner>?>(null)

    private val homeService = RetrofitInstance.homeService

    fun getAllBanner() {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = homeService.getAllBanner().awaitResponse()
                if (response.isSuccessful) {
                    val banners = response.body()
                    banner = banners
                    bannerVideo = banners?.filter { it.type == "homepage" }
                    bannerImage = banners?.filter { it.type == "other" }
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
}