import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.viettel.tvbox.models.UserInformation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserPreferences private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferences(context.applicationContext).also { INSTANCE = it }
            }
    }

    fun getKeys(): Set<String> = prefs.all.keys

    fun isLogin(): Boolean = getToken().isNotEmpty()

    // Token methods
    fun saveToken(token: String) {
        prefs.edit { putString("token", token) }
    }

    fun getToken(): String = prefs.getString("token", "") ?: ""

    fun saveRefreshToken(token: String) {
        prefs.edit { putString("refresh_token", token) }
    }

    fun getRefreshToken(): String = prefs.getString("refresh_token", "") ?: ""

    // Token expiration methods
    fun saveTokenExpiration(expiresIn: String) {
        try {
            // Giả sử expiresIn có format "DD/MM/YYYY HH:mm:ss" như trong Angular
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val expirationDate = dateFormat.parse(expiresIn)
            val expirationTime = expirationDate?.time ?: 0L
            prefs.edit { putLong("token_expiration", expirationTime) }
        } catch (e: Exception) {
            // Nếu parse lỗi, có thể expiresIn là timestamp
            try {
                val expirationTime = expiresIn.toLong()
                prefs.edit { putLong("token_expiration", expirationTime) }
            } catch (e2: Exception) {
                // Nếu vẫn lỗi, set thời gian mặc định (1 giờ từ hiện tại)
                val defaultExpiration = System.currentTimeMillis() + (60 * 60 * 1000) // 1 hour
                prefs.edit { putLong("token_expiration", defaultExpiration) }
            }
        }
    }

    fun getTokenExpiration(): Long = prefs.getLong("token_expiration", 0L)

    // Method kiểm tra token có hợp lệ không
    fun isTokenValid(): Boolean {
        val token = getToken()
        val expirationTime = getTokenExpiration()

        if (token.isEmpty() || expirationTime <= 0) {
            return false
        }

        // Kiểm tra token có hết hạn không (thêm buffer 2 giây)
        val currentTime = System.currentTimeMillis()
        return expirationTime > (currentTime + 2000)
    }

    // Method để lưu thông tin auth hoàn chỉnh
    fun saveAuthData(accessToken: String, refreshToken: String, expiresIn: String) {
        prefs.edit {
            putString("token", accessToken)
            putString("refresh_token", refreshToken)
        }
        saveTokenExpiration(expiresIn)
    }

    // User Information methods
    fun saveUserInformation(info: UserInformation) {
        val json = gson.toJson(info)
        prefs.edit { putString("user_information", json) }
    }

    fun getUserInformation(): UserInformation? {
        val json = prefs.getString("user_information", null)
        return if (json != null) {
            gson.fromJson(json, UserInformation::class.java)
        } else {
            null
        }
    }

    // Clear methods
    fun clearAuth() {
        prefs.edit {
            remove("token")
            remove("refresh_token")
            remove("token_expiration")
            remove("user_information")
        }
    }

    fun clearAll() {
        prefs.edit { clear() }
    }
    
    fun getTokenInfo(): String {
        val token = getToken()
        val refreshToken = getRefreshToken()
        val expiration = getTokenExpiration()
        val isValid = isTokenValid()

        return """
            Token: ${if (token.isNotEmpty()) "***${token.takeLast(10)}" else "Empty"}
            Refresh Token: ${if (refreshToken.isNotEmpty()) "***${refreshToken.takeLast(10)}" else "Empty"}
            Expiration: ${if (expiration > 0) Date(expiration).toString() else "Not set"}
            Is Valid: $isValid
        """.trimIndent()
    }

    fun hasValidSession(): Boolean {
        return isLogin() && isTokenValid()
    }

    fun getTokenRemainingTime(): Long {
        val expiration = getTokenExpiration()
        val currentTime = System.currentTimeMillis()
        return maxOf(0L, expiration - currentTime)
    }

    fun isTokenExpiringSoon(minutesThreshold: Int = 5): Boolean {
        val remainingTime = getTokenRemainingTime()
        val thresholdMs = minutesThreshold * 60 * 1000L
        return remainingTime in 1..thresholdMs
    }
}