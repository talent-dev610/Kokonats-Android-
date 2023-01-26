package com.biiiiit.kokonats.data.repo

/**
 * @Author yo_hack
 * @Date 2021.10.31
 * @Description
 **/

const val GAME_ORDER_ALL = "ALL"

const val GAME_ORDER_NEW = "NEW"

const val PLAY_TYPE_CIRCLE = 0

const val PLAY_TYPE_PRACTISE = 3

const val PLAY_TYPE_PVP = 100000


/**
 * 玩游戏的 type tnmt
 */
const val PLAY_STR_TNMT = "tournament"

/**
 * 玩游戏的 type match
 */
const val PLAY_STR_MATCH = "match"

/**
 * 玩游戏的 type practise
 */
const val PLAY_STR_PRACTISE = "practise"


/**
 * 玩游戏的类型 tnmt
 */
const val PLAY_GAME_TNMT = 1

/**
 * 玩游戏的类型 practise
 */
const val PLAY_GAME_PRACTISE = 3

/**
 * 未知未知
 */
const val PLAY_GAME_UNKNOWN = -1

/**
 * 玩游戏的类型 pvp
 */
const val PLAY_GAME_PVP = 101


/**
 * pvp state
 * 匹配状态(-1 错误; 0 已请求; 1 匹配中; 2 已匹配; 4 匹配超期)
 */
const val PVP_STATE_UNKNOWN = -1
const val PVP_STATE_REQUESTED = 0
const val PVP_STATE_MATCHING = 1
const val PVP_STATE_MATCHED = 2
const val PVP_STATE_TIME_OUT = 4





