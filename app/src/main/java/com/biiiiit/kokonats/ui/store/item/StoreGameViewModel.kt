package com.biiiiit.kokonats.ui.store.item

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.PurchasedGameItem
import com.biiiiit.kokonats.data.repo.StoreRepository
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.base.TAG_LOADING_HIDE
import com.biiiiit.lib_base.base.TAG_LOADING_SHOW
import com.biiiiit.lib_base.utils.logi

/**
 * @Author Lucas Jordan
 * @Date 2022.04.13
 * @Description
 */
class StoreGameViewModel : BaseViewModel() {

    /**
     * 后期考虑 inject
     */
    private val storeRepo: StoreRepository by lazy { StoreRepository() }

    val gameItemsPurchased = MutableLiveData<MutableList<PurchasedGameItem>>()

    fun queryPurchasedGameItems() {
        request(
            {
                storeRepo.getPurchasedGameItems()
            },
            {
                gameItemsPurchased.postValue(it.toMutableList())
            },
            error = {
                gameItemsPurchased.postValue(arrayListOf())
            },
            loadingMsg = null
        )
    }

    fun purchaseGameItem(id: Long) {
        request(
            {
                postLoading(TAG_LOADING_SHOW, null)
                storeRepo.purchaseGameItem(id)
            },
            {
                queryPurchasedGameItems()
                postLoading(TAG_LOADING_HIDE, null)
            },
            error = {
                postLoading(TAG_LOADING_HIDE, null)
            },
            loadingMsg = null
        )
    }
}