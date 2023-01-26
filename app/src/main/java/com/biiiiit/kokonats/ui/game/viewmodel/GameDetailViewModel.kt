package com.biiiiit.kokonats.ui.game.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.bean.GameItem
import com.biiiiit.kokonats.data.bean.PurchasedGameItem
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.data.repo.GameRepository
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PRACTISE
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PVP
import com.biiiiit.kokonats.ui.game.ui.adapter.GameTnmtTabTitleEntity
import com.biiiiit.kokonats.ui.game.ui.adapter.TYPE_NORMAL
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.data.ResultState
import com.biiiiit.lib_base.utils.loge
import com.biiiiit.lib_base.utils.logi
import com.biiiiit.lib_base.utils.t2KokoResponse
import kotlinx.coroutines.async

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description
 **/
class GameDetailViewModel : BaseViewModel() {


    var tnmtStr: String = "Tournament"
    var pvpStr: String = "PVP"
    var praStr: String = "Practise"


    private var gameRepo = GameRepository()

    val gameDetail = MutableLiveData<ResultState<Game>>()

    val gameItems = MutableLiveData<MutableList<GameItem>>()

    val gameItemsPurchased = MutableLiveData<MutableList<PurchasedGameItem>>()

    /** 选中的 tnmt pos **/
    val tnmtTabPos = MutableLiveData<Int>()

    /** 所有的tnmt title **/
    private val allTnmtTitle = mutableListOf<GameTnmtTabTitleEntity>()

    /** tnmt title **/
    val tnmtTitleList = Transformations.map(tnmtTabPos) {
        Pair(it, allTnmtTitle)
    }

    /** 数据 **/
    private val allTnmtList = mutableListOf<MutableList<TournamentClass>>()

    /** tnmt detail list **/
    val tnmtList = Transformations.map(tnmtTabPos) {
        allTnmtList.getOrNull(it)
    }

    fun queryGameDetail(gid: Long) {
        request(
            {
                gameRepo.getGameDetail(gid)
            },
            gameDetail,
            null
        )
    }

    fun queryGameItems(gid: Long) {
        request(
            {
                gameRepo.getGameItemsByGid(gid)
            },
            {
                gameItems.postValue(it.toMutableList())
            },
            error = {
                gameItems.postValue(arrayListOf())
            }
        )
    }

    fun queryGameTnmts(gid: Long) {
        request(
            {
                val defTnmt = it.async {
                    try {
                        gameRepo.getTnmtsByGid(gid)
                    } catch (e: Exception) {
                        t2KokoResponse(emptyList(), null)
                    }
                }

                val defMatch = it.async {
                    try {
                        gameRepo.getMatchByGid(gid)
                    } catch (e: Exception) {
                        e.message?.logi()
                        t2KokoResponse(emptyList(), null)
                    }
                }

                val list = mutableListOf<TournamentClass>()
                defTnmt.await().getResponseData()?.let {
                    list.addAll(it)
                }
                defMatch.await().getResponseData()?.let {
                    list.addAll(it)
                }
                t2KokoResponse(list, null)
            },
            {
                handleTnmtTitle(it)
                tnmtTabPos.postValue(tnmtTabPos.value ?: 0)
            },
            loadingMsg = null
        )

    }


    fun queryTnmtPlayers(cId: Long) {
        request({
            gameRepo.getTnmtByTnmtCId(cId)
        }, { tnmt ->
            allTnmtList.forEachIndexed { index, item ->
                val find = item.find { it.id == cId && it.type != PLAY_TYPE_PVP }
                if (find != null) {
                    find.players = tnmt.newestJoinPlayers
                    allTnmtList[index] = allTnmtList[index].toMutableList()
                }
            }

            tnmtTabPos.postValue(tnmtTabPos.value ?: 0)
        }, {
            allTnmtList.forEachIndexed { index, item ->
                val find = item.find { it.id == cId && it.type != PLAY_TYPE_PVP }
                if (find != null) {
                    find.players = mutableListOf()
                    allTnmtList[index] = allTnmtList[index].toMutableList()
                }
            }
            tnmtTabPos.postValue(tnmtTabPos.value ?: 0)
        }, loadingMsg = null)
    }


    /**
     * 本地化数据 tnmt
     */
    private fun handleTnmtTitle(list: List<TournamentClass>) {
        allTnmtTitle.clear()
        allTnmtList.clear()

        val list0 = mutableListOf<TournamentClass>()
        val list1 = mutableListOf<TournamentClass>()
        val list2 = mutableListOf<TournamentClass>()

        list.forEach { tnmt ->
            when (tnmt.type) {
                PLAY_TYPE_PRACTISE -> list2.add(tnmt)
                PLAY_TYPE_PVP -> list1.add(tnmt)
                else -> list0.add(tnmt)
            }
        }

        if (!list0.isNullOrEmpty()) {
            allTnmtTitle.add(GameTnmtTabTitleEntity(tnmtStr, TYPE_NORMAL))
            allTnmtList.add(list0)
        }


        if (!list1.isNullOrEmpty()) {
            allTnmtTitle.add(GameTnmtTabTitleEntity(pvpStr, TYPE_NORMAL))
            allTnmtList.add(list1)
        }


        if (!list2.isNullOrEmpty()) {
            allTnmtTitle.add(GameTnmtTabTitleEntity(praStr, TYPE_NORMAL))
            allTnmtList.add(list2)
        }


    }
}