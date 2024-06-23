package datlowashere.project.rcoffee.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_PHONE = "phone"
    private const val KEY_USER_NAME = "name"
    private const val KEY_USER_TOKEN = "key_user_token"


    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setUserEmail(context: Context, email: String) {
        getSharedPreferences(context).edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, null)
    }

    fun setUserName(context: Context, name: String) {
        getSharedPreferences(context).edit().putString(KEY_USER_NAME, name).apply()
    }
    fun getUserName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_NAME, null)
    }

    fun setUserPhone(context: Context, phone: String) {
        getSharedPreferences(context).edit().putString(KEY_USER_PHONE, phone).apply()
    }
    fun getUserPhone(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_PHONE, null)
    }

    fun setUserToken(context: Context, token: String) {
        getSharedPreferences(context).edit().putString(KEY_USER_TOKEN, token).apply()
    }

    fun getUserToken(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USER_TOKEN, null)
    }
    fun clear(context: Context) {
        getSharedPreferences(context).edit().clear().apply()
    }
}
