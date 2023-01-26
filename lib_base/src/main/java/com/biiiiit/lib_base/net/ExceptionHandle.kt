package com.biiiiit.lib_base.net

import android.util.MalformedJsonException
import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.R
import com.biiiiit.lib_base.base.BizException
import com.biiiiit.lib_base.user.removeUserAndToken
import com.biiiiit.lib_base.utils.ActivityManger
import com.biiiiit.lib_base.utils.ROUTE_LOGIN
import com.biiiiit.lib_base.utils.simpleJump
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLException

/**
 * @Author yo_hack
 * @Date 2021.10.15
 * @Description convert network exception to bizException
 **/
fun handleNetException(e: Throwable?): BizException {
    fun getStringFromId(id: Int) = BaseApp.app.getString(id)
    return when (e) {
        is HttpException -> {
            when (e.code()) {
                401 -> {
                    // 过期
                    removeUserAndToken()
                    ActivityManger.finishAllActivity()
                    simpleJump(ROUTE_LOGIN, null)
                    BizException(NET_AUTH_FAILED, getStringFromId(R.string.login_expire))
                }
                406 -> {
                    // 升级
                    BizException(NET_AUTH_FAILED, getStringFromId(R.string.login_expire))
                }
                else -> {
                    BizException(
                        NET_ERROR_NETWORK,
                        "${getStringFromId(R.string.network_error)}...${e.code()}"
                    )
                }
            }

        }
        is ConnectException
        -> BizException(NET_ERROR_NETWORK, getStringFromId(R.string.network_error))

        is JSONException, is ParseException, is JsonParseException, is MalformedJsonException
        -> BizException(NET_ERROR_PARSE, getStringFromId(R.string.parse_data_error))

        is SSLException -> BizException(NET_ERROR_SSL, getStringFromId(R.string.unsafe_network))

        is SocketTimeoutException, is UnknownHostException
        -> BizException(NET_ERROR_TIMEOUT, getStringFromId(R.string.connect_timeout))

        is BizException -> e

        else -> BizException(NET_ERROR_UNKNOWN, getStringFromId(R.string.unknown_error))
    }
}