package com.biiiiit.lib_base.utils

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.biiiiit.lib_base.base.BizException
import com.biiiiit.lib_base.data.BaseResponse
import com.biiiiit.lib_base.net.EMPTY_ERROR
import com.biiiiit.lib_base.net.NET_NO_DATA
import com.biiiiit.lib_base.net.UNKNOWN_ERROR
import com.biiiiit.lib_base.net.handleNetException
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**
 * @Author yo_hack
 * @Date 2021.08.19
 * @Description 适用于list 相关列表 分理出通用方法，和 fragment 或 activity 去除耦合
 **/

/**
 * 没有返回 total 数据
 */
const val LIST_NO_TOTAL = -1

/**
 * page start
 */
const val COMMON_PAGE_START = 1

const val COMMON_PAGE_SIZE = 20


class ListDataUiState<T>(
    /** 总的 list 数据 **/
    var list: MutableList<T>,
    /** 当前页码 **/
    var page: Int,
    /**下一页**/
    var nextPage: Int,
    /** 是否有更多的数据 **/
    var hasMore: Boolean = false,
    /** 数据总条数 当 total == -1 时 没有条数这个字断 **/
    var total: Int = -LIST_NO_TOTAL,
    /** 数据总页数 当 totalPage == -1 时， 没有页数这个字断 **/
    var totalPage: Int = -LIST_NO_TOTAL,
    /** 上一次的 size **/
    var lastSize: Int,
    /** 错误信息 仅出现当 list 为空的时候的错误信息 **/
    var exception: BizException? = null
) {

    fun isFirstPage() = page == COMMON_PAGE_START

    fun isEmpty() = list.size <= 0

}

/**
 * 获取列表信息转换
 */
suspend fun <T> getList(
    page: Int,
    pageSize: Int,
    block: suspend (Int, Int) -> BaseResponse<List<T>>,
    data: MutableLiveData<ListDataUiState<T>>
) {
    runCatching {
        block(page, pageSize)
    }.onSuccess {
        if (it.success()) {
            // 过滤后台错误数据
            val responseData = it.getResponseData()?.filter {
                it != null
            }

            if (responseData.isNullOrEmpty()) {
                commonFailure(data, page, null, null, false, BizException(NET_NO_DATA, EMPTY_ERROR))
            } else {
                val next = page + 1
                val list = mutableListOf<T>()

                val lastList = data.value?.list
                if (page != COMMON_PAGE_START && !lastList.isNullOrEmpty()) {
                    list.addAll(lastList)
                }
                val lastSize = list.size

                val hasMore = responseData.size > COMMON_PAGE_SIZE / 2
                list.addAll(responseData)
                data.postValue(ListDataUiState(list, page, next, hasMore, lastSize = lastSize))
            }
        } else {
            commonFailure(
                data, page,
                throwable = BizException(it.getResponseCode(), it.getResponseMsg() ?: UNKNOWN_ERROR)
            )
        }
    }.onFailure {
        commonFailure(data, page, throwable = it)
    }
}

/**
 * 通用的 failure
 */
fun <T> commonFailure(
    data: MutableLiveData<ListDataUiState<T>>,
    page: Int,
    total: Int? = null,
    totalPage: Int? = null,
    hasMore: Boolean? = null,
    throwable: Throwable
) {
    val beforeList = if (page == COMMON_PAGE_START) {
        mutableListOf()
    } else {
        data.value?.list ?: mutableListOf()
    }
    val beforeTotal = total ?: data.value?.total ?: LIST_NO_TOTAL
    val beforePage = totalPage ?: data.value?.totalPage ?: LIST_NO_TOTAL
    val more = hasMore ?: data.value?.hasMore ?: false
    data.postValue(
        ListDataUiState(
            beforeList, page, page, more, beforeTotal, beforePage, data.value?.list?.size ?: 0,
            handleNetException(throwable)
        )
    )
}

/**
 * @param refreshLayout 刷新的布局
 * @param contentView 显示正确图的布局
 * @param errorView 显示错误图的布局
 * @param adapter 适配器
 * @param data 封装的数据
 * @param errorBlock 错误模块处理
 */
fun <T> setListObserver(
    refreshLayout: RefreshLayout?,
    contentView: View?,
    errorView: View?,
    adapter: BaseQuickAdapter<T, *>,
    data: ListDataUiState<T>,
    errorBlock: ((Int, String) -> Unit)?,
) {
    var contentVisible = true

    if (data.exception == null && !data.list.isNullOrEmpty()) {
        // 有数据
        adapter.setNewInstance(data.list)

        if (data.isFirstPage()) {
            refreshLayout?.finishRefresh(true)
            refreshLayout?.setNoMoreData(true)
        } else {
            // 是否是第一页
            refreshLayout?.finishLoadMore(true)
            if (data.hasMore) {
                // 有更多数据
                refreshLayout?.setNoMoreData(true)
            } else {
                refreshLayout?.setNoMoreData(false)
            }
        }
    } else {
        if (data.isFirstPage()) {
            // 处于第一页没更多数据 清空老的数据
            contentVisible = false
            adapter.setNewInstance(data.list)

            refreshLayout?.finishRefresh()
            refreshLayout?.setNoMoreData(true)

            errorBlock?.invoke(
                data.exception?.code ?: NET_NO_DATA,
                data.exception?.message ?: EMPTY_ERROR
            )
        } else {
            refreshLayout?.finishLoadMore(false)
            refreshLayout?.setNoMoreData(data.hasMore)
        }
    }
    contentView?.isVisible = contentVisible
    errorView?.isVisible = !contentVisible
}


