package com.biiiiit.kokonats.utils

import android.content.Context
import android.widget.ImageView
import com.biiiiit.kokonats.R
import com.biiiiit.kokonats.data.bean.TournamentClass
import com.biiiiit.kokonats.data.repo.*
import com.biiiiit.lib_base.utils.logi
import com.biiiiit.lib_base.utils.photo.loadCircle
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author yo_hack
 * @Date 2021.12.01
 * @Description
 **/


/**
 * 获取项目中的 play game Type inside App
 */
fun getPlayGameTypeByTnmtType(type: Int): Int =
    when (type) {
        PLAY_TYPE_PRACTISE -> PLAY_GAME_PRACTISE
        0, 1, 2 -> PLAY_GAME_TNMT
        PLAY_TYPE_PVP -> PLAY_GAME_PVP
        else -> PLAY_GAME_UNKNOWN
    }


/**
 * 获取项目职工的 play game str
 */
fun getPlayGameStrByPgt(typeInt: Int): String =
    when (typeInt) {
        PLAY_GAME_TNMT -> PLAY_STR_TNMT
        PLAY_GAME_PRACTISE -> PLAY_STR_PRACTISE
        PLAY_GAME_PVP -> PLAY_STR_MATCH
        else -> ""
    }


/**
 * 加载图像
 * @param picture: 1--5  http 都可以
 */
fun loadCircleAvatar(iv: ImageView?, picture: String, context: Context?, check: Boolean = true) {
    val avatar: Any = if (picture.startsWith("http")) {
        picture
    } else {
        val name = if (check) {
            "ic_def_avatar${picture}"
        } else {
            "ic_def_no_avatar${picture}"
        }
        context?.resources
            ?.getIdentifier(name, "drawable", context.packageName) ?: 0
    }

    iv?.loadCircle(avatar, 0, R.drawable.ic_def_avatar1)
}

/**
 * 比较 tnmt 时间
 * @param time 后台返回的时间类型， jp 时间, 格式为 23:00:00
 * @param beforeSecond 进入游戏截止的时间
 * @return -1 小于  0 ing  1 过了 2满了
 */
fun getMultiTimeStatus(tournament: TournamentClass): Pair<Int, String> {
    return when (tournament.type) {
        PLAY_TYPE_PRACTISE, PLAY_TYPE_CIRCLE -> Pair(0, "")
        else -> getTnmtTimeStatus(
            tournament.startTime, tournament.durationSecond, tournament.entryBeforeSecond
        )
    }
}

/**
 * 比较 tnmt 时间
 * @param time 后台返回的时间类型， jp 时间, 格式为 23:00:00
 * @param beforeSecond 进入游戏截止的时间
 * @return -1 小于  0 ing  1 过了 2满了
 */
fun getTnmtTimeStatus(time: String, durationSecond: Long, beforeSecond: Long): Pair<Int, String> {
    // 当前时间
    val nowCal = Calendar.getInstance(Locale.JAPAN)
    val curTimeInMillis = nowCal.timeInMillis


    // 设置的时间
    val timeCal = Calendar.getInstance(Locale.JAPAN)
    val format = SimpleDateFormat("HH:mm:ss", Locale.JAPAN)
    format.timeZone = TimeZone.getTimeZone("GMT+9:00")
    timeCal.time = format.parse(time) ?: Date()
    val today = nowCal.get(Calendar.DATE)
    timeCal.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), today)
    val timeInMillis = timeCal.timeInMillis

    // 最大时间
    val maxTimeInMillis = timeInMillis + (durationSecond - beforeSecond) * 1000
    timeCal.timeInMillis = maxTimeInMillis
    val maxDay = timeCal.get(Calendar.DATE)

    // 隔夜的今天的最后的时间
    var maxTimeInMillisToday = 0L
    // 是否是同一天 用于后面的判断
    val isSameDay = maxDay == today
    if (!isSameDay) {
        timeCal.add(Calendar.DATE, -1)
        maxTimeInMillisToday = timeCal.timeInMillis
    }


    // 为了回显
    val reFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    val endTime = reFormat.format(timeInMillis) + " ~"

    "$curTimeInMillis $timeInMillis $maxTimeInMillis  $maxTimeInMillisToday ${curTimeInMillis - timeInMillis}    ${format.timeZone}  ${reFormat.timeZone}  $endTime".logi(
        "------"
    )


    val resultInt =
        if (isSameDay) {
            when {
                curTimeInMillis < timeInMillis -> {
                    -1
                }
                curTimeInMillis > maxTimeInMillis -> {
                    1
                }
                else -> {
                    0
                }
            }
        } else {
            when {
                curTimeInMillis < maxTimeInMillisToday || curTimeInMillis > timeInMillis -> {
                    0
                }
                curTimeInMillis < timeInMillis -> {
                    -1
                }
                else -> {
                    1
                }
            }
        }

    // 时间戳的比较, 游玩中 当前天 >
    return Pair(resultInt, endTime)
}





