package com.biiiiit.lib_base.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biiiiit.lib_base.utils.COMMON_PAGE_SIZE
import com.biiiiit.lib_base.utils.COMMON_PAGE_START
import com.biiiiit.lib_base.utils.ListDataUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @Author yo_hack
 * @Description
 **/
abstract class ListViewModel<T> : BaseViewModel() {

    var page: Int = COMMON_PAGE_START
    var pageSize: Int = COMMON_PAGE_SIZE

    var data = MutableLiveData<ListDataUiState<T>>()


    fun loadList(isRefresh: Boolean): Job {
        page = if (isRefresh) {
            COMMON_PAGE_START
        } else {
            data.value?.nextPage ?: page
        }

        return viewModelScope.launch {
            loadData(page, pageSize, data)
        }
    }

    protected abstract suspend fun loadData(
        page: Int,
        pageSize: Int,
        data: MutableLiveData<ListDataUiState<T>>
    )

}