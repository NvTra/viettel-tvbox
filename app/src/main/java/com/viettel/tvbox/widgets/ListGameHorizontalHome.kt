package com.viettel.tvbox.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.viettel.tvbox.models.ManagerHomeConfig
import com.viettel.tvbox.theme.GapH12
import com.viettel.tvbox.theme.GapW8
import com.viettel.tvbox.theme.Typography
import com.viettel.tvbox.theme.VietelPrimaryColor
import com.viettel.tvbox.theme.WhiteColor
import com.viettel.tvbox.utils.getImageUrl

@Composable
fun ListGameHorizontal(gameConfigItem: ManagerHomeConfig? = null, navController: NavController) {
    Column {
        Row(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = gameConfigItem?.title ?: "",
                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            GapW8()
            GameButton(onClick = { navController.navigate("all_game_by_title/${gameConfigItem?.id}/${gameConfigItem?.title}") })
        }
        GapH12()
        Box(
            modifier = Modifier
                .height(155.dp)
                .background(Color.Transparent)
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Transparent),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                itemsIndexed(
                    gameConfigItem?.items ?: emptyList()
                ) { index, game ->
                    GameCard(
                        game.id ?: "",
                        game.title ?: "",
                        getImageUrl(game.image ?: ""),
                        navController
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GameButton(
    onClick: () -> Unit,
) {
    var isFocus by remember { mutableStateOf(false) }
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFocus) VietelPrimaryColor else Color.Transparent,
        ),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(width = 1.dp, VietelPrimaryColor),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 3.dp),
        modifier = Modifier
            .height(24.dp)
            .onFocusChanged { focusState -> isFocus = focusState.isFocused },
    ) {
        Text(
            text = "Xem tất cả",
            style = Typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = if (isFocus) WhiteColor else VietelPrimaryColor
        )

    }
}