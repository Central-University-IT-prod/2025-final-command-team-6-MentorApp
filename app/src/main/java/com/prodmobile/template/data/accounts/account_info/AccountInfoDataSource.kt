package com.prodmobile.template.data.accounts.account_info

import com.prodmobile.template.core.models.AccountInfo

interface AccountInfoDataSource {
    suspend fun saveAccountInfo(info: AccountInfo)
    suspend fun getAccountInfo(): AccountInfo?
    suspend fun clearAccountInfo()
}