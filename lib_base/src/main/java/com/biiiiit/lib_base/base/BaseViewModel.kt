package com.biiiiit.lib_base.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biiiiit.lib_base.data.BaseResponse
import com.biiiiit.lib_base.data.ResultState
import com.biiiiit.lib_base.data.parseException
import com.biiiiit.lib_base.data.parseResult
import com.biiiiit.lib_base.net.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * @Author yo_hack
 * @Date 2021.10.14
 * @Description
 **/
open class BaseViewModel : ViewModel() {

    /**
     * ui Loading
     */
    val loadingChange: MutableLiveData<Pair<Int?, String?>> by lazy {
        MutableLiveData<Pair<Int?, String?>>()
    }

    /**
     * for toast errorMsg
     */
    val toastMsg: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    /**
     * request network
     */
    protected fun <T> request(
        query: suspend (CoroutineScope) -> BaseResponse<T>,
        success: (T) -> Unit,
        error: ((BizException) -> Unit)? = {
            // default toast msg
            hideAction?.invoke()
            toastMsg.postValue(it.message)
        },
        loadingMsg: String? = LOADING_TXT,
        hideAction: (() -> Unit)? = {
            // default hide loading
            postLoading(TAG_LOADING_HIDE, loadingMsg)
        }
    ): Job {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
            val newError = handleNetException(e)
            error?.invoke(newError)
        }

        //show loading
        if (loadingMsg.isNullOrBlank().not()) {
            postLoading(TAG_LOADING_SHOW, loadingMsg)
        }

        return viewModelScope.launch(coroutineExceptionHandler) {
            val result = query.invoke(this)

            if (result.success()) {
                hideAction?.invoke()
                val data = result.getResponseData()
                data?.let(success) ?: kotlin.run {
                    throw BizException(NET_NO_DATA, EMPTY_ERROR)
                }
            } else {
                val code = result.getResponseCode()
                val msg = result.getResponseMsg() ?: UNKNOWN_ERROR
                throw BizException(code, msg)
            }
        }

    }

    protected fun <T> request(
        query: suspend (CoroutineScope) -> BaseResponse<T>,
        resultState: MutableLiveData<ResultState<T>>,
        loadingMsg: String? = LOADING_TXT
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (loadingMsg != null) {
                    resultState.value = ResultState.onNetLoading(loadingMsg)
                }
                query(this)
            }.onSuccess {
                resultState.parseResult(it)
            }.onFailure {
                resultState.parseException(it)
            }
        }
    }

    protected fun <T> doJob(
        query: suspend (CoroutineScope) -> T,
        success: (T) -> Unit,
        error: ((BizException) -> Unit)? = {
            // default toast msg
            hideAction?.invoke()
            toastMsg.postValue(it.message)
        },
        loadingMsg: String? = LOADING_TXT,
        hideAction: (() -> Unit)? = {
            // default hide loading
            postLoading(TAG_LOADING_HIDE, loadingMsg)
        }
    ): Job {
        return viewModelScope.launch {
            runCatching {
                query(this)
            }.onSuccess {
                hideAction?.invoke()
                success(it)
            }.onFailure {
                error?.invoke(handleNetException(it))
            }
        }
    }

    protected fun <T> doJob(
        query: suspend (CoroutineScope) -> T,
    ): Job {
        return viewModelScope.launch {
            runCatching {
                query(this)
            }
        }
    }

    protected fun postLoading(tag: Int, msg: String?) {
        loadingChange.postValue(Pair(tag, msg))
    }

}