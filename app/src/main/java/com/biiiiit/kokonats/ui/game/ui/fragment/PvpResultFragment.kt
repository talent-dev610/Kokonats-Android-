package com.biiiiit.kokonats.ui.game.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.FragmentPvpResultBinding
import com.biiiiit.kokonats.ui.game.ui.adapter.PvpResultAdapter
import com.biiiiit.kokonats.ui.game.viewmodel.PvpResultViewModel
import com.biiiiit.lib_base.base.BaseFragment
import com.biiiiit.lib_base.data.COMMON_DATA
import com.biiiiit.lib_base.data.COMMON_ID

/**
 * @Author yo_hack
 * @Date 2022.01.13
 * @Description pvp detail activity
 **/
class PvpResultFragment : BaseFragment<FragmentPvpResultBinding, PvpResultViewModel>() {


    private var score: String = ""

    /**
     * tnmt --> tnmtId
     * pvp --> matchPlayId
     */
    private var id = 0L

    private var adapter = PvpResultAdapter()

    companion object {
        fun newInstance(id: Long, score: String) = PvpResultFragment().apply {
            arguments = Bundle().apply {
                putLong(COMMON_ID, id)
                putString(COMMON_DATA, score)
            }
        }
    }

    override fun beforeOnCreate0() {
        arguments?.let {
            score = it.getString(COMMON_DATA, "")
            id = it.getLong(COMMON_ID, 0)
        }
    }


    override fun initView1() {
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.postDelayed({ binding.refreshLayout.finishRefresh() }, 10000)
            actionAlways()
        }
        binding.rcvScore.layoutManager = LinearLayoutManager(context)
        binding.rcvScore.adapter = adapter

        binding.tvBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun actionAlways() {
        vm.queryPvpHistory(id)
    }

    override fun initViewModel2() {
        vm.data.observe(viewLifecycleOwner) {
            binding.refreshLayout.finishRefresh()
            it?.let {
                val gameFinish = it.players?.all { it.state == 1 }
                if (gameFinish == true) { // 结束
                    showRcv(true)
                    adapter.myStatus = it.result ?: ""
                    adapter.setNewInstance(it.players?.toMutableList())
                } else { // 进行中
                    showRcv(false)
                }
                binding.tvScore.setText(when (it.result) {
                    "W" -> R.string.game_win
                    "L" -> R.string.game_lost
                    "D" -> R.string.game_draw
                    else -> if (gameFinish == true) {
                        R.string.waiting
                    } else {
                        R.string.game_playing
                    }
                })
            }
        }
    }

    private fun showRcv(flag: Boolean) {
        binding.tvNotFinish.isVisible = !flag
        binding.rcvScore.isVisible = flag
    }


    override fun getVMClazz(): Class<PvpResultViewModel> =
        PvpResultViewModel::class.java

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPvpResultBinding.inflate(layoutInflater, container, false)
}