package com.biiiiit.kokonats.ui.game.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.commitNow
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TnmtPayout
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_CIRCLE
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PRACTISE
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PVP
import com.biiiiit.kokonats.databinding.FragmentGameTnmtDetailBinding
import com.biiiiit.kokonats.ui.game.ui.adapter.TnmtPlayPayoutAdapter
import com.biiiiit.kokonats.ui.game.ui.adapter.TnmtPlayRankAdapter
import com.biiiiit.kokonats.ui.game.viewmodel.GameTnmtDetailViewModel
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.kokonats.ui.user.vm.UserInfoViewModel
import com.biiiiit.kokonats.utils.*
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.data.COMMON_DATA
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.net.SPONSOR_URL
import com.biiiiit.lib_base.user.loginOrAction
import com.biiiiit.lib_base.utils.*
import com.biiiiit.lib_base.utils.photo.loadCropRound
import com.google.gson.Gson
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description tnmt 详情页
 **/
class GameTnmtDetailFragment :
    BaseFragment<FragmentGameTnmtDetailBinding, GameTnmtDetailViewModel>() {

    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }

    private val userVM: UserInfoViewModel by lazy {
        getAppViewModel(UserInfoViewModel::class.java)
    }

    //    private var id: Long = 0
    private var tnmtClass: TournamentClass? = null

    private val payoutAdapter = TnmtPlayPayoutAdapter()


    private val rankAdapter = TnmtPlayRankAdapter()

    private var hasEnter = true

    private var timer: CountDownTimer? = null

    companion object {
        fun newInstance(tnmtClass: TournamentClass?) = GameTnmtDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(COMMON_DATA, tnmtClass)
            }
        }
    }

    override fun beforeOnCreate0() {
//        id = arguments?.getLong(COMMON_ID) ?: id
        tnmtClass = arguments?.getSerializable(COMMON_DATA) as? TournamentClass
    }

    override fun initView1() {
        binding.headerEnergy.setBackClick { activity?.finish() }
        setCommonRefreshLayout(binding.refreshLayout)
        binding.refreshLayout.setOnRefreshListener {
            actionOnce()
        }
        binding.llPlay.setOnClickListener {
            loginOrAction {
                enterTnmtGame()
            }
        }
        binding.cvSponsor.setOnClickListener(View.OnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SPONSOR_URL)))
        })
        binding.ivSponsorCopy.setOnClickListener(View.OnClickListener {
            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("koko_sponsor_url", SPONSOR_URL)
            clipboardManager.setPrimaryClip(clipData)
            showToast("URL copied to clipboard")
        })
        initRcvPayout()
        initRcvRank()
        tnmtClass?.let { bindTournament(it) }
    }


    private fun enterTnmtGame() {
        tnmtClass?.let { tnmt ->
            if (tnmt.type == PLAY_TYPE_CIRCLE) {
                vm.playersCount.value?.let {
                    // 人数满园咯...
                    if (it.first >= it.second) {
                        return
                    }
                }
            }
            if (tnmt.type !in arrayOf(PLAY_TYPE_PRACTISE, PLAY_TYPE_PVP)) {
                val status = getMultiTimeStatus(tnmt)
                if (status.first != 0) {
                    return@let
                }
            }

            val gUrl = vm.gameDetail.value?.cdnUrl ?: ""
            hasEnter = true
            val typeInt = getPlayGameTypeByTnmtType(tnmt.type)
            val egFragment =
                (parentFragmentManager.findFragmentByTag(EnterGameFragment.TAG) as? EnterGameFragment)
            if (egFragment == null || !egFragment.isAdded || egFragment.isDetached) {
                if (tnmt.entryFee > 0) {
                    showCommonDialogRes(context,
                        ShowCommonDataRes(
                            R.drawable.ic_v_dialog_question,
                            R.string.enter_tournament,
                            getString(R.string.use_energy_tournament, tnmt.entryFee),
                            R.string.back,
                            R.string.dialog_common_ok
                        ), {}) {
                        parentFragmentManager.commitNow(true) {
                            add(
                                EnterGameFragment.newInstance(
                                    typeInt,
                                    tnmt.gameId,
                                    tnmt.id,
                                    gUrl,
                                    tnmt.entryFee,
                                    tnmt.durationPlaySecond
                                ), EnterGameFragment.TAG
                            )
                        }
                    }
                } else {
                    parentFragmentManager.commitNow(true) {
                        add(
                            EnterGameFragment.newInstance(
                                typeInt,
                                tnmt.gameId,
                                tnmt.id,
                                gUrl,
                                tnmt.entryFee,
                                tnmt.durationPlaySecond
                            ), EnterGameFragment.TAG
                        )
                    }
                }

            } else {
                if (tnmt.entryFee > 0) {
                    showCommonDialogRes(context,
                        ShowCommonDataRes(
                            R.drawable.ic_v_dialog_question,
                            R.string.enter_tournament,
                            getString(R.string.use_energy_tournament, tnmt.entryFee),
                            R.string.back,
                            R.string.dialog_common_ok
                        ), {}) {
                        egFragment.enterGame(
                            typeInt,
                            tnmt.gameId,
                            tnmt.id,
                            gUrl,
                            tnmt.entryFee,
                            tnmt.durationPlaySecond
                        )
                    }
                } else {
                    egFragment.enterGame(
                        typeInt, tnmt.gameId, tnmt.id, gUrl, tnmt.entryFee, tnmt.durationPlaySecond
                    )
                }
            }
        }
    }

    private fun initRcvPayout() {
        binding.rcvRule.layoutManager = LinearLayoutManager(context)
        binding.rcvRule.adapter = payoutAdapter
    }

    private fun initRcvRank() {
        binding.rcvRank.layoutManager = LinearLayoutManager(context)
        binding.rcvRank.adapter = rankAdapter
    }


    override fun actionOnce() {
//        vm.queryTnmtDetail(id)
        vm.queryGameDetail(tnmtClass?.gameId ?: 0)
        energyVM.requestEnergy()
        userVM.queryUserInfo()
    }

    override fun actionAlways() {
        super.actionAlways()
        if (hasEnter) {
            vm.queryTnmtPlayHistory(tnmtClass?.id ?: 1)
            hasEnter = false
        }
        tnmtClass?.let { bindTournament(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initViewModel2() {

//        vm.tournament.observe(viewLifecycleOwner) {
//            it?.let { bindTournament(it) }
//        }

        vm.tnmtPlayList.observe(viewLifecycleOwner) {
            val isNotEmpty = !it.isNullOrEmpty()
            if (isNotEmpty) {
                binding.refreshLayout.finishRefresh(0)
                rankAdapter.setNewInstance(it)
            }
            binding.rcvRank.isVisible = isNotEmpty
            binding.tvHistoryHint.isVisible = isNotEmpty
            setPlayButton(it)
        }

        energyVM.energy.observe(viewLifecycleOwner) {
            binding.headerEnergy.setNewEnergyLong(it)
        }

        vm.gameDetail.observe(viewLifecycleOwner) {
            val intro = it?.introduction
            binding.tvDesc.isVisible = intro.isNullOrBlank().not()
            binding.tvDesc.text = intro
        }

        // ==0 时 显示满员状态
        /*if (tnmtClass?.type == PLAY_TYPE_CIRCLE) {
            vm.playersCount.observe(viewLifecycleOwner) {
                it?.let {
                    setBtnStatusByPair(
                        Pair(
                            if (it.first >= it.second) {
                                2
                            } else {
                                0
                            }, ""
                        )
                    )
                }
            }
        }*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPlayButton(players: MutableList<TournamentPlay>) {
        when (tnmtClass?.type) {
            0 -> setPlayButtonType0(players)
            3 -> setPlayButtonType3()
            else -> vm.playersCount.observe(viewLifecycleOwner) {
                it?.let {
                    setBtnStatusByPair(
                        Pair(
                            if (it.first >= it.second) {
                                2
                            } else {
                                0
                            }, ""
                        )
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPlayButtonType0(players: MutableList<TournamentPlay>) {
        binding.llPlay.setBackgroundResource(R.drawable.gradient_common_blue)
        if (KokoNetApi.token.isNullOrBlank()) {
            binding.tvPlay.text = getString(R.string.enter_now)
        } else {
            userVM.loginUser.observe(viewLifecycleOwner) {
                val player = players.findLast { tournamentPlay -> tournamentPlay.userName == it.userName }
                if (player == null) {
                    binding.tvPlay.text = getString(R.string.enter_now)
                } else {
                    binding.tvPlay.text = getString(R.string.enter_again)
                }
            }
        }
        val time = LocalTime.now()
        var startTime = LocalTime.parse(tnmtClass?.startTime)
        while (startTime.isBefore(time)) {
            tnmtClass?.durationSecond?.let { startTime = startTime.plusSeconds(it) }
        }
        val diff = time.until(startTime, ChronoUnit.SECONDS)
        setTimer(diff)
    }

    private fun setTimer(duration: Long) {
        timer = object: CountDownTimer(duration * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.tvTimer.visibility = View.VISIBLE
                    binding.tvTimer.text = DateUtils.formatElapsedTime(millisUntilFinished / 1000)
                }
                override fun onFinish() {
                    tnmtClass?.durationSecond?.let { setTimer(it) }
                }
            }.start()
    }

    private fun setPlayButtonType3() {
        binding.llPlay.setBackgroundResource(R.drawable.gradient_common_blue)
        binding.tvPlay.text = getString(R.string.enter_now)
        binding.tvTimer.visibility = View.GONE
    }

    private fun bindTournament(tournament: TournamentClass) {
        binding.ivCover.loadCropRound(tournament.coverImageUrl, 9, 0, 0)

        val ranks: MutableList<TnmtPayout>? = Gson().fromJson(tournament.rankingPayout)
        if (ranks.isNullOrEmpty()) {
            binding.rcvRule.isVisible = false
            binding.tvRewardHint.isVisible = false
        } else {
            binding.rcvRule.isVisible = true
            binding.tvRewardHint.isVisible = true
            payoutAdapter.setDiffNewData(ranks)
        }

        val statusPair = getMultiTimeStatus(tournament)
        //setBtnStatusByPair(statusPair)
    }

    private fun setBtnStatusByPair(pair: Pair<Int, String>) {
        val bg: Int
        val txt: String
        when (pair.first) {
            -1 -> {
                bg = R.drawable.rect9_black323755
                txt = pair.second
            }
            0 -> {
                bg = R.drawable.gradient_common_blue
                txt = getString(R.string.play_now)
            }
            2 -> {
                bg = R.drawable.rect9_black323755
                txt = getString(R.string.game_full)
            }
            else -> {
                bg = R.drawable.rect9_black323755
                txt = getString(R.string.game_ended)
            }
        }
        binding.llPlay.setBackgroundResource(bg)
        binding.tvPlay.text = txt
    }

    override fun onDestroyView() {
        binding.rcvRank.adapter = null
        if (timer != null) {
            timer?.cancel()
        }
        super.onDestroyView()
    }

    override fun getVMClazz(): Class<GameTnmtDetailViewModel> = GameTnmtDetailViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGameTnmtDetailBinding.inflate(inflater, container, false)

}