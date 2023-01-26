package com.biiiiit.kokonats.db

import androidx.room.Entity

/**
 * @Author yo_hack
 * @Date 2021.11.28
 * @Description
 **/
//@Entity(tableName = TAB_APP_TOKEN_NAME, primaryKeys = ["token"])
data class InAppPayToken(
    var uid: Long = 0,

    var token: String = "",

    var skuId: String = "",

    var status: Int = STATUS_NEW
)