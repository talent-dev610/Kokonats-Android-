package com.biiiiit.kokonats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.ABORT
import androidx.room.Query

/**
 * @Author yo_hack
 * @Date 2021.11.28
 * @Description
 **/
//@Dao
interface InAppPayTokenDao {

//    /**
//     * 查找所有的 token
//     */
//    @Query("SELECT * FROM $TAB_APP_TOKEN_NAME")
//    suspend fun getAll(): List<InAppPayToken>
//
//    @Query("SELECT * FROM $TAB_APP_TOKEN_NAME WHERE uid=:uid")
//    suspend fun getAllByUId(uid:Long):List<InAppPayToken>
//
//    /**
//     * query by token
//     */
//    @Query("SELECT * FROM $TAB_APP_TOKEN_NAME WHERE token=:token")
//    suspend fun queryByToken(token: String): InAppPayToken
//
//    /**
//     * insert
//     */
//    @Insert(onConflict = ABORT)
//    suspend fun insert(appPayToken: InAppPayToken)
//
//    /**
//     * 删除所有的token
//     */
//    @Query("DELETE FROM $TAB_APP_TOKEN_NAME WHERE token=:token")
//    suspend fun deleteByToken(token: String): Int
//
//    /**
//     * 删除所有的token
//     */
//    @Query("UPDATE $TAB_APP_TOKEN_NAME SET status=:newStatus WHERE token=:token")
//    suspend fun updateByToken(token: String, newStatus: Int): Int
}