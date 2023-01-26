package com.biiiiit.kokonats.ui.game.ui.fragment

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.repo.PLAY_GAME_PVP
import com.biiiiit.kokonats.data.repo.PLAY_GAME_UNKNOWN
import com.biiiiit.kokonats.ui.game.viewmodel.EnterGameViewModel
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.kokonats.utils.ShowCommonData
import com.biiiiit.kokonats.utils.jump2PvpGame
import com.biiiiit.kokonats.utils.jump2WebGame
import com.biiiiit.kokonats.utils.showCommonDialog
import com.biiiiit.lib_base.base.AbsFragment
import com.biiiiit.lib_base.data.*
import com.biiiiit.lib_base.user.loginOrAction
import com.biiiiit.lib_base.utils.jump2Main
import com.biiiiit.lib_base.utils.loge
import com.biiiiit.lib_base.utils.showMsg

/**
 * @Author yo_hack
 * @Date 2021.11.11
 * @Description 进入游戏的 fragment 无 ui
 **/
class EnterGameFragment : AbsFragment() {


    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }

    /**
     * 进入游戏的部分参数
     */
    private var type: Int = PLAY_GAME_UNKNOWN
    private var gameId: Long = 0
    private var tnmtCId: Long = 0
    private var gameUrl: String = ""
    private var gameFee: Long = 0
    private var durationS: Long = 0

    private var hasEnter = false
    private val vm: EnterGameViewModel by lazy {
        ViewModelProvider(this).get(EnterGameViewModel::class.java)
    }
//    private lateinit var vm: EnterGameViewModel

    companion object {
        const val TAG = "TAG_ENTER_GAME"

        fun newInstance(
            type: Int,
            gId: Long,
            tnmtCId: Long,
            gUrl: String?,
            fee: Long,
            durationS: Long
        ) =
            EnterGameFragment().apply {
                arguments = Bundle().apply {
                    putString(DATA_0, gUrl)
                    putInt(DATA_1, type)
                    putLong(COMMON_DATA, gId)
                    putLong(COMMON_ID, tnmtCId)
                    putLong(DATA_2, fee)
                    putLong(DATA_3, durationS)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            gameUrl = it.getString(DATA_0) ?: gameUrl
            type = it.getInt(DATA_1, PLAY_GAME_UNKNOWN)
            gameId = it.getLong(COMMON_DATA, 0)
            tnmtCId = it.getLong(COMMON_ID, 0)
            gameFee = it.getLong(DATA_2, 0)
            durationS = it.getLong(DATA_3, 0)
        }

//        vm = ViewModelProvider(this).get(EnterGameViewModel::class.java)
        super.onCreate(savedInstanceState)
        initViewModel()
        hasEnter = savedInstanceState?.getBoolean(COMMON_DATA) ?: hasEnter
        if (!hasEnter) {
            filterAndQueryGameAuth()
            hasEnter = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(COMMON_DATA, hasEnter)
    }

    override fun getLOwner(): LifecycleOwner = this

    private fun initViewModel() {
        registerCommonVm(vm)
        vm.enterGameData.observe(this) {
            if (it != null && !it.token.isNullOrBlank()) {
//                jump2PvpGame(context, it.gameUrl, gameId, it.token, it.tnmtId)
                if (it.type == PLAY_GAME_PVP) {
                    jump2PvpGame(context, it.gameUrl, gameId, it.token, it.tnmtId, it.durationS)
                } else {
                    jump2WebGame(
                        context,
                        it.gameUrl,
                        type,
                        it.token,
                        it.tnmtId.toString(),
                        it.playId,
                        it.durationS
                    )
                }
                vm.enterGameData.postValue(null)
            }
        }
        vm.gameEnterFailed.observe(this) {
            if (it == true) {
                // 出错出错
                showMsg(getString(R.string.enter_game_failed))
                vm.gameEnterFailed.value = null
            }
        }
    }

    fun enterGame(
        typeInt: Int?,
        gId: Long?,
        tId: Long?,
        gUrl: String?,
        fee: Long,
        durationS: Long
    ) {
        if ((gId ?: 0) <= 0 || (tId ?: 0) <= 0) {
            return
        }
        type = typeInt ?: PLAY_GAME_UNKNOWN
        gameId = gId!!
        tnmtCId = tId!!
        gameUrl = gUrl ?: ""
        gameFee = fee
        this.durationS = durationS

        filterAndQueryGameAuth()
    }

    /**
     * 拦截 energy
     */
    private fun filterAndQueryGameAuth() {
        if (energyVM.energy.value ?: 0 < gameFee) {
            "$gameFee  ${energyVM.energy.value}".loge("-----")
            showCommonDialog(context,
                ShowCommonData(
                    R.drawable.ic_v_dialog_x,
                    R.string.dialog_common_head,
                    R.string.play_energy_not_enough,
                    R.string.dialog_common_cancel,
                    R.string.play_energy_right
                ), {}, {
                    loginOrAction {
                        jump2Main(context, 0)
                    }

                })
        } else {
            vm.queryGameAuth(type, gameId, tnmtCId, gameUrl, durationS)
        }
    }
}