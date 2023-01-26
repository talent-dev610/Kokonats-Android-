package com.biiiiit.kokonats.ui.user.vm

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.repo.UserBalanceRepo
import com.biiiiit.lib_base.base.BaseViewModel
import java.math.BigDecimal

/**
 * @Author yo_hack
 * @Date 2021.12.07
 * @Description
 **/
class UserKokoViewModel : BaseViewModel() {

    private val repo: UserBalanceRepo by lazy { UserBalanceRepo() }


    val koko = MutableLiveData<BigDecimal>()

    init {
        koko.value = BigDecimal.ZERO
    }

    /**
     * 刷新 koko
     */
    fun requestKoko() {
        request(
            {
                repo.getUserKoko()
            },
            {
                val cg = BigDecimal(it.confirmed)
                val ucg = BigDecimal(it.unconfirmed)
                val re = cg.plus(ucg)
                koko.postValue(re)
            },
            loadingMsg = null,
            error = null,
            hideAction = null
        )
    }
}