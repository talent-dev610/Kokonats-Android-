package com.biiiiit.lib_base.data

import androidx.lifecycle.MutableLiveData
import com.biiiiit.lib_base.base.BizException
import com.biiiiit.lib_base.net.EMPTY_ERROR
import com.biiiiit.lib_base.net.NET_NO_DATA
import com.biiiiit.lib_base.net.UNKNOWN_ERROR
import com.biiiiit.lib_base.net.handleNetException


/**
 * @Author yo_hack
 * @Date 2020.09.15
 * @Description 网络请求返回
 **/
sealed class ResultState<out T> {

    companion object {
        /**
         * @param loadingMsg null 不作为   empty 或者显示刷新   text 显示text
         */
        fun <T> onNetLoading(loadingMsg: String?): ResultState<T> = Loading(loadingMsg)

        fun <T> onNetSuccess(data: T): ResultState<T> = Success(data)

        fun <T> onNetFailure(exception: BizException): ResultState<T> = Failure(exception)
    }

    data class Loading(val loadingMsg: String?) : ResultState<Nothing>()

    data class Success<out T>(val value: T) : ResultState<T>()

    data class Failure(val exception: BizException) : ResultState<Nothing>()

}


/**
 * 处理返回结果
 */
fun <T> MutableLiveData<ResultState<T>>.parseResult(result: BaseResponse<T>) {
    value = if (result.success()) {
        if (result.getResponseData() != null) {
            ResultState.onNetSuccess(result.getResponseData()!!)
        } else {
            ResultState.onNetFailure(BizException(NET_NO_DATA, EMPTY_ERROR))
        }
    } else {
        ResultState.onNetFailure(
            handleNetException(
                BizException(
                    result.getResponseCode(), result.getResponseMsg() ?: UNKNOWN_ERROR
                )
            )
        )
    }
}

/**
 * 不处理返回结果
 */
fun <T> MutableLiveData<ResultState<T>>.parseResult(result: T?) {
    value = if (result == null) {
        ResultState.onNetFailure(BizException(NET_NO_DATA, EMPTY_ERROR))
    } else {
        ResultState.onNetSuccess(result)
    }
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.parseException(e: Throwable) {
    value = ResultState.onNetFailure(handleNetException(e))
}
