package com.biiiiit.kokonats.ui.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.biiiiit.kokonats.data.repo.GAME_ORDER_ALL
import com.biiiiit.kokonats.data.repo.GameRepository
import com.biiiiit.kokonats.data.repo.UserTnmtRepository
import com.biiiiit.lib_base.base.ListViewModel
import com.biiiiit.lib_base.utils.ListDataUiState
import com.biiiiit.lib_base.utils.getList

/**
 * @Author yo_hack
 * @Date 2021.12.01
 * @Description
 **/
class UserTnmtHistoryViewModel : ListViewModel<TournamentPlay>() {

    var ALL = "ALL"

    private val repo by lazy { UserTnmtRepository() }

    private val gameRepo by lazy { GameRepository() }

    private val gameList = mutableListOf<Game>()

    private val allHistoryList = mutableListOf<TournamentPlay>()

    val selectedPos = MutableLiveData<Int>()

    val title = Transformations.map(selectedPos) { pos ->
        val d = data.value
        d?.list?.let { list ->
            if (allHistoryList.isNullOrEmpty()) {
                allHistoryList.clear()
                allHistoryList.addAll(list)
            }
            val a: (MutableList<TournamentPlay>) -> Unit = {
                data.postValue(ListDataUiState(it, d.page, d.nextPage, d.hasMore, lastSize = d.lastSize))
            }

            val pair = initTitlePair().getOrNull(pos)
            if (pos == 0 || pair == null) {
                a(allHistoryList)
                ALL
            } else {
                val l = allHistoryList.filter { it.gameId == pair.first }.toMutableList()
                data.postValue(ListDataUiState(l, d.page, d.nextPage, d.hasMore, lastSize = d.lastSize))
                pair.second
            }

        } ?: kotlin.run {
            ALL
        }
    }

    override suspend fun loadData(
        page: Int,
        pageSize: Int,
        data: MutableLiveData<ListDataUiState<TournamentPlay>>
    ) {
        if (gameList.isNullOrEmpty()) {
            getGameTitle()
        }
        getList(page, pageSize, { _, _ ->
            repo.getUserTnmtPlays()
        }, data)
    }


    /**
     * 初始化 init title
     */
    fun initTitlePair(): MutableList<Pair<Long, String>> {
        val result = mutableListOf<Pair<Long, String>>()
        val list = if (allHistoryList.isNullOrEmpty()) {
            data.value?.list
        } else {
            allHistoryList
        }
        list?.let { list ->
            result.add(Pair(-1L, ALL))

            val cache = mutableSetOf<Long>()
            list.forEach {
                val gameId = it.gameId
                if (!cache.contains(gameId)) {
                    val name = gameList.find { it.id == gameId }?.name ?: it.getRealName()
                    cache.add(gameId)
                    result.add(it.gameId to name)
                }
            }
        }
        return result
    }

    fun getGameTitle() {
        request(
            {
                gameRepo.getHomeGameList(GAME_ORDER_ALL, null)
            },
            {
                gameList.clear()
                gameList.addAll(it)
            }, {}
        )
    }


}