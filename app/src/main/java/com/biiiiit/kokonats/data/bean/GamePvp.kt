package com.biiiiit.kokonats.data.bean

import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PVP
import com.google.gson.annotations.SerializedName

/**
 * @Author yo_hack
 * @Date 2022.01.05
 * @Description
 **/


class PvpMatchSession {
    /** 匹配会话id **/
    var sessionId: String = ""

    var matchId: Long = 0

    var matchPlayId: Long = 0


    var matchClassId: Long = 0

    /** 匹配状态(-1 错误; 0 已请求; 1 匹配中; 2 已匹配; 4 匹配超期) **/
    var state: Int = 0

    var matchingUsers: List<PvpUser>? = null
}

/**
 * pvp user
 */
class PvpUser {
    var userName: String = ""
    var picture: String = ""
    var subscriber: String = ""
}


/**
 * 用于查询结果
 */
class PvpResultState {

    var matchId: String = ""

    /** 比赛结果(Win:W; Lose:L; DRAW:D) **/
    var result: String? = ""

    var state: Int = 0

    var energy: Long = 0

    var players: List<PvpUserScore>? = null
}


/**
 * pvp user result
 */
class PvpUserScore {
    @SerializedName(value = "username", alternate = ["userName"])
    var username: String = ""
    var picture: String = ""
    var subscriber: String = ""
    var totalScore: String = ""

    /** 未知:-1; 开始:0; 结束:1 **/
    var state: Int = 0
}


class GameMatch {
    var id: Long = 0
    var gameId: Long = 0
    var matchName: String = ""
    var thumbnail: String = ""
    var coverImageUrl: String = ""
    var description: String = ""
    var type: Int = 0
    var matchingSecond: Long = 0
    var durationSecond: Long = 0
    var entryFee: Long = 0
    var keyword: String = ""
    var tag: String = ""
    var winningPayout: Int = 0

    fun map2Tnmt(): TournamentClass = TournamentClass().also {
        it.id = id
        it.gameId = gameId
        it.tournamentName = matchName
        it.description = description
        it.thumbnail = thumbnail
        it.coverImageUrl = coverImageUrl
        it.entryFee = entryFee
        it.keyword = keyword
        it.tag = tag
        // duration  play https://github.com/BiiiiiT-Inc/koko-android/issues/23
        it.durationPlaySecond = durationSecond
        it.winningPayout = winningPayout
        it.type = PLAY_TYPE_PVP
    }
}


