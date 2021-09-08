package com.fingerprintjs.android.pro.playgroundpro


import android.content.Context
import androidx.preference.PreferenceManager


interface ApplicationPreferences {
    fun getEndpointUrl(): String
    fun getApiToken(): String
    fun getLastSecurityToken(): String

    fun setEndpointUrl(endpointUrl: String)
    fun setApiToken(apiToken: String)
    fun setLastSecurityToken(lastSecurityToken: String)
}

class ApplicationPreferencesImpl(context: Context) : ApplicationPreferences {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val API_TOKEN_KEY = context.getString(R.string.apiTokenKey)
    private val ENDPOINT_URL_KEY = context.getString(R.string.endpointUrlKey)
    private val LAST_SECURITY_TOKEN = context.getString(R.string.lastSavedSecurityToken)

    override fun getEndpointUrl() = preferences.getString(ENDPOINT_URL_KEY, "") ?: ""

    override fun getApiToken() = preferences.getString(API_TOKEN_KEY, "") ?: ""

    override fun getLastSecurityToken() = preferences.getString(LAST_SECURITY_TOKEN, "") ?: ""

    override fun setEndpointUrl(endpointUrl: String) {
        preferences.edit().putString(ENDPOINT_URL_KEY, endpointUrl).apply()
    }

    override fun setApiToken(apiToken: String) {
        preferences.edit().putString(API_TOKEN_KEY, apiToken).apply()
    }

    override fun setLastSecurityToken(lastSecurityToken: String) {
        preferences.edit().putString(LAST_SECURITY_TOKEN, lastSecurityToken).apply()
    }
}