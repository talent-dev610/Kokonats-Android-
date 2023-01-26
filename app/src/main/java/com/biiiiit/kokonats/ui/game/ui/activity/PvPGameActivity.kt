package com.biiiiit.kokonats.ui.game.ui.activity

import android.view.LayoutInflater
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.BuildConfig
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.repo.PLAY_GAME_PVP
import com.biiiiit.kokonats.data.repo.PVP_STATE_MATCHED
import com.biiiiit.kokonats.data.repo.PVP_STATE_TIME_OUT
import com.biiiiit.kokonats.databinding.ActivityPvpGameBinding
import com.biiiiit.kokonats.ui.game.viewmodel.PvPGameViewModel
import com.biiiiit.kokonats.utils.jump2WebGame
import com.biiiiit.kokonats.utils.loadCircleAvatar
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.data.*
import com.biiiiit.lib_base.user.getUser
import com.biiiiit.lib_base.utils.ROUTE_GAME_PVP
import com.biiiiit.lib_base.utils.logi
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * @Author yo_hack
 * @Date 2021.11.07
 * @Description game pvp match
 **/
@Route(path = ROUTE_GAME_PVP)
class PvPGameActivity : BaseActivity<ActivityPvpGameBinding, PvPGameViewModel>() {


    /** game Url **/
    private var gameUrl: String = ""

    /** game Token **/
    private var gameToken: String = ""

    /** game Id**/
    private var gameId: Long = 0

    /** game Id**/
    private var matchClassId: Long = 0

    private var durationS: Long = 0

    /** game Id**/
    private var re: ListenerRegistration? = null

    /** 可以回退 **/
    private var canGoBack = false

    override fun beforeOnCreate0() {
        super.beforeOnCreate0()
        gameUrl = intent.getStringExtra(DATA_0) ?: ""
        gameId = intent.getLongExtra(DATA_1, gameId)
        gameToken = intent.getStringExtra(DATA_2) ?: ""
        matchClassId = intent.getLongExtra(COMMON_DATA, matchClassId)
        durationS = intent.getLongExtra(DATA_3, 0)
    }

    override fun initView1() {
        super.initView1()
        getUser()?.let { it ->
//            binding.ivMineAvatar.loadCircle(it.picture, 0, 0)
            loadCircleAvatar(binding.ivMineAvatar, it.picture, this)
            binding.tvMineName.text = it.userName
        }
    }

    /**
     * 监听结果
     */
    private fun subscribeEvent(sessionId: String) {
        re = Firebase.firestore.collection(BuildConfig.MATCH_SESSION_DOC)
            .document(sessionId)
            .addSnapshotListener(this, MetadataChanges.EXCLUDE) { snapshot, e ->
                " ${snapshot?.data}  ${e?.localizedMessage}".logi("-----")
                val state = snapshot?.getLong("state") ?: return@addSnapshotListener

                when (state.toInt()) {
                    PVP_STATE_MATCHED -> {
                        // 匹配成功
                        val matchPlayId = snapshot.getLong("matchPlayId")
                        if (matchPlayId != null && matchPlayId > 0) {
                            // 匹配完成， 开始游戏
                            val matchId = snapshot.getString("matchId") ?: ""
                            jump2WebGame(
                                this,
                                gameUrl,
                                PLAY_GAME_PVP,
                                gameToken,
                                matchId,
                                matchPlayId,
                                durationS
                            )
                            finish()
                        }
                    }
                    -1, PVP_STATE_TIME_OUT -> {
                        canGoBack = true
                        // 超时
                        showToast(getString(R.string.pvp_matching_failed))
                        vm.cancelInterval()
                    }
                }


            }
    }


    override fun actionOnce() {
        super.actionOnce()
        if (matchClassId > 0) {
            vm.startClassPvpSession(matchClassId)
        } else {
            vm.startGamePvpSession(gameId)
        }
    }

    override fun initViewModel2() {
        vm.textData.observe(this) {
            binding.tvTitleHint.text = it
        }

        vm.pvpSessionData.observe(this) {
            if (it != null && it.sessionId.isNullOrBlank().not()) {
                subscribeEvent(it.sessionId)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        vm.actionInterval()
    }

    override fun onStop() {
        vm.cancelInterval()
        super.onStop()
    }

    override fun onDestroy() {
        re?.remove()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (canGoBack){
            super.onBackPressed()
        }
    }

    override fun getVMClazz(): Class<PvPGameViewModel> =
        PvPGameViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivityPvpGameBinding =
        ActivityPvpGameBinding.inflate(layoutInflater)
}