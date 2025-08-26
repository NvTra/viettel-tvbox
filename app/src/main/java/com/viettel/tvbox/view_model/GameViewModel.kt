package com.viettel.tvbox.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.models.GameDetail
import com.viettel.tvbox.models.GameRelation
import com.viettel.tvbox.models.GeneralGame
import com.viettel.tvbox.models.LikeGame
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class GameViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSmartLoading by mutableStateOf(false)
    var smartError by mutableStateOf<String?>(null)
    var gameDetail by mutableStateOf<GameDetail?>(null)
    var gameSearchHistory by mutableStateOf<List<GameRelation>>(emptyList())
    var gameSmartSearchResults by mutableStateOf<List<GameRelation>>(emptyList())

    var generalGame by mutableStateOf<GeneralGame?>(null)

    private val gameService = RetrofitInstance.gameService

    fun getGameDetail(id: String) {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = gameService.getGameDetail(id).awaitResponse()
                if (response.isSuccessful) {
                    gameDetail = response.body()
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

    fun getGameSearchHistory(type: String) {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = gameService.searchGames(type).awaitResponse()
                if (response.isSuccessful) {
                    gameSearchHistory = response.body() ?: emptyList()
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

    fun getGameSmartSearchResults(
        page: Int? = 0,
        pageSize: Int? = Int.MAX_VALUE,
        textSearch: String,
        allGame: String? = "ALL",
        type: String
    ) {
        isSmartLoading = true
        smartError = null
        viewModelScope.launch {
            try {
                val response =
                    gameService.smartSearchGames(page, pageSize, textSearch, allGame, type)
                        .awaitResponse()
                if (response.isSuccessful) {
                    gameSmartSearchResults = response.body()?.data ?: emptyList()
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

    fun getGeneralGame(gameId: String?) {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = gameService.generalGame(gameId).awaitResponse()
                if (response.isSuccessful) {
                    generalGame = response.body()

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

    fun likeGame(likeGame: LikeGame) {
        viewModelScope.launch {
            try {
                val response = gameService.likeGame(likeGame).awaitResponse()
                if (response.isSuccessful) {
                    getGeneralGame(likeGame.gameId)
                }
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

}