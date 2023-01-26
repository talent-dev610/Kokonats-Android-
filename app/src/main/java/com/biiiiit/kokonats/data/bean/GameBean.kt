package com.biiiiit.kokonats.data.bean

import com.biiiiit.kokonats.data.repo.PLAY_TYPE_PVP
import com.google.gson.annotations.Expose
import java.io.Serializable

/**
 * @Author yo_hack
 * @Date 2021.10.24
 * @Description
 **/

/**
 * https://swagger.kokonats.club/#/game/get_game_list
 */
class Game: Serializable {
    /** GameId saved in system **/
    var id: Long = 0

    /** 游戏版本号 **/
    var nid: String = ""

    /** 游戏名  **/
    var name: String = ""

    /** 游戏短名  **/
    var shortName: String = ""

    /** 游戏分类  **/
    var category: String = ""

    /** 标题下短句  **/
    var slogan: String? = null

    /** 中篇描述  **/
    var description: String = ""

    /** 详细页用长篇介绍  **/
    var introduction: String = ""


    var coverImageUrl: String = ""

    /** icon  **/
    var iconUrl: String = ""

    /** small icon  **/
    var faviconUrl: String = ""

    /** list of 5 pics  **/
    var screenshot: String = ""

    /** 游戏CP公司邮箱   **/
    var email: String = ""

    /** Company name of the game **/
    var company: String = ""

    /** Company twitter.  **/
    var twitter: String = ""

    /** Company facebook.  **/
    var facebook: String = ""

    /** Company line.  **/
    var line: String = ""

    /** Cdn download url.  **/
    var cdnUrl: String = ""

    /** 游戏服务器Url  **/
    var gameServerUrl: String = ""

    /** Call back url.  **/
    var callbackUrl: String = ""

    /** 游戏状态 0 准备，1 测试， 2　公开  **/
    var state: Int = 0
    /** 列表中的高亮，Normal\New\Hot  **/
//    var highlightType: Int = 0
    /** 游戏列表中排列顺序  **/
    var orderWeight: Int = 0

    /** 游戏验证用secret，与id并用  **/
    var secret: String = ""

    /** created time.  **/
    var createTimestamp: Long = 0

    /** keyword  ココ大量発生中,新作 **/
    var keyword: String? = null
    var tag: String? = null

    @Expose(deserialize = false)
    @Transient
    var keyList: MutableList<String> = mutableListOf()
        get() {
            if (field.isNullOrEmpty() && !keyword.isNullOrBlank()) {
                field = keyword?.split(",")?.toMutableList() ?: field
            }
            return field
        }

    @Expose(deserialize = false)
    @Transient
    var tagList: MutableList<String> = mutableListOf()
        get() {
            if (field.isNullOrEmpty() && !tag.isNullOrBlank()) {
                field = tag?.split(",")?.toMutableList() ?: field
            }
            return field
        }

    @Expose(deserialize = false)
    @Transient
    var colorList: MutableList<String> = mutableListOf()
        get() {
            if (field.isNullOrEmpty()) {
                val words = keyList
                val tags = tagList
                words.addAll(tags)
                field = words
            }
            return field
        }
}

class TournamentClass : Serializable {
    var id: Long = 0
    var gameId: Long = 0
    var tournamentName: String = ""
    var description: String = ""
    var thumbnail: String? = null
    var coverImageUrl: String? = null


    /** type of the tournament class0 half hour 1 midnight run 2 king of the night 3 practise **/
    var type: Int = 0

    /** start time of the tournament format 'HH:mm:ss' **/
    var startTime: String = ""

    /** tournament duration time 持续时间 **/
    var durationSecond: Long = 0

    /** 最终入场倒计时 **/
    var entryBeforeSecond: Long = 0

    /** 入场价格 **/
    var entryFee: Long = 0

    var participantNumber: Int = 0

    var rankingPayout: String? = ""
    var winningPayout: Int = 0

    /** 游戏游玩时间 **/
    var durationPlaySecond: Long = 190

    /** keyword  ココ大量発生中,新作 **/
    var keyword: String? = null
    var tag: String? = null


    @Expose(deserialize = false, serialize = false)
    @Transient
    var players: MutableList<TnmtPlayer>? = null

    @Expose(deserialize = false)
    @Transient
    var keyList: MutableList<String> = mutableListOf()
        get() {
            if (field.isNullOrEmpty() && !keyword.isNullOrBlank()) {
                field = keyword?.split(",")?.toMutableList() ?: field
            }
            return field
        }

    @Expose(deserialize = false)
    @Transient
    var tagList: MutableList<String> = mutableListOf()
        get() {
            if (field.isNullOrEmpty() && !tag.isNullOrBlank()) {
                field = tag?.split(",")?.toMutableList() ?: field
            }
            return field
        }

    @Expose(deserialize = false)
    @Transient
    var colorList: MutableList<String> = mutableListOf()
        get() {
            if (field.isNullOrEmpty()) {
                val words = keyList.toMutableList()
                val tags = tagList
                words.addAll(tags)
                field = words
            }
            return field
        }

//    /** icon  **/
//    var iconUrl: String = ""
//        get() = "https://www.riotgames.com/darkroom/855/ac04c903e761168dfbf22893efd1ce48:20eb61af7733fa7324e94d2f8a1a08c1/valorant-productimage.png"

}

class Tournament {
    var id: Long = 0
    var gameId: Long = 0
    var tournamentClassId: Long = 0
    var tournamentName: String = ""
    var description: String = ""

    var rankingPayout: String? = ""

    var participantNumber: Int = 0
    var joinPlayersCount: Int = 0

    var newestJoinPlayers: MutableList<TnmtPlayer>? = null
}

class TournamentPlay {
    var id: Long = 0
    var matchId: String = ""
    var tournamentId: Long = 0

    var energy: Int = 0

    var result: String? = null

    var timestamp: Long = 0

    var matchName: String = ""
    var tournamentName: String = ""
    var userName: String = ""
    var gameId: Long = 0
    var score: Int = 0

    var type: Int = 0

    var kokoAmount: Long = 0

    fun getRealName() = if (type == 0) {
        tournamentName
    } else {
        matchName
    }
}

class TnmtPlayer {
    var userName: String = ""
    var picture: String = ""
}

class TnmtPayout {
    var startRank: String = ""
    var endRank: String = ""
    var payout: Int = 0
}

class GameAuth {
    var token: String = ""

    @Transient
    var tnmtPlayId: Long = 0
}

class EnterGameData(

    var type: Int,

    var token: String,

    var playId: Long,

    /**
     * tnmtId or matchId
     */
    var tnmtId: Long,

    var gameUrl: String,

    var durationS: Long,
)

class GameItem {
    /** Game item id saved in system **/
    var id: Long = 0

    /** Game id saved in system **/
    var gameId: Long = 0

    /** Game item name  **/
    var name: String = ""

    /** Game item cover picture url  **/
    var pictureUrl: String = ""

    /** Game item price **/
    var kokoPrice: Long = 0

    fun map2Item(): GameItem = GameItem().also {
        it.id = id
        it.gameId = gameId
        it.name = name
        it.pictureUrl = pictureUrl
        it.kokoPrice = kokoPrice
    }
}

class PurchasedGameItem {
    /** id **/
    var id: Long = 0

    /** game id **/
    var gameId: Long = 0

    /** game item id **/
    var gameItemId: Long = 0

    /** Game item name  **/
    var gameItemName: String = ""

    /** Game item price **/
    var kokoPrice: Long = 0

    fun map2Item(): PurchasedGameItem = PurchasedGameItem().also {
        it.id = id
        it.gameId = gameId
        it.gameItemId = gameItemId
        it.gameItemName = gameItemName
        it.kokoPrice = kokoPrice
    }
}



