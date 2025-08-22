import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import androidx.core.content.edit
import com.viettel.tvbox.models.auth.UserInformation

class UserPreferences private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
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

    // Token
    fun saveToken(token: String) {
        prefs.edit { putString("token", token) }
    }

    fun getToken(): String = prefs.getString("token", "") ?: ""

    // Refresh Token
    fun saveRefreshToken(token: String) {
        prefs.edit { putString("refresh_token", token) }
    }

    fun getRefreshToken(): String = prefs.getString("refresh_token", "") ?: ""


    fun removeInfoLogin() {
        prefs.edit { remove("infor_login") }
    }

    // Username
    fun saveUsername(username: String) {
        prefs.edit { putString("username", username) }
    }

    fun getUsername(): String = prefs.getString("username", "") ?: ""

    // UserId
    fun saveUserId(userId: String) {
        prefs.edit { putString("user_id", userId) }
    }

    fun getUserId(): String = prefs.getString("user_id", "") ?: ""

    // UserInformation (save/load as JSON)
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

    fun clearAuth() {
        prefs.edit {
            clear()
        }
    }
}
