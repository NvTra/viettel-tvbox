package com.viettel.tvbox.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.LogUserHistory
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class HistoryViewModel : ViewModel() {
    private val homeService = RetrofitInstance.homeService

    fun saveHistory(body: LogUserHistory) {
        viewModelScope.launch {
            try {
                val response = homeService.saveHistorySearch(body).awaitResponse()
                if (!response.isSuccessful) {
                    println("Lưu lịch sử thất bại: ${response.code()}")
                } else {
                    println("Gọi API lưu lịch sử thành công")
                }
            } catch (e: Exception) {
                println("Lỗi khi gọi API lưu lịch sử: ${e.message}")
            }
        }
    }

    fun savePlayGame(body: LogUserHistory) {
        viewModelScope.launch {
            try {
                val response = homeService.savePlayGame(body).awaitResponse()
                if (!response.isSuccessful) {
                    println("Lưu lịch sử chơi game thất bại: ${response.code()}")
                } else {
                    println("Gọi API lưu lịch sử chơi game thành công")
                }
            } catch (e: Exception) {
                println("Lỗi khi gọi API lưu lịch sử chơi game: ${e.message}")
            }
        }
    }
}