package com.biiiiit.lib_base.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description
 **/

inline fun <reified T : Any> Gson.fromJson(json: String?): T? = if (json.isNullOrEmpty()) {
    null
} else {
    fromJson(json, object : TypeToken<T>() {}.type)
}


