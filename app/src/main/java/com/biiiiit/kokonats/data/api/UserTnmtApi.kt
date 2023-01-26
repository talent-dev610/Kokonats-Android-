package com.biiiiit.kokonats.data.api

import com.biiiiit.kokonats.data.bean.GameAuth
import com.biiiiit.kokonats.data.bean.PvpMatchSession
import com.biiiiit.kokonats.data.bean.PvpResultState
import com.biiiiit.kokonats.data.bean.TournamentPlay
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @Author yo_hack
 * @Date 2021.11.04
 * @Description
 **/
interface UserTnmtApi {

    /**
     * 获取全部有效的 tnmt 类型
     */
    @GET("/user/tournament/plays")
    suspend fun getUserTnmtPlays(): List<TournamentPlay>

    /**
     * platform can use this api to generate temporary token for games.
     */
    @POST("/user/game/auth")
    suspend fun getGameAuth(@Body req: Map<String, Long>): GameAuth

    /**
     * 使当前user加入某场tournament，如果成功会返回对应的tournamentPlay，加入失败会返回403
     */
    @POST("/user/tournament/play")
    suspend fun playTournament(@Body req: Map<String, Long>): TournamentPlay

    /**
     * 通过游戏页面发起PVP匹配请求
     * 客户端传gameId发起匹配，服务器返回sessionId，30秒，超过30秒，状态变灰； 现在可用的matchClassId 为1或者2
     */
    @POST("/game/{gameId}/pvp")
    suspend fun getGamePvpSession(@Path("gameId") gameId: Long): PvpMatchSession


    /**
     * 客户端通过比赛配置发起PVP匹配请求
     * 发起匹配请求后，监听响应的sessionId的firebase document,获取相应比赛匹配成功或者失效;当匹配成功后，
     * 需要firebase监听返回的matchPlayId,键值格式为对应matchPlayId_userSubscriber(比如:1_adadadadw)
     */
    @POST("/matchClass/{matchClassId}/session")
    suspend fun getClassPvpSession(@Path("matchClassId") gameId: Long): PvpMatchSession


    /**
     * 查询比赛状态
     *
     */
    @GET("/match/{matchPlayId}")
    suspend fun getPvpResult(@Path("matchPlayId") matchPlayId: Long): PvpResultState
}