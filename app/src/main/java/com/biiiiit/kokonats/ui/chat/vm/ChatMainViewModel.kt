package com.biiiiit.kokonats.ui.chat.vm

import androidx.lifecycle.MutableLiveData
import com.biiiiit.kokonats.BuildConfig
import com.biiiiit.kokonats.data.bean.ChatAuthor
import com.biiiiit.kokonats.data.bean.ChatMessage
import com.biiiiit.kokonats.data.bean.ChatTh
import com.biiiiit.kokonats.data.bean.ChatThread
import com.biiiiit.kokonats.ui.chat.*
import com.biiiiit.lib_base.base.BaseViewModel
import com.biiiiit.lib_base.user.getUser
import com.biiiiit.lib_base.utils.logi
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * @Author yo_hack
 * @Date 2022.03.28
 * @Description
 **/
class ChatMainViewModel : BaseViewModel() {

    val titles = MutableLiveData<List<ChatThread>>()

    val details = MutableLiveData<MutableList<ChatMessage>>()

    /**
     * 当前 thread Id
     */
    var currentThread: ChatThread = ChatThread()

    /**
     * 当前 user
     */
    var currentUser = MutableLiveData<ChatAuthor>()

    /**
     * send msg data
     */
    var sendMsgData = MutableLiveData<Pair<String, Int>>()


    private var typeRegistration: ListenerRegistration? = null
    var detailRegistration: ListenerRegistration? = null

    fun queryType(type: String) {
        typeRegistration = getQueryByType(type)
            .addSnapshotListener { value, e ->
                "${value?.size()}  ${type}  ${e?.localizedMessage}".logi("-----", e)
                value?.documents?.map {
                    (it.toObject(ChatThread::class.java) ?: ChatThread()).apply {
                        this.id = it.id
                    }
                }?.let {
                    titles.postValue(it)
                }
            }
    }

    private fun getQueryByType(type: String): Query {
        Firebase.firestore.collection(BuildConfig.CHAT_THREAD).let {
            return when (type) {
                TYPE_CHAT_PUBLIC -> {
                    getChatRoomQuery(it)
                }
                TYPE_CHAT_DM -> {
                    getChatDMQuery(it)
                }
                else -> {
                    getChatCsQuery(it)
                }
            }
        }

    }

    private fun getChatRoomQuery(cr: CollectionReference) =
        cr.whereEqualTo(CHAT_PROPERTY_TYPE, TYPE_CHAT_PUBLIC)
            .orderBy(CHAT_PROPERTY_LASTSENTAT, Query.Direction.DESCENDING)

    private fun getChatDMQuery(cr: CollectionReference) =
        cr.whereArrayContains("members", getUser()?.id ?: 0)
            .whereEqualTo(CHAT_PROPERTY_TYPE, TYPE_CHAT_DM)
            .orderBy(CHAT_PROPERTY_LASTSENTAT, Query.Direction.DESCENDING)

    private fun getChatCsQuery(cr: CollectionReference) =
        cr.whereArrayContains("members", getUser()?.id ?: 0)
            .whereEqualTo(CHAT_PROPERTY_TYPE, TYPE_CHAT_CS)


    /**
     * 获取 list
     */
    fun queryDataListById(id: String) {
        if (id.isNullOrBlank()) {
            return
        }
        detailRegistration = Firebase.firestore.collection(BuildConfig.CHAT_MESSAGE)
            .document(id)
            .collection(CHAT_PATH_MESSAGE)
            .orderBy("sentAt")
            .addSnapshotListener { value, error ->
                details.postValue(value?.toObjects(ChatMessage::class.java) ?: mutableListOf())
            }
    }

    /**
     * 发送 public msg
     */
    fun sendPublicMsg(msg: String) {
        sendThreadMessage(msg)
    }


    /**
     * send cs msg
     */
    fun sendCSMsg(msg: String) {
        if (currentThread.id.isNullOrBlank()) {
            // 没有发送过
            sendNoThreadMsg(msg, TYPE_CHAT_CS)
        } else {
            // 发送过
            sendThreadMessage(msg)
        }
    }

    /**
     * 发送 dm message
     */
    fun sendDMMsg(msg: String) {
        if (currentThread.id.isNullOrBlank()) {
            // 没有发送过
            sendNoThreadMsg(msg, TYPE_CHAT_DM)
        } else {
            // 发送过
            sendThreadMessage(msg)
        }
    }


    /**
     * 发送消息， 有 threadId
     * 如果有 threadId 代表不是第一次发送， 如果没有，代表第一次发送
     */
    private fun sendThreadMessage(msg: String, dId: String? = null) {
        Firebase.firestore.collection(BuildConfig.CHAT_MESSAGE)
            .document(dId ?: currentThread.id)
            .collection(CHAT_PATH_MESSAGE)
            .add(ChatMessage().apply {
                author = generateChatAuthor()
                body = msg
                sentAt = Timestamp.now()
            })
//            .add(mapOf(
//                "author" to generateChatAuthor(),
//                "body" to msg,
//                "sendAt" to FieldValue.serverTimestamp()
//            ))
            .addOnSuccessListener {
                // 如果是第一次发送消息，则更新 chat-Thread
                if (dId.isNullOrBlank()) {
                    updatePublicThread(msg)
                }
                sendMsgData.postValue(msg to 1)
            }
            .addOnCanceledListener {
                // 发送失败
                sendMsgData.postValue(msg to 0)
            }
    }

    /**
     * 更新 thread
     */
    private fun updatePublicThread(msg: String) {
        val authorId = getUser()?.id ?: 0L
        Firebase.firestore.collection(BuildConfig.CHAT_THREAD)
            .document(currentThread.id)
            .update(mutableMapOf<String, Any>(
                CHAT_PROPERTY_LASTSENTAT to FieldValue.serverTimestamp(),
                CHAT_PROPERTY_SEND_AUTHOR to authorId
            ).apply {
                val members = currentThread.members ?: mutableListOf()
                if (!members.contains(authorId)) {
                    // 添加 members
                    members.add(authorId)
                    put("members", members)

                    val membersDetail = currentThread.membersDetail ?: mutableListOf()
                    membersDetail.add(generateChatAuthor())
                    put("membersDetail", membersDetail)
                }
            })
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    /**
     * 发送信息 noThreadId
     * 因为没有 thread Id 所以先需要创建 thread， 再利用这个 thread 创建 id
     */
    private fun sendNoThreadMsg(msg: String, type: String) {
        val author = currentUser.value ?: return
        val me = generateChatAuthor()

        val chatThread = ChatTh().apply {
            this.lastSentAt = Timestamp.now()
            this.createdAt = this.lastSentAt
            this.type = type
            this.lastSentAuthorId = me.id
            this.tournamentClassId = 0

            this.name = "${me.id}_${author.id}"
            this.members = mutableListOf(me.id, author.id)
            this.membersDetail = mutableListOf(me, author)
        }
        Firebase.firestore.collection(BuildConfig.CHAT_THREAD)
            .add(chatThread)
            .addOnSuccessListener {
                // 加载完成之后 保存这个 id 数据， 下次不执行
                currentThread = ChatThread.map2Me(chatThread)
                currentThread.id = it.id
                sendThreadMessage(msg, it.id)
                // 这个列表， 请求到详情页面
                if (detailRegistration == null) {
                    queryDataListById(it.id)
                }
            }
            .addOnFailureListener {
            }
    }


    private fun generateChatAuthor() = ChatAuthor().apply {
        val u = getUser()
        this.id = u?.id ?: 0
        this.picture = u?.picture ?: ""
        this.userName = u?.userName ?: ""
    }


    override fun onCleared() {
        typeRegistration?.remove()
        detailRegistration?.remove()
        super.onCleared()
    }

}