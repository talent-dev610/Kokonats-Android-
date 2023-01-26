package com.biiiiit.kokonats.data.repo

import com.biiiiit.kokonats.data.api.GameApi
import com.biiiiit.kokonats.data.bean.*
import com.biiiiit.lib_base.base.BizException
import com.biiiiit.lib_base.data.BaseResponse
import com.biiiiit.lib_base.data.KoKoResponse
import com.biiiiit.lib_base.net.*
import com.biiiiit.lib_base.utils.list2KokoResponse
import com.biiiiit.lib_base.utils.t2KokoResponse

/**
 * @Author yo_hack
 * @Date 2021.10.24
 * @Description
 **/
class GameRepository {
    private val gameApi: GameApi by lazy { KokoNetApi.INSTANCE.create(GameApi::class.java) }


    suspend fun getHomeGameList(order: String?, category: String?): BaseResponse<List<Game>> {
        val result = gameApi.getHomeGameList(order, category)
        return list2KokoResponse(result)
    }

    suspend fun getGameDetail(gameId: Long): BaseResponse<Game> {
        val result = gameApi.getGameDetail(gameId)
        return t2KokoResponse(result) { it.id > 0 }
    }

    suspend fun getTnmtsByGid(gameId: Long): BaseResponse<List<TournamentClass>> {
        val result = gameApi.getTnmtsByGid(gameId)
        return list2KokoResponse(result)
    }

    suspend fun getMatchByGid(gameId: Long): BaseResponse<List<TournamentClass>> {
        val result = gameApi.getPvpByGid(gameId).map { it.map2Tnmt() }
        return list2KokoResponse(result)
    }

    suspend fun getGameItemsByGid(gameId: Long): BaseResponse<List<GameItem>> {
        val result = gameApi.getGameItemsByGid(gameId).map { it.map2Item() }
        return list2KokoResponse(result)
    }

    suspend fun getTnmtsClass(): BaseResponse<List<TournamentClass>> {
        val result = gameApi.getTnmtsClass()
        return list2KokoResponse(result)
    }

    suspend fun getTnmtByTnmtCId(tnmtCId: Long): BaseResponse<Tournament> {
        val result = gameApi.getTnmtByTnmtCId(tnmtCId)
        return t2KokoResponse(result) { it.id > 0 }
    }


    suspend fun getTnmtsHistoryByTnmtCId(tnmtCId: Long, size: Int): BaseResponse<List<Tournament>> {
        val result = gameApi.getTnmtsHistoryByTnmtCId(tnmtCId)
        return list2KokoResponse(result)
    }

//    suspend fun getTnmtPlaysByTnmtCId(tnmtCId: Long): BaseResponse<List<TournamentPlay>> {
//        val result = gameApi.getTnmtPlaysByTnmtCId(tnmtCId)
//        return list2KokoResponse(result)
//    }

    suspend fun getTnmtPlaysByTnmtId(tnmtId: Long): BaseResponse<List<TournamentPlay>> {
        val result = gameApi.getTnmtPlaysByTnmtId(tnmtId)
        return list2KokoResponse(result)
    }

}