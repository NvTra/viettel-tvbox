package com.viettel.tvbox.view_model

import UserPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viettel.tvbox.BuildConfig
import com.viettel.tvbox.models.CheckPlayResponse
import com.viettel.tvbox.models.GameDetail
import com.viettel.tvbox.models.GameRelation
import com.viettel.tvbox.models.GeneralGame
import com.viettel.tvbox.models.LikeGame
import com.viettel.tvbox.models.PlayToken
import com.viettel.tvbox.services.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class GameViewModelFactory(
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class GameViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isSmartLoading by mutableStateOf(false)
    var smartError by mutableStateOf<String?>(null)
    var gameDetail by mutableStateOf<GameDetail?>(null)
    var gameSearchHistory by mutableStateOf<List<GameRelation>>(emptyList())
    var gameSmartSearchResults by mutableStateOf<List<GameRelation>>(emptyList())

    var generalGame by mutableStateOf<GeneralGame?>(null)
    var isLiked by mutableStateOf<Boolean?>(null)

    var checkPlayResponse by mutableStateOf<CheckPlayResponse?>(null)
    var linkGame by mutableStateOf<String?>(null)
    var statusUserForPlaying by mutableStateOf<Int?>(null)

    // Use PlayToken from models instead of BlacknutPlayInfo
    var blacknutPlayToken by mutableStateOf<PlayToken?>(null)
    var blacknutGameID: String? = null
    var blacknutPartnerGameId: String? = null

    private val gameService = RetrofitInstance.gameService

    fun getGameDetail(id: String, type: String) {
        isLoading = true
        error = null
        viewModelScope.launch {
            try {
                val response = if (userPreferences.isLogin()) {
                    gameService.getGameDetailAuth(id, type).awaitResponse()
                } else {
                    gameService.getGameDetail(id).awaitResponse()
                }
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
                if (textSearch == "") {
                    gameSmartSearchResults = emptyList()
                    isSmartLoading = false
                    return@launch
                }
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
                isSmartLoading = false
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

                    // Set isLiked based on userInfoInteracts
                    val userId = generalGame?.userInfoInteracts?.firstOrNull()?.userId
                    val liked = generalGame?.userInfoInteracts?.any { it.userId == userId } == true
                    isLiked = liked
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
        isLiked = likeGame.likeGame
        viewModelScope.launch {
            try {
                val response = gameService.likeGame(likeGame).awaitResponse()
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    fun checkPlay(gameId: String) {
        isLoading = true
        error = null
        viewModelScope.launch {
            var link: String? = null
            var statusUser: Int? = 0
            try {
                val response = gameService.checkPlay(gameId).awaitResponse()
                if (response.isSuccessful) {
                    val result = response.body()
                    checkPlayResponse = result
                    link = null
                    statusUser = 0
                    if (result?.status == true) {
                        // Allowed to play
                        if (gameDetail?.partnerGameId == null) {
                            if (result.upload == "LINK") {
                                link = result.linkGame
                            } else if (result.upload == "SOURCE") {
                                link = BuildConfig.BLACKNUT_URL + "/" + (result.sourceGame ?: "")
                            }
                        }
                        statusUser = 0
                    } else {
                        val res =
                            gameService.getValidSubs(gameId).awaitResponse()
                        val validSubs = res.body() ?: emptyList<Any>()
                        statusUser = if (validSubs.isNotEmpty()) 3 else 1
                    }
                } else {
                    error = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                linkGame = link
                statusUserForPlaying = statusUser
                println("linkGame: $linkGame, statusUserForPlaying: $statusUserForPlaying")
                isLoading = false
            }
        }
    }

    fun clearGameSearchHistory() {
        gameSearchHistory = emptyList()
    }

    fun playBlacknut(blacknutGameID: String, partnerGameId: String?) {
        viewModelScope.launch {
            try {
                val response = gameService.getGamePlayToken().awaitResponse()
                if (response.isSuccessful) {
                    val token = response.body()
                    blacknutPlayToken = token
                    this@GameViewModel.blacknutGameID = blacknutGameID
                    this@GameViewModel.blacknutPartnerGameId = partnerGameId
                } else {
                    error = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                error = e.message
            }
        }
    }
}