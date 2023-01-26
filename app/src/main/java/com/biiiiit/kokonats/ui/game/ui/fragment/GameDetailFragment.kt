package com.biiiiit.kokonats.ui.game.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commitNow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.Game
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_CIRCLE
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PRACTISE
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PVP
import com.biiiiit.kokonats.databinding.FragmentGameDetailBinding
import com.biiiiit.kokonats.ui.game.ui.GameTnmtCardDecoration
import com.biiiiit.kokonats.ui.game.ui.GameTnmtTabTitleDecoration
import com.biiiiit.kokonats.ui.game.ui.adapter.GameTnmtTabTitleAdapter
import com.biiiiit.kokonats.ui.game.ui.adapter.HomeGameTnmtAdapter
import com.biiiiit.kokonats.ui.game.ui.adapter.HomeGameTnmtKeyAdapter
import com.biiiiit.kokonats.ui.game.viewmodel.GameDetailViewModel
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.kokonats.utils.*
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.data.COMMON_ID
import com.biiiiit.lib_base.data.ResultState
import com.biiiiit.lib_base.net.KOKO_GAME_URL
import com.biiiiit.lib_base.net.SPONSOR_URL
import com.biiiiit.lib_base.user.loginOrAction
import com.biiiiit.lib_base.utils.photo.loadCropRound
import com.biiiiit.lib_base.utils.showMsg
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description game detail fragment
 **/
class GameDetailFragment : BaseFragment<FragmentGameDetailBinding, GameDetailViewModel>() {


    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }

    private var gid: Long = 0

    /**
     * tnmt adapter
     */
    private var tnmtAdapter = HomeGameTnmtAdapter()

    private var tnmtTitleAdapter = GameTnmtTabTitleAdapter()

    private var keyAdapter = HomeGameTnmtKeyAdapter()

    companion object {
        fun newInstance(gid: Long) = GameDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(COMMON_ID, gid)
            }
        }
    }

    override fun beforeOnCreate0() {
        gid = arguments?.getLong(COMMON_ID) ?: gid
    }


    override fun getVMClazz(): Class<GameDetailViewModel> = GameDetailViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGameDetailBinding.inflate(inflater, container, false)

    override fun initView1() {
        binding.headerEnergy.setBackClick { activity?.finish() }

        setCommonRefreshLayout(binding.refreshLayout)
        binding.refreshLayout.autoRefreshAnimationOnly()
        binding.refreshLayout.setOnRefreshListener {
            actionOnce()
        }
        binding.cvSponsor.setOnClickListener(View.OnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SPONSOR_URL)))
        })

        initKeyWordRcv()
        initRcvTnmtTitle()
        initRcvTnmt()
    }

    private fun initKeyWordRcv() {
        binding.rcvKey.adapter = keyAdapter
        binding.rcvKey.layoutManager = FlexboxLayoutManager(context)
    }

    private fun initRcvTnmtTitle() {
        binding.rcvTitle.addItemDecoration(GameTnmtTabTitleDecoration())
        binding.rcvTitle.adapter = tnmtTitleAdapter
        binding.rcvTitle.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        tnmtTitleAdapter.setOnItemClickListener { _, _, pos ->
            vm.tnmtTabPos.postValue(pos)
        }
    }

    private fun initRcvTnmt() {
        tnmtAdapter.requestPlayersCall = {
            vm.queryTnmtPlayers(it)
        }
        binding.rcvTnmt.adapter = tnmtAdapter
        binding.rcvTnmt.addItemDecoration(
            GameTnmtCardDecoration(GameTnmtCardDecoration.TYPE_GAME_TNMT)
        )
        tnmtAdapter.setOnItemClickListener { _, _, position ->
            tnmtAdapter.getItemOrNull(position)?.let {
//                jump2GameTnmtDetail(context, it.id)
                loginOrAction {
                    if (it.type == PLAY_TYPE_PVP) {
                        showFilterDialog(it)
                    } else {
                        jump2GameTnmtDetail(context, it)
                    }
                }
            }
        }
        binding.rcvTnmt.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(binding.rcvTnmt)
    }

    private fun enterTnmtGame(tnmt: TournamentClass) {
        if (tnmt.type == PLAY_TYPE_CIRCLE) {
            // 外面直接禁止 ，因为不知道参与人数
            return
        }
        if (tnmt.type !in arrayOf(PLAY_TYPE_PRACTISE, PLAY_TYPE_PVP)) {
            val status = getMultiTimeStatus(tnmt)
            if (status.first != 0) {
                return
            }
        }

        if (vm.gameDetail.value is ResultState.Success) {
            (vm.gameDetail.value as? ResultState.Success<Game>)?.value?.let { game ->
                val typeInt = getPlayGameTypeByTnmtType(tnmt.type)

                val egFragment =
                    (parentFragmentManager.findFragmentByTag(EnterGameFragment.TAG) as? EnterGameFragment)
                if (egFragment == null || !egFragment.isAdded || egFragment.isDetached) {
                    parentFragmentManager.commitNow(true) {
                        add(
                            EnterGameFragment.newInstance(
                                typeInt, tnmt.gameId, tnmt.id, game.cdnUrl, tnmt.entryFee, tnmt.durationPlaySecond
                            ), EnterGameFragment.TAG
                        )
                    }
                } else {
                    egFragment.enterGame(typeInt, tnmt.gameId, tnmt.id, game.cdnUrl, tnmt.entryFee, tnmt.durationPlaySecond)
                }
            }
        }

    }

    private fun showFilterDialog(tnmt: TournamentClass) {
        showCommonDialog(
            context,
            ShowCommonData(
                R.drawable.ic_v_dialog_info,
                R.string.pvp_enter_content, 0,
                R.string.dialog_common_cancel, R.string.dialog_common_go
            ),
            right = {
                enterTnmtGame(tnmt)
            }
        )
    }

    private fun finishRefresh() {
        binding.refreshLayout.finishRefresh(0)
    }

    override fun initViewModel2() {
        vm.tnmtStr = getString(R.string.tournament)
        vm.pvpStr = getString(R.string.pvp)
        vm.praStr = getString(R.string.practise)


        vm.gameDetail.observe(viewLifecycleOwner) {
            parseState(it,
                { game ->
                    bindGameDetailData(game)
                    finishRefresh()
                },
                {
                    showMsg(it.message)
                    finishRefresh()
                })
        }

        vm.tnmtList.observe(viewLifecycleOwner) {
            tnmtAdapter.setNewInstance(it)
        }

        vm.tnmtTitleList.observe(viewLifecycleOwner) {
            it?.let {
                tnmtTitleAdapter.selectPos = it.first
                tnmtTitleAdapter.setList(it.second)
            }
        }

        energyVM.energy.observe(viewLifecycleOwner) {
            binding.headerEnergy.setNewEnergyLong(it)
        }

    }

    private fun bindGameDetailData(game: Game) {
        binding.tvTitle.text = game.name
        binding.tvDesc.text = game.introduction
        binding.ivCover.loadCropRound(game.screenshot, 9, 0, 0)
        bindKeyWords(game.colorList)
    }

    private fun bindKeyWords(list: MutableList<String>?) {
        keyAdapter.setNewInstance(list)
    }


    override fun actionOnce() {
        vm.queryGameDetail(gid)
        vm.queryGameTnmts(gid)
        energyVM.requestEnergy()
    }


}