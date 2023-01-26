package com.biiiiit.lib_base.net

import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.R

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description
 **/

/**
 * time out millis
 */
const val CONNECT_TIME_MILLIS = 20000L
const val READ_TIME_MILLIS = 60000L
const val WRITE_TIME_MILLIS = 60000L

const val NET_SUCCESS = 200

/**
 * self define error
 */
const val NET_NO_DATA = -99
const val NET_ERROR_UNKNOWN = -100
const val NET_ERROR_NETWORK = -101
const val NET_ERROR_PARSE = -101
const val NET_ERROR_SSL = -102
const val NET_ERROR_TIMEOUT = -103
const val NET_AUTH_FAILED = -403


val LOADING_TXT = BaseApp.app.getString(R.string.loading_dot)

val UNKNOWN_ERROR = BaseApp.app.getString(R.string.unknown_error)

val EMPTY_ERROR = BaseApp.app.getString(R.string.no_data)

/**
 * list no data
 */
const val LIST_NO_TOTAL = -1

const val AUTH_STR= "Authorization"

const val KOKO_GAME_URL = "https://game.kokonats.club"
const val SPONSOR_URL = "https://cloud7.link/"