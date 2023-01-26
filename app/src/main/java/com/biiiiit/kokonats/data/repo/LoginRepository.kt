package com.biiiiit.kokonats.data.repo

import com.biiiiit.kokonats.data.api.LoginApi
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.base.BizException
import com.biiiiit.lib_base.data.BaseResponse
import com.biiiiit.lib_base.data.KoKoResponse
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.net.NET_ERROR_UNKNOWN
import com.biiiiit.lib_base.net.NET_SUCCESS
import com.biiiiit.lib_base.net.UNKNOWN_ERROR

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description
 **/
class LoginRepository {

    private val loginApi: LoginApi by lazy { KokoNetApi.INSTANCE.create(LoginApi::class.java) }


    suspend fun signInThird(path: String, idToken: String): BaseResponse<LoginUser> {
        val result = loginApi.signInGoogle(path, mapOf("idToken" to idToken))
        if (result.id > 0) {
            return KoKoResponse(NET_SUCCESS, "", result)
        } else {
            throw BizException(NET_ERROR_UNKNOWN, UNKNOWN_ERROR)
        }
    }

    suspend fun signInFirebaseApple(idToken: String): BaseResponse<LoginUser> {
        val result = loginApi.signInFirebaseApple(mapOf("idToken" to idToken))
        if (result.id > 0) {
            return KoKoResponse(NET_SUCCESS, "", result)
        } else {
            throw BizException(NET_ERROR_UNKNOWN, UNKNOWN_ERROR)
        }
    }
}