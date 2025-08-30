package com.viettel.tvbox.screens.home

import LoadingIndicator
import UserPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.viettel.tvbox.R
import com.viettel.tvbox.models.LikeGame
import com.viettel.tvbox.models.LogUserHistory
import com.viettel.tvbox.theme.BG_DB27777
import com.viettel.tvbox.theme.Black50
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.GapH16
import com.viettel.tvbox.theme.GapH2
import com.viettel.tvbox.theme.GapH4
import com.viettel.tvbox.theme.GapH8
import com.viettel.tvbox.theme.GapW4
import com.viettel.tvbox.theme.Grey300
import com.viettel.tvbox.theme.Grey400
import com.viettel.tvbox.theme.Grey600
import com.viettel.tvbox.theme.Grey700
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.theme.VietelSecondary
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.view_model.GameViewModel
import com.viettel.tvbox.view_model.GameViewModelFactory
import com.viettel.tvbox.view_model.HistoryViewModel
import com.viettel.tvbox.widgets.DeviceIcon
import com.viettel.tvbox.widgets.GameCard
import com.viettel.tvbox.widgets.ServiceInterruptionPopup
import com.viettel.tvbox.widgets.SigningPackagePopup
import com.viettel.tvbox.widgets.VideoBackground
import com.viettel.tvbox.widgets.VideoPopup

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GameDetail(id: String, navController: NavController) {
    val context = LocalContext.current
    val userPres = remember { UserPreferences.getInstance(context) }
    val viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(userPres))

    val historyModel: HistoryViewModel = viewModel()
    val gameDetail = viewModel.gameDetail
    val isLoading = viewModel.isLoading
    val error = viewModel.error
    val statusUserForPlaying = viewModel.statusUserForPlaying
    val isMuted = remember { mutableStateOf(false) }

    //popup show
    val showVideoPopup = remember { mutableStateOf(false) }
    var signinPackgerDialog by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var serviceInterrupDialog by remember { mutableStateOf(false) }


    var backFocus by remember { mutableStateOf(false) }

    val isLiked = viewModel.isLiked

    LaunchedEffect(Unit) {
        viewModel.getGameDetail(id = id, type = userPres.getUserInformation()?.forAge ?: "ADULT")
        viewModel.checkPlay(gameId = id)
        viewModel.getGeneralGame(gameId = id)
    }

    fun getPegi(age: Double): String {
        if (age < 12) return "00+"
        else if (age >= 12 && age < 16) return "12+"
        else if (age >= 16 && age < 18) return "16+"
        else if (age >= 18) return "18+"
        else return "00+"
    }

    fun likeGame() {
        val likeGame = LikeGame(
            gameId = id,
            likeGame = !(isLiked ?: false)
        )
        viewModel.likeGame(likeGame)
    }


    fun playGame() {
        when (statusUserForPlaying) {
            0 -> {
                if (gameDetail?.partnerGameId != null) {
                    viewModel.playBlacknut(gameDetail.id!!, gameDetail.partnerGameId)
                    historyModel.savePlayGame(
                        LogUserHistory(
                            "PLAY_GAME",
                            1,
                            id,
                            gameDetail.title,
                            userPres.getUserInformation()?.id
                        )
                    )
                } else {
                    val link = viewModel.linkGame
                    if (!link.isNullOrEmpty()) {
                        val intent =
                            android.content.Intent(android.content.Intent.ACTION_VIEW, link.toUri())
                        context.startActivity(intent)
                        historyModel.saveHistory(
                            LogUserHistory(
                                "PLAY_GAME",
                                1,
                                id,
                                gameDetail?.title,
                                userPres.getUserInformation()?.id
                            )
                        )
                    }
                }
            }

            1 -> {
                signinPackgerDialog = true
            }

            2 -> {
                showLoginDialog = true
            }

            3 -> {
                serviceInterrupDialog = true
            }
        }
    }

    val playToken = viewModel.blacknutPlayToken
    val blacknutGameID = viewModel.blacknutGameID
    val partnerGameId = viewModel.blacknutPartnerGameId
    if (playToken != null && blacknutGameID != null) {
        val url =
            "https://cloudgame.vn/play/blacknut/$blacknutGameID?accessToken=${playToken.accessToken}&refreshToken=${playToken.refreshToken}&partnerGameId=$partnerGameId"
        val encodedUrl =
            java.net.URLEncoder.encode(url, java.nio.charset.StandardCharsets.UTF_8.toString())
        LaunchedEffect(url) {
            navController.navigate("play_blacknut_webview/$encodedUrl")
            viewModel.blacknutPlayToken = null
            viewModel.blacknutGameID = null
            viewModel.blacknutPartnerGameId = null
        }
    }

    when {
        isLoading -> {
            LoadingIndicator()
        }

        error != null -> {
            Text("Error: $error")
        }

        gameDetail != null -> {
            Box() {
                VideoBackground(
                    Modifier
                        .matchParentSize()
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    (gameDetail.videoTrailer ?: "").toUri(),
                    mute = if (isMuted.value) 1f else 0f
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
                if (showVideoPopup.value) {
                    VideoPopup(
                        videoUrl = gameDetail.videoTrailer ?: "",
                        onDismiss = { showVideoPopup.value = false }
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = Black50.copy(alpha = 0.5f)),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp),
                            border = BorderStroke(
                                if (backFocus) 0.5.dp else 0.dp,
                                if (backFocus) VietelSecondary else Color.Transparent
                            ),
                            modifier = Modifier
                                .size(30.dp)
                                .onFocusChanged { focusState -> backFocus = focusState.isFocused },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = WhiteColor
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ItemButton(
                                onClick = { isMuted.value = !isMuted.value },
                                iconResId = if (isMuted.value) R.drawable.ic_volume else R.drawable.ic_volume_mute,
                                title = "Âm thanh",
                            )
                            ItemButton(
                                onClick = { showVideoPopup.value = true },
                                iconResId = R.drawable.ic_maximize,
                                title = "Chi tiết",
                            )
                            ItemButton(
                                text = getPegi(gameDetail.star ?: 0.0),
                                title = "Độ tuổi",
                                onClick = {},
                                enabled = false
                            )
                            ItemButton(
                                onClick = {},
                                iconResId = if (gameDetail.multiPlay == true) R.drawable.ic_user_group else R.drawable.ic_user,
                                title = "Người chơi",
                                enabled = false
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .padding(horizontal = 12.dp)
                    ) {
                        Text(
                            text = gameDetail.title ?: "",
                            style = Typography.displayMedium,
                            color = WhiteColor
                        )
                        GapH12()
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            GameButton(
                                iconResId = R.drawable.ic_play,
                                title = "Chơi game",
                                onClick = { playGame() })

                            GameButton(
                                iconResId = if (isLiked == true) R.drawable.ic_heart_fill else R.drawable.ic_heart,
                                title = "Yêu thích",
                                onClick = { likeGame() },
                                disabled = isLiked == false
                            )
                        }
                        GapH16()
                        Text(
                            text = gameDetail.description ?: "",
                            style = Typography.bodyMedium,
                            color = WhiteColor,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                        GapH8()
                        Text(
                            text = "${gameDetail.development ?: ""} | ${gameDetail.partner ?: ""}",
                            style = Typography.bodySmall, color = Grey400
                        )
                        GapH16()
                        Row {
                            Text(
                                text = "Thiết bị chơi: ",
                                style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = WhiteColor
                            )
                            DeviceIcon(gameDetail.controller ?: emptyList())
                        }
                        GapH4()
                        Row {
                            Text(
                                text = "Thể loại: ",
                                style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = WhiteColor
                            )
                            Text(text = gameDetail.types?.joinToString(", ") { e -> e } ?: "",
                                style = Typography.bodySmall,
                                color = Grey300)
                        }
                        GapH4()
                        Row {
                            Text(
                                text = "Thời lượng: ",
                                style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = WhiteColor
                            )
                            Text(
                                text = gameDetail.duration ?: "",
                                style = Typography.bodySmall,
                                color = Grey300
                            )
                        }
                        GapH16()
                        Text(
                            text = "Các game liên quan",
                            style = Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = WhiteColor
                        )
                    }
                    GapH12()
                    Box(
                        modifier = Modifier
                            .height(68.dp)
                            .background(Color.Transparent)
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .background(Color.Transparent),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            itemsIndexed(
                                (gameDetail.relationGame ?: emptyList()).take(6)
                            ) { index, game ->
                                GameCard(
                                    id = game.id ?: "",
                                    imageUrl = game.imageScreen ?: "",
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (signinPackgerDialog) {
        SigningPackagePopup(onDismiss = { signinPackgerDialog = false })
    }

    if (serviceInterrupDialog) {
        ServiceInterruptionPopup(onDismiss = { serviceInterrupDialog = false })
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ItemButton(
    iconResId: Int? = null,
    title: String = "",
    text: String = "",
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    var isFocus by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = if (isFocus) 1.06f else 1f,
                    scaleY = if (isFocus) 1.06f else 1f
                )
                .clip(CircleShape)
                .background(Color.Transparent)
        ) {
            Button(
                onClick = onClick,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Grey700,
                    disabledContainerColor = Grey700
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(
                    if (isFocus) 0.5.dp else 0.dp,
                    if (isFocus) VietelSecondary else Color.Transparent
                ),
                modifier = Modifier
                    .size(24.dp)
                    .then(if (!enabled) Modifier.focusable(false) else Modifier)
                    .onFocusChanged { focusState -> isFocus = focusState.isFocused },
            ) {
                if (iconResId != null && iconResId != 0) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(8.dp),
                        tint = VietelSecondary
                    )
                } else if (text.isNotEmpty()) {
                    Text(
                        text = text,
                        fontSize = 7.sp,
                        fontWeight = FontWeight.Bold,
                        color = VietelSecondary
                    )
                }
            }
        }
        GapH2()
        if (title.isNotEmpty()) {
            Text(text = title, fontSize = 6.sp, fontWeight = FontWeight.SemiBold, color = Grey300)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GameButton(
    iconResId: Int,
    title: String,
    onClick: () -> Unit,
    disabled: Boolean? = false
) {
    var isFocus by remember { mutableStateOf(false) }
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = if (disabled == true) Grey600 else BG_DB27777),
        shape = RoundedCornerShape(size = 30.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 3.dp),
        border = when {
            disabled == true && isFocus -> BorderStroke(0.5.dp, VietelPrimaryColor)
            isFocus -> BorderStroke(0.5.dp, WhiteColor)
            else -> BorderStroke(0.dp, Color.Transparent)
        },
        modifier = Modifier
            .height(24.dp)
            .graphicsLayer(
                scaleX = if (isFocus) 1.15f else 1f,
                scaleY = if (isFocus) 1.15f else 1f
            )
            .onFocusChanged { focusState -> isFocus = focusState.isFocused },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                tint = when {
                    disabled == true && isFocus -> VietelPrimaryColor
                    isFocus -> WhiteColor
                    else -> WhiteColor
                }
            )
            GapW4()
            Text(
                text = title,
                style = Typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = when {
                    disabled == true && isFocus -> VietelPrimaryColor
                    isFocus -> WhiteColor
                    else -> WhiteColor
                }
            )
        }
    }
}
