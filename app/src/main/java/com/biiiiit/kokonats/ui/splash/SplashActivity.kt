package com.biiiiit.kokonats.ui.splash

import android.view.LayoutInflater
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.kokonats.databinding.ActivitySplashBinding
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.base.EmptyViewModel
import com.biiiiit.lib_base.utils.*

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description splash
 **/
class SplashActivity : BaseActivity<ActivitySplashBinding, EmptyViewModel>() {


    override fun getVMClazz(): Class<EmptyViewModel> = EmptyViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)


    override fun beforeOnCreate0() {
        super.beforeOnCreate0()
        // use level 12 splash core
//        getAny<LoginUser>(SP_LOGIN_USER)?.let {
//            jump2Main(this)
//        } ?: kotlin.run {
//            simpleJump(ROUTE_LOGIN, this)
//        }
        jump2Main(this)
        finish()
    }
}