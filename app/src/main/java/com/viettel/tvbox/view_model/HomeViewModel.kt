package com.viettel.tvbox.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.AllGameByTitle
import com.viettel.tvbox.models.ManagerHomeConfig
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class HomeViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)

    var error by mutableStateOf<String?>(null)

    var managerHomeConfig by mutableStateOf<List<ManagerHomeConfig>?>(null)
    var gameHot by mutableStateOf<ManagerHomeConfig?>(null)

    var genres by mutableStateOf<ManagerHomeConfig?>(null)

    var allGame by mutableStateOf<ManagerHomeConfig?>(null)

    var gamePlayed by mutableStateOf<ManagerHomeConfig?>(null)


    var allGameByTitle by mutableStateOf<List<AllGameByTitle>?>(null)
    private val homeService = RetrofitInstance.homeService

    fun getManagerHomeConfigData() {
        isLoading = true
        error = null

        viewModelScope.launch {
            try {
                val response = homeService.getManagerHomeConfig().awaitResponse()

                if (response.isSuccessful) {
                    val homeConfigs = response.body()
                    managerHomeConfig = homeConfigs
                    gameHot = homeConfigs?.get(1)
                    genres = homeConfigs?.get(0)
                    allGame = homeConfigs?.get(2)
                    gamePlayed = homeConfigs?.firstOrNull { it.criteria == "PLAYED" }
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

    fun getAllGameByTitle(
        page: Int? = 0,
        pageSize: Int? = Int.MAX_VALUE,
        forAge: String = "ALL",
        title: String
    ) {
        isLoading = true
        error = null

        viewModelScope.launch {
            try {
                val response =
                    homeService.getAllGameByTitle(page, pageSize, forAge, title).awaitResponse()

                if (response.isSuccessful) {
                    val allGames = response.body()
                    allGameByTitle = allGames?.data
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