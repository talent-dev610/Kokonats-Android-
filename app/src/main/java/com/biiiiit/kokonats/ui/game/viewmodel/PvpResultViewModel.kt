package com.biiiiit.kokonats.ui.game.viewmodel

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.bean.PvpResultState
import com.biiiiit.kokonats.data.repo.UserTnmtRepository
import com.biiiiit.lib_base.base.BaseViewModel

/**
 * @Author yo_hack
 * @Date 2022.01.13
 * @Description pvp result viewModel
 **/
class PvpResultViewModel : BaseViewModel() {
    private var tnmtRepo = UserTnmtRepository()


    val data = MutableLiveData<PvpResultState>()

    fun queryPvpHistory(playId: Long) {
        request(
            {
                tnmtRepo.getPvpResult(playId)
            },
            {
                data.postValue(it)
            },
            loadingMsg = null
        )
    }
}