package com.biiiiit.kokonats.data.api

import com.biiiiit.kokonats.data.bean.UserKoko
import retrofit2.http.GET

/**
 * @Author yo_hack
 * @Date 2021.11.26
 * @Description
 **/
interface UserEnergyApi {

    /**
     * 获取用户 energy
     */
    @GET("/energy/balance")
    suspend fun getUserEnergy(): Long

    /**
     * 获取用户 koko
     */
    @GET("/wallet/balance")
    suspend fun getUserKoko(): UserKoko

}