package com.biiiiit.kokonats.ui.game.viewmodel

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.biiiiit.kokonats.data.repo.GameRepository
import com.biiiiit.lib_base.base.BaseViewModel

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description
 **/
class GameTnmtDetailViewModel : BaseViewModel() {
    private var gameRepo = GameRepository()

//    val tournament = MutableLiveData<Tournament>()

    val tnmtPlayList = MutableLiveData<MutableList<TournamentPlay>>()


    val playersCount: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    val gameDetail = MutableLiveData<Game>()

//    fun queryTnmtDetail(tnmtCId: Long) {
//        request(
//            {
//                gameRepo.getTnmtByTnmtCId(tnmtCId)
//            },
//            {
//                tournament.postValue(it)
//            }
//        )
//    }

    fun queryTnmtPlayHistory(tnmtCId: Long) {
        request(
            {
                // 查找有效的 tournament
                var id = runCatching {
                    val tnmt = gameRepo.getTnmtByTnmtCId(tnmtCId).getResponseData()
                    tnmt?.apply {
                        playersCount.postValue(tnmt.joinPlayersCount to tnmt.participantNumber)
                    }
                    tnmt?.id
                }.getOrNull()


                // 获取不到有效的 就获取最进的
                if (id ?: 0 <= 0) {
                    id = gameRepo.getTnmtsHistoryByTnmtCId(tnmtCId, 1)
                        .getResponseData()?.getOrNull(0)?.id
                } else {
                    // 有有效 id
                }

                gameRepo.getTnmtPlaysByTnmtId(id!!)
            },
            {
                tnmtPlayList.postValue(it.toMutableList())
            },
            error = {
                tnmtPlayList.postValue(mutableListOf())
            },
            loadingMsg = null,
            hideAction = null
        )
    }

    fun queryGameDetail(gid:Long){
        request({
            gameRepo.getGameDetail(gid)
        },{
            gameDetail.postValue(it)
        },null)
    }
}