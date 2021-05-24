package com.atlas.core

import com.atlas.sdk.core.AtlasSdk

object TransactionManager {

    const val TOKEN_POINT_ID: String     = //Enter your data
    const val TOKEN_POINT_TOKEN: String  = //Enter your data
    const val TOKEN_SERVICE_ID: Int      = //Enter your data

    const val PAY_POINT_ID: String       = //Enter your data
    const val PAY_POINT_TOKEN: String    = //Enter your data
    const val PAY_SERVICE_ID: Int        = //Enter your data

    val FIELDS: MutableMap<String, String> = mutableMapOf("account" to "//Enter your data")

    val sdk: AtlasSdk = AtlasSdk()
}