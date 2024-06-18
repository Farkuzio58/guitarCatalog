/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Clase para cargar imágenes desde una Url.
 */

package com.farkuzio58.guitarcatalog

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class ImageLoader (private val context: Context, private val imageView: ImageView, private val rotation:Float?) {

    fun load(imageUrl: String) {
        if (rotation == null) {
            Glide.with(context)
                .load(imageUrl)
                .into(imageView)
        } else {
            val rotationTransformation = object : BitmapTransformation() {
                override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
                    return rotateImage(toTransform, rotation)
                }
                override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
            }
            Glide.with(context)
                .load(imageUrl)
                .transform(rotationTransformation)
                .into(imageView)
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}