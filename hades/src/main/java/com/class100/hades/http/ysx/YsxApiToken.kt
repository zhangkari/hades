package com.class100.hades.http.ysx

import com.class100.atropos.env.context.AtPrefs

object YsxApiToken {
    private const val key_token = "_key_token"
    fun getToken(): String {
        return AtPrefs.get(key_token, "")
    }

    fun saveToken(token: String) {
        AtPrefs.put(key_token, token)
    }
}