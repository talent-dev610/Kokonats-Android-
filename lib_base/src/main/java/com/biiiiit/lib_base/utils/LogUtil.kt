package com.biiiiit.lib_base.utils

import android.util.Log
import com.biiiiit.lib_base.BuildConfig

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description log util for extend， all log should use this utils
 **/


/**
 * log  e
 */
fun String?.loge(tag: String? = "Koko", throwable: Throwable? = null) {
    if (this.isNullOrBlank().not()) {
        Log.e(tag, this, throwable)
    }
}


/**
 * log i
 */
fun String?.logi(tag: String = "Koko", throwable: Throwable? = null) {
    if (BuildConfig.DEBUG) {
        if (this.isNullOrBlank().not()) {
            Log.i(tag, this, throwable)
        }
    }
}