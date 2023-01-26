package com.biiiiit.kokonats.ui.user.vm

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.repo.UserBalanceRepo
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.net.KokoNetApi

/**
 * @Author yo_hack
 * @Date 2021.11.30
 * @Description 用户的 energy
 **/
class UserEnergyViewModel : BaseViewModel() {
    private val repo: UserBalanceRepo by lazy { UserBalanceRepo() }


    val energy = MutableLiveData<Long>()

    init {
        energy.value = 0
    }

    /**
     * 刷新 energy
     */
    fun requestEnergy() {
        if (KokoNetApi.token.isNullOrBlank()) {
            return
        }
        request(
            {
                repo.getUserEnergy()
            },
            {
                energy.postValue(it)
            },
            loadingMsg = null,
            error = null,
            hideAction = null
        )
    }

}