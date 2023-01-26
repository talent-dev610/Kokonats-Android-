package com.biiiiit.kokonats.data.api

import com.biiiiit.kokonats.data.bean.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author yo_hack
 * @Date 2021.10.24
 * @Description game api
 **/
interface GameApi {

    /**
     * 获取 home game 列表
     * @param order ALL, NEW default is ALL
     */
    @GET("/game/list")
    suspend fun getHomeGameList(
        @Query("order") order: String?,
        @Query("category") category: String?,
    ): List<Game>

    /**
     * 获取 game 详情
     */
    @GET("/game/{gameId}")
    suspend fun getGameDetail(@Path("gameId") gameId: Long): Game

    /**
     * get tournaments by gameId from user
     */
    @GET("/game/{gameId}/tournaments")
    suspend fun getTnmtsByGid(@Path("gameId") gameId: Long): List<TournamentClass>


    /**
     * get matches by gameId
     */
    @GET("/game/{gameId}/matches")
    suspend fun getPvpByGid(@Path("gameId") gameId: Long): List<GameMatch>

    /**
     * get game items by gameId
     */
    @GET("/game/{gameId}/items")
    suspend fun getGameItemsByGid(@Path("gameId") gameId: Long): List<GameItem>

    /**
     * 获取全部有效的 tnmt 类型
     */
    @GET("/tournamentClass")
    suspend fun getTnmtsClass(): List<TournamentClass>

    /**
     * get tournament by tnmt class
     */
    @GET("/tournamentClass/{tnmtCId}/tournament")
    suspend fun getTnmtByTnmtCId(@Path("tnmtCId") tnmtCId: Long): Tournament

    /**
     * 根据tournament class获取全部历史tournament，包括当前有效的
     */
    @GET("/tournamentClass/{tnmtCId}/history")
    suspend fun getTnmtsHistoryByTnmtCId(@Path("tnmtCId") tnmtCId: Long): List<Tournament>
    /    /**
    //     * 获取某个 tnmtCID 下的游戏榜单
    //     */
//    @GET("/tournamentClass/{tnmtCId}/plays")
//    suspend fun getTnmtPlaysByTnmtCId(@Path("tnmtCId") tnmtCId: Long): List<TournamentPlay>

    /**
     * 获取单次tournament的游戏榜单，按照分数倒序
     */
/
    @GET("/tournament/{tnmtId}/plays")
    suspend fun getTnmtPlaysByTnmtId(@Path("tnmtId") tnmtId: Long): List<TournamentPlay>
}