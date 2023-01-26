package com.biiiiit.kokonats.ui.login

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.repo.LoginRepository
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.user.saveUserAndToken

/**
 * @Author yo_hack
 * @Date 2021.10.19
 * @Description
 **/
class LoginViewModel : BaseViewModel() {

    /**
     * 后期考虑 inject
     */
    private val repo: LoginRepository by lazy { LoginRepository() }

    /**
     * 登录数据
     */
    val loginData = MutableLiveData<LoginUser>()

    fun signInGoogle(idToken: String) {
        signInThird(1, idToken)
    }

    fun signInFirebaseApple(idToken: String) {
        signInThird(2, idToken)
    }

    /**
     * third login
     * @param type 1: google, 2: firesebase Apple
     */
    private fun signInThird(type: Int, idToken: String) {
        request(
            {
                if (type == 1) {
                    repo.signInThird("google", idToken)
                } else {
                    repo.signInFirebaseApple(idToken)
                }
            },
            {
                saveUserAndToken(it, idToken)
                loginData.postValue(it)
            }
        )
    }
}