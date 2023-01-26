package com.biiiiit.kokonats.ui.game.ui.activity

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.ui.game.ui.fragment.GameTnmtDetailFragment
import com.biiiiit.lib_base.base.OneFragmentActivity
import com.biiiiit.lib_base.data.COMMON_DATA
import com.biiiiit.lib_base.data.COMMON_ID
import com.biiiiit.lib_base.utils.ROUTE_GAME_TNMT_DETAIL

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description
 **/
@Route(path = ROUTE_GAME_TNMT_DETAIL)
class GameTnmtActivity : OneFragmentActivity() {
    override fun createFragment(): Fragment {
        val tnmtClass =  intent.getSerializableExtra(COMMON_DATA) as? TournamentClass
//        val gid = intent.getLongExtra(COMMON_ID, 0)
//        if (gid == 0L) {
//            finish()
//        }
        if (tnmtClass == null) {
            finish()
        }
        return GameTnmtDetailFragment.newInstance(tnmtClass)
    }
}