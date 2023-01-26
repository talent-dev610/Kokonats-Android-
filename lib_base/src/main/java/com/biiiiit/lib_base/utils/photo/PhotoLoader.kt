package com.biiiiit.lib_base.utils.photo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.biiiiit.lib_base.BaseApp
import com.biiiiit.lib_base.utils.dp2px
import com.biiiiit.lib_base.utils.logi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.*

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description photoLoader
 **/


/**
 * 加载图片
 */
@SuppressLint("CheckResult")
fun loadImg(
    context: Context?,
    iv: ImageView?,
    model: Any?,
    transform: Transformation<Bitmap>?,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    if (context == null || iv == null) {
        return
    }
    Glide.with(context)
        .load(model)
        .apply {
            if (placeHolder != 0) {
                placeholder(placeHolder)
            }

            if (errorHolder != 0) {
                error(errorHolder)
            }

            transform?.let {
                transform(it)
            }
        }
        .into(iv)
}

/**
 * 加载图片
 */
@SuppressLint("CheckResult")
fun loadImg(
    context: Context?,
    iv: ImageView?,
    model: Any?,
    transform: Transformation<Bitmap>?,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    if (context == null || iv == null) {
        return
    }
    Glide.with(context)
        .load(model)
        .apply {
            if (placeHolder != null) {
                placeholder(placeHolder)
            }

            if (errorHolder != null) {
                error(errorHolder)
            }

            transform?.let {
                transform(it)
            }
        }
        .into(iv)
}

/**
 * centerCrop   round radius
 */
fun loadCropRound(
    context: Context?,
    iv: ImageView,
    model: Any?,
    radius: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadImg(
        context, iv, model,
        if (radius <= 0) {
            CenterCrop()
        } else {
            MultiTransformation<Bitmap>(CenterCrop(), RoundedCorners(radius.dp2px(context)))
        },
        placeHolder, errorHolder
    )
}

fun loadCropRound(
    context: Context?,
    iv: ImageView,
    model: Any?,
    topLeft: Int = 3,
    topRight: Int = 3,
    bottomRight: Int = 3,
    bottomLeft: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadImg(
        context, iv, model,
        MultiTransformation<Bitmap>(CenterCrop(),
            GranularRoundedCorners(
                topLeft.dp2px(context).toFloat(),
                topRight.dp2px(context).toFloat(),
                bottomRight.dp2px(context).toFloat(),
                bottomLeft.dp2px(context).toFloat()
            )),
        placeHolder, errorHolder
    )
}

/**
 * centerCrop   round radius
 */
fun loadCropRound(
    context: Context?,
    iv: ImageView,
    model: Any?,
    radius: Int = 3,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    loadImg(
        context, iv, model,
        if (radius <= 0) {
            CenterCrop()
        } else {
            MultiTransformation<Bitmap>(CenterCrop(), RoundedCorners(radius.dp2px(context)))
        },
        placeHolder, errorHolder
    )
}


/**
 * centerCrop   round radius
 */
fun loadRadius(
    context: Context?,
    iv: ImageView,
    model: Any?,
    radius: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    val transform = if (radius > 0) {
        RoundedCorners(radius.dp2px(context))
    } else {
        null
    }
    loadImg(
        context, iv, model, transform, placeHolder, errorHolder
    )
}

/**
 * centerCrop   round radius
 */
fun loadRadius(
    context: Context?,
    iv: ImageView,
    model: Any?,
    radius: Int = 3,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    val transform = if (radius > 0) {
        RoundedCorners(radius.dp2px(context))
    } else {
        null
    }
    loadImg(
        context, iv, model, transform, placeHolder, errorHolder
    )
}


/**
 * centerInside   round radius
 */
fun loadInsideRound(
    context: Context?,
    iv: ImageView,
    model: Any?,
    radius: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadImg(
        context, iv, model,
        MultiTransformation<Bitmap>(CenterInside(), RoundedCorners(radius.dp2px(context))),
        placeHolder, errorHolder
    )
}

/**
 * centerInside   round radius
 */
fun loadInsideRound(
    context: Context?,
    iv: ImageView,
    model: Any?,
    radius: Int = 3,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    loadImg(
        context, iv, model,
        MultiTransformation<Bitmap>(CenterInside(), RoundedCorners(radius.dp2px(context))),
        placeHolder, errorHolder
    )
}

/**
 * 加载圆形图片
 */
fun loadCircle(
    context: Context?,
    iv: ImageView,
    model: Any?,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadImg(context, iv, model, CircleCrop(), placeHolder, errorHolder)
}


/**
 * 加载圆形图片
 */
fun loadCircle(
    context: Context?,
    iv: ImageView,
    model: Any?,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    loadImg(context, iv, model, CircleCrop(), placeHolder, errorHolder)
}


fun downloadPic(url: String, width: Int? = null, height: Int? = null): Bitmap? {
    if (url.isNullOrBlank()) {
        return null
    }
    return try {
        Glide.with(BaseApp.app)
            .asBitmap()
            .load(url)
            .apply {
                if (width != null && height != null) {

                }
            }
            .submit()
            .get()
    } catch (e: Exception) {
        "get image error".logi()
        null
    }
}

fun ImageView.loadInsideRound(
    model: Any?,
    radius: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadInsideRound(this.context, this, model, radius, placeHolder, errorHolder)
}

fun ImageView.loadCircle(
    model: Any?,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadCircle(this.context, this, model, placeHolder, errorHolder)
}

fun ImageView.loadCircle(
    model: Any?,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    loadCircle(this.context, this, model, placeHolder, errorHolder)
}

fun ImageView.loadCropRound(
    model: Any?,
    radius: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadCropRound(this.context, this, model, radius, placeHolder, errorHolder)
}

fun ImageView.loadCropRound(
    model: Any?,
    topLeft: Int = 3,
    topRight: Int = 3,
    bottomRight: Int = 3,
    bottomLeft: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadCropRound(this.context, this, model, topLeft, topRight, bottomRight, bottomLeft, placeHolder, errorHolder)
}

fun ImageView.loadCropRound(
    model: Any?,
    radius: Int = 3,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    loadCropRound(this.context, this, model, radius, placeHolder, errorHolder)
}


fun ImageView.loadRadius(
    model: Any?,
    radius: Int = 3,
    placeHolder: Int = 0,
    errorHolder: Int = placeHolder
) {
    loadRadius(this.context, this, model, radius, placeHolder, errorHolder)
}


fun ImageView.loadRadius(
    model: Any?,
    radius: Int = 3,
    placeHolder: Drawable? = null,
    errorHolder: Drawable? = placeHolder
) {
    loadRadius(this.context, this, model, radius, placeHolder, errorHolder)
}
