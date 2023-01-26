package com.biiiiit.kokonats.data.repo

import com.biiiiit.kokonats.db.InAppPayToken
import com.biiiiit.kokonats.db.InAppPayTokenDao
import com.biiiiit.lib_base.utils.loge
import java.lang.Exception

/**
 * @Author yo_hack
 * @Date 2021.11.28
 * @Description
 **/
class PayTokenRepository {
    /**
     * db
     */
//    private val dao: InAppPayTokenDao by lazy { getDb(null).getPayTokenDao() }
//
//    suspend fun getAll(): List<InAppPayToken> = dao.getAll()
//
//    suspend fun getAllByUId(uid: Long): List<InAppPayToken> = dao.getAllByUId(uid)
//
//    suspend fun queryByToken(token: String): InAppPayToken = dao.queryByToken(token)
//
//    suspend fun insert(appPayToken: InAppPayToken) {
//        try {
//            dao.insert(appPayToken)
//        } catch (e: Exception) {
//            appPayToken.toString().loge("---- Insert Error")
//        }
//    }
//
//    suspend fun deleteByToken(token: String) = dao.deleteByToken(token)
//
//    suspend fun updateByToken(token: String, newStatus: Int) = dao.updateByToken(token, newStatus)
}