package com.biiiiit.kokonats.ui.game.viewmodel

import android.content.res.Resources
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.core.text.toSpannable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.data.repo.*
import com.biiiiit.kokonats.ui.game.ui.adapter.GameTnmtTabTitleEntity
import com.biiiiit.kokonats.ui.game.ui.adapter.TYPE_ALL
import com.biiiiit.kokonats.ui.game.ui.adapter.TYPE_NORMAL
import com.biiiiit.kokonats.utils.ColorTxtVo
import com.biiiiit.kokonats.utils.colorSizeTxt
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.utils.loge
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import kotlin.random.Random

/**
 * @Author yo_hack
 * @Date 2021.10.24
 * @Description
 **/
class HomeGameViewModel : BaseViewModel() {

    var all: String = "ALL"
    var new: String = "NEW"


    private var winName = arrayOf(
        "Knight",
        "Thor",
        "ウソトノプ",
        "アナフマ",
        "ウシタロ",
        "サナデ",
        "シトノマモ",
        "アチヌヨ",
        "ククテズン",
        "山本秀也",
        "石田洋二",
        "コーニエル",
        "小林正道",
        "ソロミオン",
        "ローマンド",
        "タンリー",
        "アンソニー",
        "アントム",
        "オリヴェイ",
        "ルドウィン",
        "ギデオドア",
        "ルーファス",
        "マルコリー",
        "ルーパット",
        "セバスター",
        "ディナード",
        "エグバート",
        "レマイモン",
        "コンランク",
        "ノーマンド",
        "ラステッド")

    private var winGold = arrayOf("1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000")


    private var gameRepo = GameRepository()

    /**
     * 获取了多少
     */
    val gameWin = MutableLiveData<CharSequence>()

    /** 选中的 game pos **/
    val gameTabPos = MutableLiveData<Int>()

    private val allGameTitle = mutableListOf<GameTnmtTabTitleEntity>()

    val gameTitleList = Transformations.map(gameTabPos) {
        Pair(it, allGameTitle)
    }

    private val allGameMap = mutableMapOf<String, MutableList<Game>>()

    val gameList = Transformations.map(gameTabPos) {
        allGameMap[allGameTitle.getOrNull(it)?.name]
    }


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


    fun queryGame() {
        val value = gameTabPos.value ?: 0
        val order: String?
        val category: String?
        when (value) {
            0 -> {
                order = GAME_ORDER_ALL
                category = null
            }
            1 -> {
                order = GAME_ORDER_NEW
                category = null
            }
            else -> {
                order = null
                category = allGameTitle.getOrNull(value)?.name
            }
        }
        request(
            {
                gameRepo.getHomeGameList(order, category)
            },
            {
                if (order == GAME_ORDER_ALL) {
                    handleGameTitle(it)
                } else if (order == GAME_ORDER_NEW) {
                    allGameMap[new] = it.toMutableList()
                }
                gameTabPos.postValue(gameTabPos.value ?: 0)
            }
        )
    }

    fun queryTnmt() {
        request(
            {
                gameRepo.getTnmtsClass()
            },
            {
                handleTnmtTitle(it)
                tnmtTabPos.postValue(tnmtTabPos.value ?: 0)
            }
        )
    }

    private fun handleGameTitle(list: List<Game>) {
        val tempTitle = mutableListOf<String>()

        val tempNew = allGameMap[new] ?: mutableListOf()

        allGameTitle.clear()
        allGameMap.clear()


        list.filter { !it.category.isNullOrBlank() }.forEach { game ->
            val c = game.category
            val index = tempTitle.indexOf(c)
            if (index >= 0) {
                allGameMap[c]?.add(game)
            } else {
                tempTitle.add(c)
                allGameMap[c] = mutableListOf(game)
            }
        }

        // 添加所有
        allGameTitle.add(GameTnmtTabTitleEntity(all, TYPE_ALL))
        allGameMap[all] = list.toMutableList()

        // 添加新的
        allGameTitle.add(GameTnmtTabTitleEntity(new, TYPE_NORMAL))
        allGameMap[new] = tempNew

        allGameTitle.addAll(tempTitle.map { GameTnmtTabTitleEntity(it, TYPE_NORMAL) })
    }

    /**
     * 本地化数据 tnmt
     */
    private fun handleTnmtTitle(list: List<TournamentClass>) {
        val tempTitle = mutableListOf<String>()
        allTnmtTitle.clear()
        allTnmtList.clear()

        list.forEach { tnmt ->
            tnmt.tagList.forEach { tagStr ->
                val index = tempTitle.indexOf(tagStr)
                if (index >= 0) {
                    allTnmtList[index].add(tnmt)
                } else {
                    tempTitle.add(tagStr)
                    allTnmtList.add(mutableListOf(tnmt))
                }
            }
        }

        // 添加所有
        allTnmtTitle.add(GameTnmtTabTitleEntity(all, TYPE_ALL))
        allTnmtList.add(0, list.toMutableList())

        allTnmtTitle.addAll(tempTitle.map { GameTnmtTabTitleEntity(it, TYPE_NORMAL) })
    }


    fun generateReward(re: Resources) {
        doJob {

            val keys = MutableList(30) {
                val indexName = Random.nextInt(winName.size)
                val indexWin = Random.nextInt(winGold.size)
                Pair(winName[indexName], winGold[indexWin])
            }
            val result = SpannableStringBuilder()

            keys.forEach { pair ->
                val second = pair.second + "ココ"
                val resultStr = re.getString(R.string.congratulate_win, pair.first, second)
                result.append(colorSizeTxt(resultStr,
                    ColorTxtVo(pair.first, Color.WHITE, 1.1f),
                    ColorTxtVo(second, Color.parseColor("#FFCB00"), 1.1f)))
                result.append("        ")
            }
            gameWin.postValue(result)
        }
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

}