package com.biiiiit.kokonats.data.api

import com.biiiiit.kokonats.data.bean.CheckInAppPay
import com.biiiiit.kokonats.data.bean.NetInAppProduct
import com.biiiiit.kokonats.data.bean.PurchasedGameItem
import com.biiiiit.kokonats.data.bean.TournamentPlay
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @Author yo_hack
 * @Date 2021.11.05
 * @Description
 **/
interface StoreApi {

    /**
     * 充值
     */
    @POST("/energy/purchase")
    suspend fun uploadPayToken(@Body req: Map<String, String>): CheckInAppPay

    /**
     * 后台的数据
     */
    @GET("/energy/products/google")
    suspend fun getProductList(): List<NetInAppProduct>

    /**
     * get user purchased game items
     */
    @GET("/user/consume/item")
    suspend fun getPurchasedGameItems(): List<PurchasedGameItem>

    /**
     * purchase game item
     */
    @POST("/user/consume/item")
    suspend fun purchaseGameItem(@Body req: Map<String, Long>): Int
}