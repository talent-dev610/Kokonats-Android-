package com.biiiiit.kokonats.data.repo

import com.biiiiit.kokonats.data.api.UserEnergyApi
import com.biiiiit.kokonats.data.bean.UserKoko
import com.biiiiit.lib_base.data.KoKoResponse
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.net.NET_SUCCESS

/**
 * @Author yo_hack
 * @Date 2021.11.26
 * @Description
 **/
class UserBalanceRepo {
    private val energyApi: UserEnergyApi by lazy {
        KokoNetApi.INSTANCE.create(UserEnergyApi::class.java)
    }

    suspend fun getUserEnergy(): KoKoResponse<Long> {
        val result = energyApi.getUserEnergy()
        return KoKoResponse(NET_SUCCESS, "", result)
    }

    suspend fun getUserKoko(): KoKoResponse<UserKoko> {
        val result = energyApi.getUserKoko()
        return KoKoResponse(NET_SUCCESS, "", result)
    }

}