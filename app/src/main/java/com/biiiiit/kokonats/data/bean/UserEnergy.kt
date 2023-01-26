package com.biiiiit.kokonats.data.bean

import com.android.billingclient.api.SkuDetails
import java.lang.Exception


/**
 * @Author yo_hack
 * @Date 2021.11.26
 * @Description
 **/


class CheckInAppPay {
    var state: Int = 0
    var receipt: String = ""

}

class NetInAppProduct {
    var id: Long = 0
    var productId: String = ""
    var productName: String = ""
    var jpyPrice: Long = 0
    var energy: Long = 0


    @Transient
    var localPrice: String = ""

    @Transient
    var originJson: String = ""


    fun map2Local(): SkuDetails? = try {
        SkuDetails(originJson)
    } catch (e: Exception) {
        null
    }
}

class UserKoko {
    var confirmed: String = ""
    var unconfirmed: String = ""
}