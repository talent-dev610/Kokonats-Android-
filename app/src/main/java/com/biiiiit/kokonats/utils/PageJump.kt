package com.biiiiit.kokonats.utils

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.lib_base.data.*
import com.biiiiit.lib_base.utils.*

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description
 **/


/**
 * jump2 game detail
 */
fun jump2GameDetail(context: Context?, gid: Long) {
    ARouter.getInstance().build(ROUTE_GAME_DETAIL).withLong(COMMON_ID, gid).navigation(context)
}


fun jump2GameTnmtDetail(context: Context?, tnmt: TournamentClass) {
    ARouter.getInstance().build(ROUTE_GAME_TNMT_DETAIL).withSerializable(COMMON_DATA, tnmt)
        .navigation(context)
}


/**
 * 跳转至玩游戏的详情页
 * @param context context
 * @param gameUrl game url
 * @param typeInt 不同的游戏类型
 * @param gameAuth game 的 token
 * @param tnmtId tournament 的某场 tournamentId,  或 pvp 中的 matchId
 * @param playId tournament 获取到的 playId, 或 pvp 中的 pvpId
 * @param duration 监听的 duration
 */
fun jump2WebGame(
    context: Context?, gameUrl: String, typeInt: Int, gameAuth: String, tnmtId: String, playId: Long, duration: Long
) {
    ARouter.getInstance().build(ROUTE_PLAY_GAME)
        .withString(DATA_0, gameUrl)
        .withInt(DATA_1, typeInt)
        .withString(DATA_2, gameAuth)
        .withString(COMMON_DATA, tnmtId)
        .withLong(COMMON_ID, playId)
        .withLong(DATA_3, duration)
        .navigation(context)
}


fun jump2PvpGame(context: Context?, gameUrl: String, gameId: Long, gameAuth: String, matchCId: Long, duration: Long) {
    ARouter.getInstance().build(ROUTE_GAME_PVP)
        .withString(DATA_0, gameUrl)
        .withLong(DATA_1, gameId)
        .withString(DATA_2, gameAuth)
        .withLong(COMMON_DATA, matchCId)
        .withLong(DATA_3, duration)
        .navigation(context)
}


fun jump2TnmtResult(
    context: Context?, typeInt: Int, id: Long, score: String
) {
    ARouter.getInstance().build(ROUTE_GAME_TNMT_RESULT)
        .withLong(COMMON_ID, id)
        .withString(COMMON_DATA, score)
        .withInt(DATA_0, typeInt)
        .navigation(context)
}


