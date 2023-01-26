package com.biiiiit.kokonats.data.api

import com.biiiiit.lib_base.data.LoginUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @Author yo_hack
 * @Date 2021.10.23
 * @Description
 **/
interface UserApi {

    /**
     * 获取用户信息
     */
    @GET("/user/info")
    suspend fun getUserInfo(): LoginUser

    @POST("/user/info")
    suspend fun changeUserInfo(@Body map: Map<String, String>): LoginUser
}