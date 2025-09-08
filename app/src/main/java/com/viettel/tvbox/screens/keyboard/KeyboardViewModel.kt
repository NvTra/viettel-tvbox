package com.viettel.tvbox.screens.keyboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Loại bàn phím
enum class KeyboardType {
    ALPHABET, NUMBER, SYMBOL
}

// Định nghĩa phím
data class KeyboardKey(
    val label: String,
    val type: KeyType = KeyType.CHAR,
    val value: String = label
)

enum class KeyType {
    CHAR, DELETE, SPACE, ENTER, SWITCH, CLEAR, SEARCH
}

class KeyboardViewModel : ViewModel() {
    var keyboardType by mutableStateOf(KeyboardType.ALPHABET)
        private set
    var focusedRow by mutableStateOf(0)
    var focusedCol by mutableStateOf(0)

    fun onKeyPress(
        key: KeyboardKey,
        inputText: String,
        onInputChanged: (String) -> Unit,
        onSearch: (() -> Unit)? = null
    ) {
        when (key.type) {
            KeyType.CHAR -> onInputChanged(inputText + key.value)
            KeyType.DELETE -> if (inputText.isNotEmpty()) onInputChanged(inputText.dropLast(1))
            KeyType.SPACE -> onInputChanged(inputText + " ")
            KeyType.ENTER -> { /* handled outside */
            }

            KeyType.SWITCH -> switchKeyboardType()
            KeyType.CLEAR -> onInputChanged("")
            KeyType.SEARCH -> onSearch?.invoke()
        }
    }

    fun switchKeyboardType() {
        keyboardType = when (keyboardType) {
            KeyboardType.ALPHABET -> KeyboardType.NUMBER
            KeyboardType.NUMBER -> KeyboardType.ALPHABET
            KeyboardType.SYMBOL -> KeyboardType.ALPHABET
        }
    }
}
