package com.viettel.tvbox.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.GameRelation
import com.viettel.tvbox.models.HistoryGameDetail
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class UserViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)

    var error by mutableStateOf<String?>(null)

    var gamePlayHistory by mutableStateOf<List<HistoryGameDetail>?>(null)

    var favoriteGame by mutableStateOf<List<GameRelation>?>(null)
    private val userService = RetrofitInstance.userService

    fun getGamePlayHistory(
        page: Int = 0,
        pageSize: Int = Int.MAX_VALUE,
        startDate: String = "",
        endDate: String = "",
        forAge: String = "ALL"
    ) {
        isLoading = true
        error = null

        viewModelScope.launch {
            try {
                val response = userService.getGamePlayHistory(
                    page, pageSize,
                    "desc",
                    "created_date", startDate, endDate, forAge
                ).awaitResponse()

                if (response.isSuccessful) {
                    gamePlayHistory = response.body()?.data
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

    fun getFavoriteGame() {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = userService.getFavoriteGame().awaitResponse()

                if (response.isSuccessful) {
                    favoriteGame = response.body()?.listGame
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