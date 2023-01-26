package com.biiiiit.kokonats.ui.user.vm

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.data.bean.TournamentPlay
import com.biiiiit.kokonats.data.repo.UserRepository
import com.biiiiit.kokonats.data.repo.UserTnmtRepository
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.data.ResultState
import com.biiiiit.lib_base.user.getUser
import com.biiiiit.lib_base.user.saveUser

/**
 * @Author yo_hack
 * @Date 2021.10.23
 * @Description
 **/
class UserInfoViewModel : BaseViewModel() {

    val loginUser = MutableLiveData<LoginUser>()

    val tnmtPlayState = MutableLiveData<ResultState<List<TournamentPlay>>>()

    val changeSuccess = MutableLiveData<Boolean>()

    private val userRepo: UserRepository by lazy {
        UserRepository()
    }

    private val userTnmtRepo: UserTnmtRepository by lazy {
        UserTnmtRepository()
    }


    init {
        getUser()?.let {
            loginUser.postValue(it)
        }
    }

    fun queryUserInfo() {
        request({
            userRepo.getUserInfo()
        }, {
            loginUser.postValue(it)
        },
            error = null,
            loadingMsg = null,
            hideAction = null
        )
    }

    fun changeUserInfo(picture: String, userName: String) {
        request(
            {
                userRepo.changeUserInfo(userName, picture)
            },
            {
                saveUser(it)
                changeSuccess.postValue(true)
            }
        )
    }

    fun queryUserTnmtPlayHistory() {
        request(
            {
                userTnmtRepo.getUserTnmtPlays(true)
            },
            tnmtPlayState,
            null
        )
    }
}