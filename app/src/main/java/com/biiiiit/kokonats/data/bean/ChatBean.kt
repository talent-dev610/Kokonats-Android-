package com.biiiiit.kokonats.data.bean

import com.google.firebase.Timestamp
import java.util.*

/**
 * @Author yo_hack
 * @Date 2022.03.31
 * @Description
 **/


class ChatAuthor {
    var id: Long = 0
    var picture: String = ""
    var userName: String = ""
}

class ChatMessage {
    var author: ChatAuthor? = null

    var body: String = ""

    var sentAt: Timestamp? = Timestamp.now()

}

open class ChatTh {
    var icon: String? = ""
    var lastSentAt: Timestamp? = Timestamp.now()
    var createdAt: Timestamp? = Timestamp.now()
    var lastSentAuthorId: Long = 0
    var members: MutableList<Long>? = null
    var membersDetail: MutableList<ChatAuthor>? = null
    var tournamentClassId: Long? = null
    var type: String = ""
    var name: String = ""
}

class ChatThread : ChatTh() {
    var id: String = ""

    companion object {
        fun map2Me(chatTh: ChatTh) = ChatThread().apply {
            lastSentAt = chatTh.lastSentAt
            lastSentAuthorId = chatTh.lastSentAuthorId
            members = chatTh.members
            membersDetail = chatTh.membersDetail
            tournamentClassId = chatTh.tournamentClassId
            type = chatTh.type
            name = chatTh.name
        }
    }
}