package com.fingerprintjs.android.pro.playgroundpro


import android.content.Context
import androidx.preference.PreferenceManager


interface ApplicationPreferences {
    fun getEndpointUrl(): String
    fun getApiToken(): String
}

class ApplicationPreferencesImpl(context: Context) : ApplicationPreferences {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val API_TOKEN_KEY = context.getString(R.string.apiTokenKey)
    private val ENDPOINT_URL_KEY = context.getString(R.string.endpointUrlKey)

    override fun getEndpointUrl() = preferences.getString(ENDPOINT_URL_KEY, "") ?: ""

    override fun getApiToken() = preferences.getString(API_TOKEN_KEY, "") ?: ""
}