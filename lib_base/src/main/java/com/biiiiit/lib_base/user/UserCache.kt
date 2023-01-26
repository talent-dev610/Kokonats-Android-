package com.biiiiit.lib_base.user

import com.biiiiit.lib_base.data.LoginUser
import com.biiiiit.lib_base.net.KokoNetApi
import com.biiiiit.lib_base.utils.*

/**
 * @Author yo_hack
 * @Date 2021.10.20
 * @Description
 **/

fun saveUserAndToken(user: LoginUser, token: String) {
    saveUser(user)
    saveToken(token)
}

/**
 * save user
 * remember token
 */
fun saveUser(user: LoginUser) {
    putAny(SP_LOGIN_USER, user)
}

fun getUser():LoginUser? = getAny(SP_LOGIN_USER)

fun removeUserAndToken() {
    KokoNetApi.token = null
    removeValueForKey(SP_USER_TOKEN)
    putAny(SP_LOGIN_USER, null)
}

private fun saveToken(idToken: String) {
    KokoNetApi.token = idToken
    putString(SP_USER_TOKEN, idToken)
}

fun getToken() = getString(SP_USER_TOKEN, "")


fun loginOrAction(action: () -> Unit) {
    if (KokoNetApi.token.isNullOrBlank()) {
        simpleJump(ROUTE_LOGIN, null)
    } else {
        action.invoke()
    }
}

