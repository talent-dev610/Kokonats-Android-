package com.biiiiit.lib_base.utils

import android.content.Context
import android.os.Environment
import com.biiiiit.lib_base.BaseApp
import java.io.File
import java.math.BigInteger

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description file utils
 **/


val DIR_ROOT = "koko"

/**
 * glideCache
 */
val DIR_GLIDE = "glide"

/**
 * okHttp
 */
val DIR_OKHTTP = "okHttp"

/**
 * webView
 */
val DIR_WEBVIEW = "webView"

/**
 * The number of bytes in a kilobyte.
 */
val ONE_KB: Long = 1024

/**
 * The number of bytes in a kilobyte.
 */
val ONE_KB_BI = BigInteger.valueOf(ONE_KB)

/**
 * The number of bytes in a megabyte.
 */
val ONE_MB = ONE_KB * ONE_KB

/**
 * The number of bytes in a megabyte.
 */
val ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI)

/**
 * The number of bytes in a gigabyte.
 */
val ONE_GB = ONE_KB * ONE_MB

/**
 * sd mounted
 */
fun isSdCardMounted() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

fun getAppFolder(context: Context = BaseApp.app): File {
    val file = if (isSdCardMounted()) {
        //有sdCard

        // data/android/<package-name>/cache/DIR_ROOT
        File(context.externalCacheDir, DIR_ROOT)
    } else {
        // data/data/<package-name>/cache/DIR_ROOT
        File(context.cacheDir, DIR_ROOT)
    }

    createDirs(file)

    return file
}

/**
 * create dirs
 */
fun createDirs(file: File?) {
    file?.let {
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}


/**
 * get dir of app
 */
fun getDirRoot(
    context: Context = BaseApp.app,
    rootPath: String,
): File {
    val parent = getAppFolder(context)

    val rootDir = File(parent, rootPath)
    createDirs(rootDir)
    return rootDir
}


/**
 * okhttpCache path
 */
fun getOkHttpFile(context: Context = BaseApp.app): File {
    return getDirRoot(context, DIR_OKHTTP)
}


/**
 * okhttpCache path
 */
fun getWebViewCacheDir(context: Context = BaseApp.app): File {
    return getDirRoot(context, DIR_WEBVIEW)
}