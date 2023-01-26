package com.biiiiit.kokonats.data.bean

import com.android.billingclient.api.SkuDetails
import java.lang.Exception

/**
 * @Author yo_hack
 * @Date 2021.11.05
 * @Description
 **/

class StoreEnergy {

    companion object {
        fun map2Local(skuDetails: SkuDetails): StoreEnergy {
            return StoreEnergy().apply {
                price = skuDetails.price
                originPrice = skuDetails.originalPrice
                iconUrl = skuDetails.iconUrl
                title = skuDetails.title

                originalJson = skuDetails.originalJson
            }
        }

    }

    var title: String = ""

    var price: String = ""

    var originPrice: String = ""

    var iconUrl: String = ""

    var originalJson: String = ""


    fun map2Local(): SkuDetails? = try {
        SkuDetails(originalJson)
    } catch (e: Exception) {
        null
    }
}