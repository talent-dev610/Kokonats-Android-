package com.biiiiit.kokonats.ui.user.ui

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.ui.user.ui.UserTnmtHistoryFragment
import com.biiiiit.lib_base.base.OneFragmentActivity
import com.biiiiit.lib_base.utils.ROUTE_TNMT_HISTORY

/**
 * @Author yo_hack
 * @Date 2021.11.01
 * @Description 用户 tnmt history
 **/

@Route(path = ROUTE_TNMT_HISTORY)
class UserTnmtHistoryActivity : OneFragmentActivity() {
    override fun createFragment(): Fragment {
        return UserTnmtHistoryFragment.newInstance()
    }
}