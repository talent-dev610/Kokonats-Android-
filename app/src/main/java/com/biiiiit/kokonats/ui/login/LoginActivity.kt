package com.biiiiit.kokonats.ui.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.alibaba.android.arouter.facade.annotation.Route
import com.biiiiit.kokonats.BuildConfig
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.databinding.ActivityLoginBinding
import com.biiiiit.lib_base.base.BaseActivity
import com.biiiiit.lib_base.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

/**
 * @Author yo_hack
 * @Date 2021.10.18
 * @Description login activity
 **/
@Route(path = ROUTE_LOGIN)
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    /**
     * google sign in client
     */
    private val gsic: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(this, gso)
    }


    /**
     * google sign launcher
     */
    private var gResultLauncher: ActivityResultLauncher<GoogleSignInClient>? = null

    private val appleProvider by lazy {
        OAuthProvider.newBuilder("apple.com").apply {
            scopes = mutableListOf("email", "name")
        }
    }


    override fun getVMClazz(): Class<LoginViewModel> = LoginViewModel::class.java
    override fun createBinding(layoutInflater: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)


    override fun initView1() {
        gResultLauncher = registerForActivityResult(
            object : ActivityResultContract<GoogleSignInClient, Task<GoogleSignInAccount>>() {
                override fun createIntent(
                    context: Context,
                    input: GoogleSignInClient?
                ): Intent {
                    return input?.signInIntent ?: gsic.signInIntent
                }

                override fun parseResult(
                    resultCode: Int,
                    intent: Intent?
                ): Task<GoogleSignInAccount> {
                    return GoogleSignIn.getSignedInAccountFromIntent(intent)
                }
            }
        ) {
            dealWithSignIn(it, 1)
        }


        binding.btnGoogle.setOnClickListener {
            //show loading
            showLoading(getString(R.string.loading_dot))
            gResultLauncher?.launch(gsic)
        }

        binding.ivApple.setOnClickListener {
            showLoading(getString(R.string.loading_dot))
            signInWithApple()
        }

    }


    override fun initViewModel2() {
        vm.loginData.observe(this) {
            it?.let {
                jump2Main(this)
                finish()
            }
        }
    }


    /**
     * apple login
     */
    private fun signInWithApple() {
        val instance = FirebaseAuth.getInstance()

        val pending = instance.pendingAuthResult
        if (pending != null) {
            dealWithSignIn(pending)
        } else {
            dealWithSignIn(
                FirebaseAuth.getInstance()
                    .startActivityForSignInWithProvider(this, appleProvider.build())
            )
        }

    }


    /**
     * deal with sign in
     * @param stage 1: google 第一步获取 oauth 2: apple 登录   3： Google 第二步 firebase 登录
     */
    private fun <T> dealWithSignIn(task: Task<T>, stage: Int = 2) {
        task
            .addOnSuccessListener {
                it.toString().loge()
                when (it) {
                    is GoogleSignInAccount -> {
                        if (stage == 1) {
                            val p = GoogleAuthProvider.getCredential(it.idToken ?: "", null)
                            dealWithSignIn(FirebaseAuth.getInstance().signInWithCredential(p), 3)
                        }
                    }
                    is GetTokenResult -> {
                        // 获取用户
                        vm.signInFirebaseApple(it.token ?: "")
                    }
                    is AuthResult -> {
                        (it.credential as? OAuthCredential)?.idToken?.let { token ->
                            when (stage) {
                                2 -> {
                                    FirebaseAuth.getInstance().currentUser?.let { user->
                                        dealWithSignIn(user.getIdToken(true), 4)
                                    }
                                }
                                3 -> {
                                    vm.signInGoogle(token)
                                }
                                else -> {
                                }
                            }
                        } ?: kotlin.run {
                            (" Login Failed").logi()
                            dismissLoading()
                        }
                    }
                }
            }
            .addOnFailureListener {
                ("${it.message} Login Failed").logi()
                dismissLoading()
            }
            .addOnCanceledListener {
                "Login Cancel".logi()
                dismissLoading()
            }
            .addOnCompleteListener {
                //hide dialog
                if (stage != 1) {
                    "Login Complete".logi()
                }
            }

    }

    override val showFloat: Boolean = false

}