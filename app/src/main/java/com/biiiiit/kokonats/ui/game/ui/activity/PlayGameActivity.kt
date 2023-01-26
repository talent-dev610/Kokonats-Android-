package com.biiiiit.kokonats.ui.game.ui.activity

import android.view.LayoutInflater
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.BuildConfig
import com.biiiiit.kokonats.data.repo.PLAY_GAME_PVP
import com.biiiiit.kokonats.data.repo.PLAY_GAME_UNKNOWN
import com.biiiiit.kokonats.databinding.ActivityPlayGameBinding
import com.biiiiit.kokonats.ui.game.viewmodel.PlayGameViewModel
import com.biiiiit.kokonats.utils.getPlayGameStrByPgt
import com.biiiiit.kokonats.utils.jump2TnmtResult
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.data.*
import com.biiiiit.lib_base.user.getUser
import com.biiiiit.lib_base.utils.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * @Author yo_hack
 * @Date 2021.11.11
 * @Description  ?type=tournament&gameAuth=token&gameId=?
 **/
@Route(path = ROUTE_PLAY_GAME)
class PlayGameActivity : BaseActivity<ActivityPlayGameBinding, PlayGameViewModel>() {
    // url
    private var url: String = ""

    // game Url
    private var gameUrl: String = ""

    // game type
    private var typeInt: Int = PLAY_GAME_UNKNOWN

    // game token
    private var gameToken: String = ""

    // tnmtId or matchId
    private var tnmtId: String = ""

    // playId
    private var playId: Long = 0

    private var durationSecond: Long = 0

    private var enableSubscribe = false

    private var re: ListenerRegistration? = null

    override fun beforeOnCreate0() {
        gameUrl = intent.getStringExtra(DATA_0) ?: ""
        typeInt = intent.getIntExtra(DATA_1, PLAY_GAME_UNKNOWN)
        gameToken = intent.getStringExtra(DATA_2) ?: ""
        tnmtId = intent.getStringExtra(COMMON_DATA) ?: ""
        playId = intent.getLongExtra(COMMON_ID, 0)
        durationSecond = intent.getLongExtra(DATA_3, 10)


        val first = if (gameUrl.contains("?")) {
            "&"
        } else {
            "?"
        }
        val typeIdStr = if (typeInt == PLAY_GAME_PVP) {
            "matchPlayId"
        } else {
            "tournamentPlayId"
        }
        url =
            "${gameUrl}${first}playType=${getPlayGameStrByPgt(typeInt)}&token=${gameToken}&${typeIdStr}=${playId}&durationSecond=$durationSecond"
        url.logi("---------")
    }


//    class MsgType {
//        var msgType: String = ""
//        var data: Map<String, Any>? = null
//    }

    override fun initView1() {
        super.initView1()

//        binding.webView.addJavascriptInterface(object : Any() {
//
//            // 可针对用于扩展事件
//            @JavascriptInterface
//            fun postMessage(str: String) {
//                str.logi("------")
//                val tupe = Gson().fromJson<MsgType>(str)
//                tupe?.let {
//                    it.msgType.logi("----")
//                    handlePostMessage(it)
//                }
//            }
//
//        }, "messageHandlers")

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                newProgress.toString().logi("------")
                if (newProgress > 90) {
                    enableSubscribe = true
                }
            }


        }

        setDefaultWebSettings(binding.webView, object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//                if (request?.url.toString().contains( "https://game.kokonats.club/")){
//                     binding.webView.loadUrl(request?.url.toString())
//                    return true
//                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                error?.description.toString().loge("-----")
            }

        })
        binding.webView.loadUrl(url)

//        subscribeEvent()
    }

    override fun onStart() {
        super.onStart()
        subscribeEvent()
    }


    /**
     * 通过 fireStore 监听事件
     */
    private fun subscribeEvent() {
        val subscriber = getUser()?.subscriber

        val isPvp = typeInt == PLAY_GAME_PVP
        // 监听事件
        re = Firebase.firestore.collection(
            if (isPvp) {
                BuildConfig.MATCH_PLAY_DOC
            } else {
                BuildConfig.TOURNAMENT_PLAY_DOC
            }
        )
            .document("${playId}_${subscriber}")
            .addSnapshotListener(this, MetadataChanges.EXCLUDE) { snapshot, e ->
                "${snapshot?.metadata?.isFromCache}   ${snapshot?.data}  ${e?.code}   ${e?.localizedMessage}".logi(
                    "-----"
                )
                // 允许订阅 && snapshot 存在  &&  不是缓存
                if (enableSubscribe &&
                    snapshot?.exists() == true &&
                    !snapshot.metadata.isFromCache
                ) {
                    handleTnmtSnapShot(snapshot)
                }
            }
    }


    /**
     * handle tnmt snapshot
     */
    private fun handleTnmtSnapShot(snapshot: DocumentSnapshot) {
        val state = snapshot.getLong("state")


        if (state == 1L) {
            // 结束游戏
            val score = snapshot.getLong("score")

            val time = snapshot.getDate("timestamp")?.time ?: 0
            val curTime = System.currentTimeMillis()

            if (typeInt != PLAY_GAME_PVP && curTime - time > 10 * 1000) {
                return
            }
            // 只接受 10s 内的修改的判断
            val id = if (typeInt == PLAY_GAME_PVP) {
                playId
            } else {
                tnmtId.toLongOrNull() ?: 0L
            }
            jump2TnmtResult(this, typeInt, id, score.toString())
            finish()
        }
    }


    /**
     * 处理 post msg
     */
//    private fun handlePostMessage(msgType: MsgType) {
//        runOnUiThread {
//            when (msgType.msgType) {
//                "endGame" -> {
//                    finish()
//                    jump2TnmtResult(
//                            this,
//                            tnmtPlayId,
//                            (msgType.data?.get("score") as? String) ?: "0"
//                    )
//                }
//            }
//        }
//
//    }


    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        re?.remove()
        binding.webView.destroy()
        super.onDestroy()
    }

    private var lastClickTime = 0L

    // 防止误触退出游戏
    override fun onBackPressed() {
        val cur = System.currentTimeMillis()
        if (cur - lastClickTime < 3000) {
            super.onBackPressed()
        }
        lastClickTime = cur
    }

    override fun getVMClazz(): Class<PlayGameViewModel> =
        PlayGameViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivityPlayGameBinding =
        ActivityPlayGameBinding.inflate(layoutInflater)
}