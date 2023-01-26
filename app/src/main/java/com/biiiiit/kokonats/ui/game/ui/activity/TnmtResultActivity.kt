package com.biiiiit.kokonats.ui.game.ui.activity

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.data.repo.PLAY_GAME_PVP
import com.biiiiit.kokonats.ui.game.ui.fragment.PvpResultFragment
import com.biiiiit.kokonats.ui.game.ui.fragment.TnmtResultFragment
import com.biiiiit.lib_base.base.OneFragmentActivity
import com.biiiiit.lib_base.data.COMMON_DATA
import com.biiiiit.lib_base.data.COMMON_ID
import com.biiiiit.lib_base.data.DATA_0
import com.biiiiit.lib_base.utils.ROUTE_GAME_TNMT_RESULT

/**
 * @Author yo_hack
 * @Date 2021.11.09
 * @Description tnmt detail activity
 **/
@Route(path = ROUTE_GAME_TNMT_RESULT)
class TnmtResultActivity : OneFragmentActivity() {
    override fun createFragment(): Fragment {
        val score = intent.getStringExtra(COMMON_DATA) ?: ""
        val id = intent.getLongExtra(COMMON_ID, 0L)
        val typeInt = intent.getIntExtra(DATA_0, 0)
        return if (typeInt == PLAY_GAME_PVP) {
            PvpResultFragment.newInstance(id, score)
        } else {
            TnmtResultFragment.newInstance(id, score)
        }
    }

}