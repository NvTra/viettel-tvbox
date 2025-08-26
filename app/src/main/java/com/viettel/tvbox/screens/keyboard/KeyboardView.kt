package com.viettel.tvbox.screens.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.viettel.tvbox.theme.BG_DB27777
import com.viettel.tvbox.theme.Grey600
import kotlin.math.min

@Composable
fun KeyboardView(
    inputText: String,
    onInputChanged: (String) -> Unit,
    viewModel: KeyboardViewModel,
    onEnter: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val keys = getKeyboardLayout(viewModel.keyboardType)
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                val rowCount = keys.size
                val colCount = keys[viewModel.focusedRow].size
                when (event.key) {
                    Key.DirectionUp -> {
                        if (viewModel.focusedRow > 0) {
                            val newRow = viewModel.focusedRow - 1
                            val newCol = min(viewModel.focusedCol, keys[newRow].size - 1)
                            viewModel.focusedRow = newRow
                            viewModel.focusedCol = newCol
                            true
                        } else {
                            false
                        }
                    }

                    Key.DirectionDown -> {
                        if (viewModel.focusedRow < rowCount - 1) {
                            val newRow = viewModel.focusedRow + 1
                            val newCol = min(viewModel.focusedCol, keys[newRow].size - 1)
                            viewModel.focusedRow = newRow
                            viewModel.focusedCol = newCol
                            true
                        } else {
                            false
                        }
                    }

                    Key.DirectionLeft -> {
                        if (viewModel.focusedCol > 0) {
                            viewModel.focusedCol--
                            true
                        } else {
                            false
                        }
                    }

                    Key.DirectionRight -> {
                        if (viewModel.focusedCol < colCount - 1) {
                            viewModel.focusedCol++
                            true
                        } else {
                            false
                        }
                    }

                    Key.Enter, Key.NumPadEnter -> {
                        val key = keys[viewModel.focusedRow][viewModel.focusedCol]
                        if (key.type == KeyType.ENTER) {
                            onEnter(inputText)
                        } else {
                            viewModel.onKeyPress(key, inputText, onInputChanged)
                        }
                        true
                    }

                    else -> false
                }
            }
    ) {
        keys.forEachIndexed { rowIdx, row ->
            Row(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEachIndexed { colIdx, key ->
                    val isFocused = viewModel.focusedRow == rowIdx && viewModel.focusedCol == colIdx
                    val keyWidth = when (key.type) {
                        KeyType.SPACE -> 60.dp
                        else -> 20.dp
                    }
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(width = keyWidth, height = 20.dp)
                            .background(
                                if (isFocused) BG_DB27777 else Color.Transparent,
                                RoundedCornerShape(6.dp)
                            )
                            .border(
                                0.5.dp,
                                if (isFocused) BG_DB27777 else Grey600,
                                RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when (key.type) {
                            KeyType.DELETE -> {
                                Text(text = "⌫", color = Color.White, fontSize = 9.sp)
                            }

                            else -> {
                                Text(text = key.label, color = Color.White, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getKeyboardLayout(type: KeyboardType): List<List<KeyboardKey>> {
    return when (type) {
        KeyboardType.ALPHABET -> listOf(
            listOf(
                KeyboardKey("A"),
                KeyboardKey("B"),
                KeyboardKey("C"),
                KeyboardKey("D"),
                KeyboardKey("E"),
                KeyboardKey("F"),
                KeyboardKey("G"),
            ),
            listOf(
                KeyboardKey("H"),
                KeyboardKey("I"),
                KeyboardKey("J"),
                KeyboardKey("K"),
                KeyboardKey("L"),
                KeyboardKey("M"),
                KeyboardKey("N"),
            ),
            listOf(
                KeyboardKey("O"),
                KeyboardKey("P"),
                KeyboardKey("Q"),
                KeyboardKey("R"),
                KeyboardKey("S"),
                KeyboardKey("T"),
                KeyboardKey("U"),

                ),
            listOf(
                KeyboardKey("V"),
                KeyboardKey("W"),
                KeyboardKey("X"),
                KeyboardKey("Y"),
                KeyboardKey("Z"),
                KeyboardKey("-"),
                KeyboardKey("'"),
            ),
            listOf(
                KeyboardKey("←", KeyType.DELETE),
                KeyboardKey("123", KeyType.SWITCH),
                KeyboardKey("Xóa", KeyType.CLEAR),
                KeyboardKey("Dấu cách", KeyType.SPACE), KeyboardKey("OK", KeyType.ENTER)
            )
        )

        KeyboardType.NUMBER -> listOf(
            listOf(
                KeyboardKey("1"),
                KeyboardKey("2"),
                KeyboardKey("3"),
                KeyboardKey("&"),
                KeyboardKey("#"),
                KeyboardKey("(", KeyType.CHAR, "("),
                KeyboardKey(")", KeyType.CHAR, ")"),
            ),
            listOf(
                KeyboardKey("4"),
                KeyboardKey("5"),
                KeyboardKey("6"),
                KeyboardKey("@"),
                KeyboardKey("!"),
                KeyboardKey("?"),
                KeyboardKey(":", KeyType.CHAR, ":"),
            ),
            listOf(
                KeyboardKey("7"),
                KeyboardKey("8"),
                KeyboardKey("9"),
                KeyboardKey("."),
                KeyboardKey("-"),
                KeyboardKey("_"),
                KeyboardKey("\""),
            ),
            listOf(
                KeyboardKey("0"),
                KeyboardKey("/"),
                KeyboardKey("$"),
                KeyboardKey("%"),
                KeyboardKey("+"),
                KeyboardKey("["),
                KeyboardKey("]"),
            ),
            listOf(
                KeyboardKey("ABC", KeyType.SWITCH), KeyboardKey("←", KeyType.DELETE),
                KeyboardKey("C", KeyType.CLEAR),
                KeyboardKey(" ", KeyType.SPACE), KeyboardKey("OK", KeyType.ENTER)
            )
        )

        KeyboardType.SYMBOL -> listOf(
            listOf(
                KeyboardKey("!"),
                KeyboardKey("?"),
                KeyboardKey("'"),
                KeyboardKey("\""),
                KeyboardKey(":"),
                KeyboardKey(";"),
                KeyboardKey("/"),
                KeyboardKey("*"),
                KeyboardKey("=", KeyType.CHAR, "=")
            ),
            listOf(
                KeyboardKey("<"),
                KeyboardKey(">"),
                KeyboardKey("["),
                KeyboardKey("]"),
                KeyboardKey("{", KeyType.CHAR, "{"),
                KeyboardKey("}", KeyType.CHAR, "}")
            ),
            listOf(
                KeyboardKey("123", KeyType.SWITCH), KeyboardKey("←", KeyType.DELETE)
            ),
            listOf(
                KeyboardKey(" ", KeyType.SPACE), KeyboardKey("OK", KeyType.ENTER)
            )
        )
    }
}
