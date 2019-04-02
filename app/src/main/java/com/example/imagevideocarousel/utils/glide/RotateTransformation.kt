package com.example.imagevideocarousel.utils.glide

import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import com.bumptech.glide.load.resource.bitmap.TransformationUtils


class RotateTransformation(context: Context, orientation: Int) : BitmapTransformation() {

    private var rotateRotationAngle = 0f
    private var mOrientation: Int = 0

    init {
        mOrientation = orientation
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val exifOrientationDegrees = getExifOrientationDegrees(mOrientation)
        return TransformationUtils.rotateImageExif(pool, toTransform, exifOrientationDegrees)
    }


    private fun getExifOrientationDegrees(orientation: Int): Int {
        return when (orientation) {
            90 -> ExifInterface.ORIENTATION_ROTATE_90
            // other cases
            else -> ExifInterface.ORIENTATION_NORMAL
        }
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("rotate$rotateRotationAngle".toByteArray())
    }
}