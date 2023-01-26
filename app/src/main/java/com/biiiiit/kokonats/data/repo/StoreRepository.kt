package com.biiiiit.kokonats.data.repo

import com.biiiiit.kokonats.data.api.StoreApi
import com.biiiiit.kokonats.data.bean.CheckInAppPay
import com.biiiiit.kokonats.data.bean.NetInAppProduct
import com.biiiiit.kokonats.data.bean.PurchasedGameItem
import com.biiiiit.lib_base.data.BaseResponse
import com.biiiiit.lib_base.data.KoKoResponse
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.net.NET_SUCCESS
import com.biiiiit.lib_base.utils.list2KokoResponse
import com.biiiiit.lib_base.utils.t2KokoResponse

/**
 * @Author yo_hack
 * @Date 2021.11.05
 * @Description
 **/
class StoreRepository {

    private val storeApi: StoreApi by lazy {
        KokoNetApi.INSTANCE.create(StoreApi::class.java)
    }

    suspend fun uploadPayToken(purchase: String, skuId: String): BaseResponse<CheckInAppPay> {
        val result = storeApi.uploadPayToken(
            mapOf(
                "receipt" to purchase,
                "productId" to skuId,
                "platform" to "google",
            )
        )
        return KoKoResponse(NET_SUCCESS, "", result)
    }

    suspend fun getBackProductList(): BaseResponse<List<NetInAppProduct>> {
        val result = storeApi.getProductList()
        var products = mutableListOf<NetInAppProduct>()
        for (i in 0..9) {
            val product = NetInAppProduct()
            product.productId = "$i"
            product.productName = "Energy Product $i"
            product.energy = (20 * (i + 1)).toLong()
            product.jpyPrice = (121 * (i + 1)).toLong()
            product.localPrice = "${120 * (i + 1)}"
            products.add(product)
        }
        return list2KokoResponse(result)
        //return list2KokoResponse(products)
    }

    suspend fun getPurchasedGameItems(): BaseResponse<List<PurchasedGameItem>> {
        val result = storeApi.getPurchasedGameItems().map { it.map2Item() }
        return list2KokoResponse(result)
    }

    suspend fun purchaseGameItem(id: Long): BaseResponse<Int> {
        val result = storeApi.purchaseGameItem(mapOf("gameItemId" to id))
        return t2KokoResponse(result,null)
    }
}