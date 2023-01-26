package com.biiiiit.lib_base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.biiiiit.lib_base.base.BizException
import com.biiiiit.lib_base.data.KoKoResponse
import com.biiiiit.lib_base.net.*
import java.lang.Exception
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description some ext
 **/


/**
 *  no op delegate
 *  for most cases, we only want implements fewer functions of interfaces
 */
inline fun <reified T : Any> noOpDelegate(): T {
    val javaClass = T::class.java
    val noDelegate = InvocationHandler { _, _, _ -> }
    return Proxy.newProxyInstance(
        javaClass.classLoader, arrayOf(javaClass), noDelegate
    ) as T
}


fun <T> t2KokoResponse(t: T?, unit: ((T) -> Boolean)?): KoKoResponse<T> {
    if (t != null && (unit == null || unit.invoke(t))) {
        return KoKoResponse(NET_SUCCESS, "", t)
    } else {
        throw BizException(NET_ERROR_UNKNOWN, UNKNOWN_ERROR)
    }
}

fun <T> list2KokoResponse(list: List<T>?): KoKoResponse<List<T>> {
    if (!list.isNullOrEmpty()) {
        return KoKoResponse(NET_SUCCESS, "", list)
    } else {
        throw BizException(NET_NO_DATA, EMPTY_ERROR)
    }
}

/**
 * 复制文字
 */
fun copyText(context: Context?, str: String?): Boolean {
    return if (context != null && str != null) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(null, str)
        clipboard.setPrimaryClip(clip)
        true
    } else {
        false
    }
}

/**
 * 跳转外部链接
 */
fun jump2Outside(context: Context?, url: String? = null): Boolean {
    if (context != null && url.isNullOrEmpty().not()) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
            return true
        } catch (e: Exception) {
        }
    }
    return false
}


/**
 * 跳转应用市场
 */
fun jump2Market(context: Context, appId: String): Boolean {
    return try {
        val uri = Uri.parse("market://details?id=${appId}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}
