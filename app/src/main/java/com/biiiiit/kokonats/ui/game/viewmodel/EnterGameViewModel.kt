package com.biiiiit.kokonats.ui.game.viewmodel

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.bean.EnterGameData
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.repo.*
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.base.TAG_LOADING_HIDE
import com.biiiiit.lib_base.data.KoKoResponse
import com.biiiiit.lib_base.utils.t2KokoResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

/**
 * @Author yo_hack
 * @Date 2021.11.11
 * @Description
 **/
class EnterGameViewModel : BaseViewModel() {
    private val repo = UserTnmtRepository()

    private val gameRepo: GameRepository by lazy {
        GameRepository()
    }


    val enterGameData = MutableLiveData<EnterGameData>()

    val gameEnterFailed = MutableLiveData<Boolean>()

    private var gameUrl: String = ""

    private var lastGameId = 0L

    fun queryGameAuth(type: Int, gameId: Long, tnmtCId: Long, gUrl: String?, durationS:Long) {
        request(
            {
                if (lastGameId != gameId) {
                    // 不缓存这个了 保证单个页面的 gameUrl 是一致的
                    gameUrl = ""
                }

                if (!gUrl.isNullOrBlank()) {
                    gameUrl = gUrl
                }

                var queryGameUrlDef: Deferred<Game?>? = null
                if (gameUrl.isNullOrBlank()) {
                    queryGameUrlDef = it.async {
                        gameRepo.getGameDetail(gameId).getResponseData()
                    }
                }

                var tnmtId = 0L
                var playId = 0L

                // 不是 pvp
                if (type != PLAY_GAME_PVP) {
                    // 首先获取 tnmtId
                    tnmtId = gameRepo.getTnmtByTnmtCId(tnmtCId).getResponseData()?.id!!

                    // 然后获取 playId  保证进入游戏
                    playId = repo.playTournament(tnmtId).getResponseData()?.id ?: 0
                } else {
                    // pvp 可能传 matchClassId 暂定
                    tnmtId = tnmtCId
                }

                // 获取 token 进入游戏 没有使用异步 是想在进入失败的时候获取不调用接口
                val token = repo.getUserGameAuth(gameId).getResponseData()?.token!!

                // 是否有 gameUrl
                gameUrl = if (queryGameUrlDef != null) {
                    queryGameUrlDef.await()?.cdnUrl!!
                } else {
                    gameUrl
                }
                lastGameId = gameId

                t2KokoResponse(EnterGameData(type, token, playId, tnmtId, gameUrl, durationS), null)
            },
            {
                enterGameData.postValue(it)
            },
            error = {
                postLoading(TAG_LOADING_HIDE, null)
                gameEnterFailed.postValue(true)
            }
        )
    }


}