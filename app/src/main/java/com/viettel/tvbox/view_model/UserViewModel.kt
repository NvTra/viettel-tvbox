package com.viettel.tvbox.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.AccessHistoryDetail
import com.viettel.tvbox.models.GameRelation
import com.viettel.tvbox.models.HistoryGameDetail
import com.viettel.tvbox.models.PayHistoryDetail
import com.viettel.tvbox.models.RestException
import com.viettel.tvbox.services.RetrofitInstance
import com.viettel.tvbox.widgets.ToastMessage
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class UserViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)

    var error by mutableStateOf<String?>(null)

    var gamePlayHistory by mutableStateOf<List<HistoryGameDetail>?>(null)

    var accessHistory by mutableStateOf<List<AccessHistoryDetail>?>(null)

    var payHistory by mutableStateOf<List<PayHistoryDetail>?>(null)
    var favoriteGame by mutableStateOf<List<GameRelation>?>(null)
    private val userService = RetrofitInstance.userService

    var changePasswordMessage by mutableStateOf<String?>(null)

    fun changePassword(
        body: Map<String, String>
    ) {
        isLoading = true
        error = null

        viewModelScope.launch {
            try {
                val response = userService.changePassword(body).awaitResponse()
                if (response.isSuccessful && response.body()?.responseCode == 6995) {
                    ToastMessage.success("Đổi mật khẩu thành công")
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody != null) {
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(errorBody, RestException::class.java)
                        when (errorResponse.code) {
                            9990 -> ToastMessage.error("Mật khẩu hiện tại không đúng")
                            9989 -> ToastMessage.error("Mật khẩu mới phải khác mật khẩu hiện tại")
                            else -> ToastMessage.error("Đổi mật khẩu thất bại")

                        }
                    } else {
                        ToastMessage.error("Đổi mật khẩu thất bại")
                    }
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

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
                    page, pageSize, "desc", "created_date", startDate, endDate, forAge
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

    fun getAccessHistory(
        page: Int = 0,
        pageSize: Int = Int.MAX_VALUE,
        startDate: Long = 0,
        endDate: Long = 0,
    ) {
        isLoading = true
        error = null

        viewModelScope.launch {
            try {
                val response = userService.getAccessHistory(
                    page, pageSize, startDate, endDate
                ).awaitResponse()

                if (response.isSuccessful) {
                    accessHistory = response.body()?.data
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

    fun getPayHistory(
        page: Int = 0,
        pageSize: Int = Int.MAX_VALUE,
        startDate: Long = 0,
        endDate: Long = 0,
    ) {
        isLoading = true
        error = null

        viewModelScope.launch {
            try {
                val response = userService.getPayHistory(
                    page, pageSize, "desc", "created_date", startDate, endDate
                ).awaitResponse()

                if (response.isSuccessful) {
                    payHistory = response.body()?.data
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