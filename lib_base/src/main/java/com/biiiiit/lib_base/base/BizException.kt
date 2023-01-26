package com.biiiiit.lib_base.base

import java.lang.RuntimeException

/**
 * @Author yo_hack
 * @Date 2021.10.15
 * @Description biz exception
 **/
class BizException(val code: Int, msg: String) : RuntimeException(msg)