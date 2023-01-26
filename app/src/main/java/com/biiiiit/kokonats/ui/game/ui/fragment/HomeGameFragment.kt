package com.biiiiit.kokonats.ui.game.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.commitNow
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_CIRCLE
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PRACTISE
import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PVP
import com.biiiiit.kokonats.databinding.FragmentHomeGameBinding
import com.biiiiit.kokonats.ui.game.ui.GameTnmtCardDecoration
import com.biiiiit.kokonats.ui.game.ui.GameTnmtTabTitleDecoration
import com.biiiiit.kokonats.ui.game.ui.adapter.GameTnmtTabTitleAdapter
import com.biiiiit.kokonats.ui.game.ui.adapter.HomeGameGameAdapter
import com.biiiiit.kokonats.ui.game.ui.adapter.HomeGameTnmtAdapter
import com.biiiiit.kokonats.ui.game.viewmodel.HomeGameViewModel
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.kokonats.utils.getMultiTimeStatus
import com.biiiiit.kokonats.utils.getPlayGameTypeByTnmtType
import com.biiiiit.kokonats.utils.jump2GameDetail
import com.biiiiit.kokonats.utils.jump2GameTnmtDetail
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.user.loginOrAction
import com.biiiiit.lib_base.utils.ROUTE_CHAT_MAIN
import com.biiiiit.lib_base.utils.simpleJump
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

/**
 * @Author yo_hack
 * @Date 2021.10.24
 * @Description home game fragment
 **/
class HomeGameFragment : BaseFragment<FragmentHomeGameBinding, HomeGameViewModel>() {
    override fun getVMClazz(): Class<HomeGameViewModel> = HomeGameViewModel::class.java

    override val vm: HomeGameViewModel
        get() = ViewModelProvider(requireActivity()).get(getVMClazz())

    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }


    /**
     * game adapter
     */
    private var gameTitleAdapter = GameTnmtTabTitleAdapter()
    private var gameAdapter = HomeGameGameAdapter()

    /**
     * tnmt adapter
     */
    private var tnmtTitleAdapter = GameTnmtTabTitleAdapter()
    private var tnmtAdapter = HomeGameTnmtAdapter()


    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentHomeGameBinding =
        FragmentHomeGameBinding.inflate(layoutInflater, container, false)


    override fun initView1() {
        initRcvTnmtTitle()
        initRcvTnmt()
        initRcvGameTitle()
        initRcvGame()
        binding.btnChat.setOnClickListener {
            loginOrAction {
                simpleJump(ROUTE_CHAT_MAIN, context)
            }
        }
    }

    private fun initRcvGameTitle() {
        commonTabTitleRcv(
            binding.rcvGameTitle, gameTitleAdapter, GameTnmtTabTitleDecoration()
        ) { _, _, pos ->
            vm.gameTabPos.value = pos
            if (pos == 0 || pos == 1) {
                vm.queryGame()
            }
        }
    }

    private fun initRcvGame() {
        commonContentRcv(
            binding.rcvGame,
            gameAdapter,
            GameTnmtCardDecoration(GameTnmtCardDecoration.TYPE_TAB_GAME)
        ) { _, _, position ->
            gameAdapter.getItemOrNull(position)?.let {
                jump2GameDetail(context, it.id)
            }
        }
    }

    private fun initRcvTnmtTitle() {
        commonTabTitleRcv(
            binding.rcvTnmtTitle, tnmtTitleAdapter, GameTnmtTabTitleDecoration()
        ) { _, _, pos ->
            vm.tnmtTabPos.postValue(pos)
            if (pos == 0) {
                vm.queryTnmt()
            }
        }
    }

    private fun initRcvTnmt() {
        tnmtAdapter.requestPlayersCall = { vm.queryTnmtPlayers(it) }
        commonContentRcv(
            binding.rcvTnmt,
            tnmtAdapter,
            GameTnmtCardDecoration(GameTnmtCardDecoration.TYPE_TAB_TNMT)
        ) { _, _, position ->
            tnmtAdapter.getItemOrNull(position)?.let {
                jump2GameTnmtDetail(context, it)
            }

        }
        tnmtAdapter.addChildClickViewIds(R.id.iv_play)
        tnmtAdapter.setOnItemChildClickListener { _, _, position ->
            tnmtAdapter.getItemOrNull(position)?.let {
                loginOrAction {
                    enterTnmtGame(it)
                }
            }
        }
    }

    private fun commonTabTitleRcv(
        rcv: RecyclerView,
        adapter: BaseQuickAdapter<*, *>,
        decoration: RecyclerView.ItemDecoration,
        listener: OnItemClickListener?
    ) {
        rcv.addItemDecoration(decoration)
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter.setOnItemClickListener(listener)
    }

    private fun commonContentRcv(
        rcv: RecyclerView,
        adapter: BaseQuickAdapter<*, *>,
        decoration: RecyclerView.ItemDecoration,
        listener: OnItemClickListener?
    ) {
        commonTabTitleRcv(rcv, adapter, decoration, listener)
        PagerSnapHelper().attachToRecyclerView(rcv)
//        rcv.enforceSingleScrollDirection()
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
        val typeInt = getPlayGameTypeByTnmtType(tnmt.type)
        val egFragment =
            (parentFragmentManager.findFragmentByTag(EnterGameFragment.TAG) as? EnterGameFragment)
        if (egFragment == null || !egFragment.isAdded || egFragment.isDetached) {
            parentFragmentManager.commitNow(true) {
                add(
                    EnterGameFragment.newInstance(
                        typeInt, tnmt.gameId, tnmt.id, "", tnmt.entryFee, tnmt.durationPlaySecond
                    ), EnterGameFragment.TAG
                )
            }
        } else {
            egFragment.enterGame(
                typeInt, tnmt.gameId, tnmt.id, "", tnmt.entryFee, tnmt.durationPlaySecond
            )
        }
    }

    override fun initViewModel2() {
        vm.all = getString(R.string.all)
        vm.new = getString(R.string.new_caps)
        vm.gameList.observe(viewLifecycleOwner) {
            gameAdapter.setNewInstance(it)
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

        vm.gameTitleList.observe(viewLifecycleOwner) {
            it?.let {
                gameTitleAdapter.selectPos = it.first
                gameTitleAdapter.setList(it.second)
            }
        }

        energyVM.energy.observe(viewLifecycleOwner) {
            binding.energyView.setNewEnergyLong(it)
        }

        vm.gameWin.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvCongratulations.text = it
                binding.tvCongratulations.isSelected = true
            }
        }

    }

    override fun actionOnce() {
        vm.queryTnmt()
        vm.queryGame()
        vm.generateReward(resources)
    }

    override fun actionAlways() {
        energyVM.requestEnergy()
    }

    override fun onDestroyView() {
        binding.rcvGameTitle.adapter = null
        binding.rcvGame.adapter = null
        binding.rcvTnmtTitle.adapter = null
        binding.rcvTnmt.adapter = null
        super.onDestroyView()
    }

}