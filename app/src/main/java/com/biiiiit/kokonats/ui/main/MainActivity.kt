package com.biiiiit.kokonats.ui.main


import android.app.Dialog
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateMargins
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.ActivityMainBinding
import com.biiiiit.kokonats.databinding.DialogTutorialBinding
import com.biiiiit.kokonats.ui.main.tutorial.OnGetStarted
import com.biiiiit.kokonats.ui.main.tutorial.TutorialAdapter
import com.biiiiit.kokonats.ui.user.vm.UserEnergyViewModel
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.data.COMMON_TAB_INDEX
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.user.loginOrAction
import com.biiiiit.lib_base.utils.*
import com.imuxuan.floatingview.FloatingMagnetView
import com.imuxuan.floatingview.FloatingView
import com.imuxuan.floatingview.MagnetViewListener


@Route(path = ROUTE_MAIN)
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), OnGetStarted {

    private lateinit var tutorialDialog: Dialog

    private val energyVM: UserEnergyViewModel by lazy {
        getAppViewModel(UserEnergyViewModel::class.java)
    }

    private var navController: NavController? = null

    override fun getVMClazz(): Class<MainViewModel> = MainViewModel::class.java

    override fun createBinding(layoutInflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun initView1() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController?.let { controller ->
            NavigationUI.setupWithNavController(binding.navView, controller)
            // 拦截一下下
            binding.navView.setOnNavigationItemSelectedListener {
                val result = when (it.itemId) {
                    R.id.navigation_mine -> !KokoNetApi.token.isNullOrBlank()
                    else -> true
                }
                if (result) {
                    NavigationUI.onNavDestinationSelected(it, controller)
                } else {
                    loginOrAction { }
                }
                result
            }
        }
	initEasyFloat()
        val alreadyPlayed = getBoolean(SP_ALREADY_PLAYED)
        if (!alreadyPlayed) {
            showTutorial()
        }
    }
    
    /**
     * easy float
     */
    private fun initEasyFloat() {

        FloatingView.get()
            .icon(R.drawable.ic_v_chat_main)
            .layoutParams(
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.BOTTOM or Gravity.END
                    updateMargins(
                        right = dp2px(this@MainActivity, 0),
                        bottom = dp2px(this@MainActivity, 80)
                    )
                    marginEnd =
                        marginEnd
                }
            )
            .add()
        FloatingView.get().listener(object : MagnetViewListener {
            override fun onRemove(magnetView: FloatingMagnetView) {
            }

            override fun onClick(magnetView: FloatingMagnetView) {
                loginOrAction {
                    simpleJump(ROUTE_CHAT_MAIN, this@MainActivity)
                }
            }
        })

//        EasyFloat.with(this.applicationContext)
//            .setLayout(R.layout.float_window_chat)
//            .setDragEnable(true)
//            .setGravity(Gravity.BOTTOM or Gravity.END, -dp2px(this, 30), -dp2px(this, 80))
//            .setShowPattern(ShowPattern.FOREGROUND)
//            .setSidePattern(SidePattern.DEFAULT)
//            .setFilter(ChatMainActivity::class.java, LoginActivity::class.java)
//            .registerCallback {
//                this.touchEvent { _, motionEvent ->
//                    if (motionEvent.action == MotionEvent.ACTION_UP) {
//                        loginOrAction {
//                            simpleJump(ROUTE_CHAT_MAIN, this@MainActivity)
//                        }
//                    }
//                }
//            }
//            .show()
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val index = intent?.getIntExtra(COMMON_TAB_INDEX, 1)
        navController?.navigate(
            when (index) {
                0 -> R.id.navigation_store
                2 -> R.id.navigation_mine
                else -> R.id.navigation_game
            }
        )
    }

    private fun showTutorial() {
        tutorialDialog = Dialog(this, R.style.common_dialog)
        val rootBinding = DialogTutorialBinding.inflate(LayoutInflater.from(this))
        tutorialDialog.setContentView(rootBinding.root)
        tutorialDialog.setCancelable(false)
        tutorialDialog.setCanceledOnTouchOutside(false)
        tutorialDialog.window?.let { window ->
            val lp = window.attributes
            lp.width = (getScreenWidth(this) * 0.8).toInt()
            lp.height = (getScreenHeight(this) * 0.8).toInt()
        }


        rootBinding.vpIntro.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                rootBinding.ivNext.visibility = View.VISIBLE
                rootBinding.ivPrev.visibility = View.VISIBLE
                if (position == 0) {
                    rootBinding.ivPrev.visibility = View.INVISIBLE
                } else if (position == 2) {
                    rootBinding.ivNext.visibility = View.INVISIBLE
                }
                rootBinding.tvStep.text = "${position + 1} / 3"
            }
        })
        rootBinding.vpIntro.adapter = TutorialAdapter(this)
        rootBinding.ivNext.setOnClickListener(View.OnClickListener {
            rootBinding.vpIntro.currentItem = rootBinding.vpIntro.currentItem + 1
        })
        rootBinding.ivPrev.setOnClickListener(View.OnClickListener {
            rootBinding.vpIntro.currentItem = rootBinding.vpIntro.currentItem - 1
        })

        tutorialDialog.show()
    }

    override fun start() {
        putBoolean(SP_ALREADY_PLAYED, true)
        tutorialDialog.dismiss()
    }
}