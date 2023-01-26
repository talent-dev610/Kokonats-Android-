package com.biiiiit.kokonats.data.repo

import com.biiiiit.kokonats.data.api.UserTnmtApi
import com.biiiiit.kokonats.data.bean.GameAuth
import com.biiiiit.kokonats.data.bean.PvpMatchSession
import com.biiiiit.kokonats.data.bean.PvpResultState
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.biiiiit.lib_base.data.BaseResponse
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.utils.list2KokoResponse
import com.biiiiit.lib_base.utils.t2KokoResponse

/**
 * @Author yo_hack
 * @Date 2021.11.04
 * @Description
 **/
class UserTnmtRepository {
    private val userTnmtApi: UserTnmtApi by lazy { KokoNetApi.INSTANCE.create(UserTnmtApi::class.java) }


    suspend fun getUserTnmtPlays(reduce: Boolean = false): BaseResponse<List<TournamentPlay>> {
        var result = userTnmtApi.getUserTnmtPlays()
        if (reduce) {
            result = result.subList(0, minOf(result.size, 3))
        }
        return list2KokoResponse(result)
    }

    suspend fun getUserGameAuth(gameId: Long): BaseResponse<GameAuth> {
        val result = userTnmtApi.getGameAuth(mapOf("gameId" to gameId))
        return t2KokoResponse(result) { !result.token.isNullOrBlank() }
    }

    suspend fun playTournament(tournamentId: Long): BaseResponse<TournamentPlay> {
        val result = userTnmtApi.playTournament(mapOf("tournamentId" to tournamentId))
        return t2KokoResponse(result) { it.id > 0 }
    }

    suspend fun getGamePvpSession(gameId: Long): BaseResponse<PvpMatchSession> {
        val result = userTnmtApi.getGamePvpSession(gameId)
        return t2KokoResponse(result) { !it.sessionId.isNullOrBlank() }
    }

    suspend fun getClassPvpSession(matchClassId: Long): BaseResponse<PvpMatchSession> {
        val result = userTnmtApi.getClassPvpSession(matchClassId)
        return t2KokoResponse(result) { !it.sessionId.isNullOrBlank() }
    }

    suspend fun getPvpResult(matchPlayId: Long): BaseResponse<PvpResultState> {
        val result = userTnmtApi.getPvpResult(matchPlayId)
        return t2KokoResponse(result) { it.matchId.isNullOrBlank().not() }
    }

}

