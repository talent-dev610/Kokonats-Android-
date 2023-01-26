package com.biiiiit.lib_base.utils.photo

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule

/**
 * @Author yo_hack
 * @Date 2021.10.13
 * @Description
 **/
@GlideModule
class KokoGlideModule : LibraryGlideModule() {
  override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    super.registerComponents(context, glide, registry)
  }
}