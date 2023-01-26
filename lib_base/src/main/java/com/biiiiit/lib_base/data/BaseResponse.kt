package com.biiiiit.lib_base.data

/**
 * @Author yo_hack
 * @Date 2021.10.15
 * @Description baseResponse
 **/
abstract class BaseResponse<T> {
    abstract fun success(): Boolean

    abstract fun getResponseData(): T?

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String?
}