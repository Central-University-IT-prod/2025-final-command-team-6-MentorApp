package com.prodmobile.template.data.accounts.account_info

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.prodmobile.template.core.Constants
import com.prodmobile.template.core.models.AccountInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.accountInfoDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.ACCOUNT_INFO_DATA_STORE
)

class AccountInfoDataSourceImpl(
    private val settingsDataStore: DataStore<Preferences>
) : AccountInfoDataSource {
    override suspend fun saveAccountInfo(info: AccountInfo) {
        val encoded = Json.encodeToString(info)
        settingsDataStore.edit { preferences ->
            preferences[accountInfoToken] = encoded
        }
    }

    override suspend fun getAccountInfo(): AccountInfo? {
        val preferences = settingsDataStore.data
        val accountInfo = try {
            preferences.map { it[accountInfoToken] }.first()!!
        } catch (e: Exception) {
            Log.e("GetAccountInfo Error", e.stackTraceToString())
            return null
        }
        return Json.decodeFromString<AccountInfo>(accountInfo)
    }

    override suspend fun clearAccountInfo() {
        settingsDataStore.edit { preferences ->
            preferences.remove(accountInfoToken)
        }
    }

    private val accountInfoToken = stringPreferencesKey("account_info_token")
}