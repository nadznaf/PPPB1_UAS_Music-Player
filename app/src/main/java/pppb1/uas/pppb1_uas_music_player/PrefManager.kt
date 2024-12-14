package pppb1.uas.pppb1_uas_music_player

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.internal.synchronized
import kotlin.concurrent.Volatile

class PrefManager private constructor(context: Context){
    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLE = "user"
        private const val KEY_PASSWORD = "password"

        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME,
            Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }
    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }
    fun saveRole(role: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }
    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "") ?:""
    }
    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?:""
    }
    fun getRole(): String {
        return sharedPreferences.getString(KEY_ROLE, "") ?:""
    }
    fun clear(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}