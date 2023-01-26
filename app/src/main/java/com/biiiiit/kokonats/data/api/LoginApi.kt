package com.biiiiit.kokonats.data.api

import com.biiiiit.lib_base.data.LoginUser
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description
 **/
interface LoginApi {

    @POST("/auth/{path}/login")
    suspend fun signInGoogle(
        @Path(value = "path") path: String,
        @Body req: Map<String, String>
    ): LoginUser


    @POST("/auth/firebase/apple/login")
    suspend fun signInFirebaseApple(
        @Body req: Map<String, String>
    ): LoginUser
}