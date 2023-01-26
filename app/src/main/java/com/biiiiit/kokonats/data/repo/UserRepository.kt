package com.biiiiit.kokonats.data.repo

import com.biiiiit.kokonats.data.api.UserApi
import com.biiiiit.lib_base.base.BizException
import com.biiiiit.lib_base.data.KoKoResponse
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.net.NET_ERROR_UNKNOWN
import com.biiiiit.lib_base.net.NET_SUCCESS
import com.biiiiit.lib_base.net.UNKNOWN_ERROR
import com.biiiiit.lib_base.user.saveUser

/**
 * @Author yo_hack
 * @Date 2021.10.23
 * @Description
 **/
class UserRepository {
    private val userApi: UserApi by lazy {
        KokoNetApi.INSTANCE.create(UserApi::class.java)
    }

    suspend fun getUserInfo(): KoKoResponse<LoginUser> {
        val result = userApi.getUserInfo()
        if (result.id > 0) {
            saveUser(result)
            return KoKoResponse(NET_SUCCESS, "", result)
        } else {
            throw BizException(NET_ERROR_UNKNOWN, UNKNOWN_ERROR)
        }
    }

    suspend fun changeUserInfo(userName: String, picture: String): KoKoResponse<LoginUser> {
        val result = userApi.changeUserInfo(mapOf(
            "picture" to picture,
            "userName" to userName
        ))
        if (result.id > 0) {
            saveUser(result)
            return KoKoResponse(NET_SUCCESS, "", result)
        } else {
            throw BizException(NET_ERROR_UNKNOWN, UNKNOWN_ERROR)
        }
    }
}