package com.biiiiit.lib_base.data

import com.biiiiit.lib_base.net.NET_SUCCESS

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description
 **/
class KoKoResponse<T>(val code: Int, val msg: String, var data: T?) : BaseResponse<T>() {
    override fun getResponseData(): T? = data
    override fun getResponseCode(): Int = code
    override fun getResponseMsg(): String = msg
    override fun success(): Boolean = code == NET_SUCCESS
}