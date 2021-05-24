package com.atlas.sdk.data.network.security

import android.util.Log
import java.security.MessageDigest


object AuthEncryption {

    private val TAG = AuthEncryption::class.simpleName

    //    Util Extensions
    private fun String.toMD5() = MessageDigest.getInstance("MD5").digest(this.toByteArray()).toHex()
    private fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }

    internal fun createHash(json: String, pointToken:  String): String{
        val hash = "$json$pointToken".toMD5()

        Log.d(TAG, "json $json")
        Log.d(TAG, "pointId $pointToken")
        Log.d(TAG, "hash $hash")
        return hash
    }
}