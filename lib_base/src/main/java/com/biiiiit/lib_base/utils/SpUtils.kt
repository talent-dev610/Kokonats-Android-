package com.biiiiit.lib_base.utils

import com.google.gson.Gson
import com.tencent.mmkv.MMKV

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description share path utils
 **/

/**
 * 登录用户
 */
const val SP_LOGIN_USER = "login_user"

/**
 * user token
 */
const val SP_USER_TOKEN = "user_token"

const val SP_ALREADY_PLAYED = "already_played"

fun putBoolean(key: String, any: Boolean, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.encode(key, any)

fun putInt(key: String, any: Int, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.encode(key, any)

fun putLong(key: String, any: Long, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.encode(key, any)

fun putFloat(key: String, any: Float, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.encode(key, any)

fun putDouble(key: String, any: Double, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.encode(key, any)

fun putString(key: String, any: String, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.encode(key, any)

fun putByteArray(key: String, any: ByteArray, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.encode(key, any)

fun putAny(key: String, any: Any?, mmkv: MMKV = MMKV.defaultMMKV()) {
    if (any != null) {
        mmkv.encode(key, Gson().toJson(any))
    } else {
        mmkv.removeValueForKey(key)
    }
}

fun removeValueForKey(key: String, mmkv: MMKV = MMKV.defaultMMKV()) {
    mmkv.removeValueForKey(key)
}


fun getBoolean(key: String, default: Boolean = false, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.decodeBool(key, default)


fun getInt(key: String, default: Int = 0, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.decodeInt(key, default)

fun getLong(key: String, default: Long = 0, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.decodeLong(key, default)


fun getFloat(key: String, default: Float = 0.0f, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.decodeFloat(key, default)

fun getDouble(key: String, default: Double = 0.0, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.decodeDouble(key, default)


fun getString(key: String, default: String = "", mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.decodeString(key, default) ?: default

fun getByteArray(key: String, default: ByteArray? = null, mmkv: MMKV = MMKV.defaultMMKV()) =
    mmkv.decodeBytes(key, default)

inline fun <reified T : Any> getAny(key: String, mmkv: MMKV = MMKV.defaultMMKV()): T? {
    return Gson().fromJson<T>(mmkv.decodeString(key, ""))
}


