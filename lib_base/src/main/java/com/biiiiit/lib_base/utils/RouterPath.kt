package com.biiiiit.lib_base.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.biiiiit.lib_base.data.COMMON_TAB_INDEX

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description router path
 **/


/**
 * base login
 */
const val ROUTE_LOGIN = "/base/login"

/**
 * home page
 */
const val ROUTE_MAIN = "/main/home"


/**
 * game detail
 */
const val ROUTE_GAME_DETAIL = "/main/gameDetail"


const val ROUTE_GAME_TNMT_DETAIL = "/main/gameTnmt"


const val ROUTE_GAME_PVP = "/main/gamePvp"

const val ROUTE_GAME_TNMT_RESULT = "/main/gameTnmtResult"

const val ROUTE_PLAY_GAME = "/main/playGame"

const val ROUTE_TNMT_HISTORY = "/main/tnmtHistory"


const val ROUTE_USER_INFO_EDIT = "/user/info/edit"

const val ROUTE_CHAT_MAIN = "/main/chat"

const val ROUTE_STORE_ENERGY = "/main/store/energy"
const val ROUTE_STORE_GAME = "/main/store/game"


fun simpleJump(path: String, context: Context?) {
    ARouter.getInstance().build(path).navigation(context)
}

fun simpleJumpWith(path: String, context: Context?, bundle: Bundle) {
    ARouter.getInstance().build(path).with(bundle).navigation(context)
}

fun jump2Main(context: Context?, index: Int = 1) {
    ARouter.getInstance()
        .build(ROUTE_MAIN)
        .withInt(COMMON_TAB_INDEX, index)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        .navigation(context)
}
