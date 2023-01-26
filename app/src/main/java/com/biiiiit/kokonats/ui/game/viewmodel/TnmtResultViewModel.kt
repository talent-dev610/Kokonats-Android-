package com.biiiiit.kokonats.ui.game.viewmodel

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.biiiiit.kokonats.data.repo.GameRepository
import com.biiiiit.kokonats.data.repo.UserTnmtRepository
import com.biiiiit.lib_base.base.BaseViewModel

/**
 * @Author yo_hack
 * @Date 2021.11.09
 * @Description
 **/
class TnmtResultViewModel : BaseViewModel() {
    private var gameRepo = GameRepository()


    val tnmtPlayList = MutableLiveData<MutableList<TournamentPlay>>()


    fun queryTnmtPlayHistory(tnmtId: Long) {
        request(
            {
                gameRepo.getTnmtPlaysByTnmtId(tnmtId)
            },
            {
                tnmtPlayList.postValue(it.toMutableList())
            },
            error = {
                tnmtPlayList.postValue(mutableListOf())
            },
            loadingMsg = null
        )
    }





}