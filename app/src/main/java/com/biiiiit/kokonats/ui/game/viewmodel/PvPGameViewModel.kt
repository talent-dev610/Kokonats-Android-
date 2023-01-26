package com.biiiiit.kokonats.ui.game.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.PvpMatchSession
import com.biiiiit.kokonats.data.repo.GameRepository
import com.biiiiit.kokonats.data.repo.UserTnmtRepository
import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.data.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Author yo_hack
 * @Date 2021.11.07
 * @Description pvp game matching
 **/
class PvPGameViewModel : BaseViewModel() {

    val textData = MutableLiveData<String>()

    private val txt = BaseApp.app.resources.getString(R.string.match_ing)

    private var intervalJob: Job? = null

    private var tnmtRepo = UserTnmtRepository()


    /**
     * pvp match session
     */
    var pvpSessionData = MutableLiveData<PvpMatchSession>()

    /**
     * start pvp session
     */
    fun startGamePvpSession(gameId: Long) {
        dealWithPvpQuery { tnmtRepo.getGamePvpSession(gameId) }
    }

    /**
     * start classId session
     */
    fun startClassPvpSession(classId: Long) {
        dealWithPvpQuery { tnmtRepo.getClassPvpSession(classId) }
    }

    private fun dealWithPvpQuery(query: suspend (CoroutineScope) -> BaseResponse<PvpMatchSession>) {
        request(query,
            {
                pvpSessionData.postValue(it)
            })
    }

    fun actionInterval() {
        intervalJob = viewModelScope.launch {
            repeat(200) {
                delay(900)
                textData.postValue(txt + ".".repeat(1 + it % 3))
            }
        }
    }

    fun cancelInterval() {
        intervalJob?.cancel()
    }


    override fun onCleared() {
        intervalJob?.cancel()
        super.onCleared()
    }

}