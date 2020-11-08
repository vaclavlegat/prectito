package cz.legat.core.extensions

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.palette.graphics.Palette
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import cz.legat.core.R

fun ImageView.tintedDrawable(@DrawableRes drawableRes: Int, @ColorRes colorRes: Int) {
    val vectorDrawable = VectorDrawableCompat.create(
        this.context.resources,
        drawableRes,
        null
    )

    val drawable = DrawableCompat.wrap(vectorDrawable!!)

    val color = ContextCompat.getColor(
        this.context,
        colorRes
    )
    DrawableCompat.setTint(drawable.mutate(), color)
    this.setImageDrawable(drawable)
}

fun ImageView.loadImg(vararg imgLinks: String?) {
    loadSingleImg(imgLinks.getOrNull(0))
        .error(loadSingleImg(imgLinks.getOrNull(1)))
        .error(loadSingleImg(imgLinks.getOrNull(2)))
        .into(this)
}

fun ImageView.loadSingleImg(imgLink: String?): RequestBuilder<Drawable> {
    return Glide.with(this).load(imgLink).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.pt_img_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade(200))
}

fun ImageView.loadWithBackground(imgLink: String?, background: View) {
    val iv = this
    Glide.with(this).asBitmap().load(imgLink).addListener(object : RequestListener<Bitmap> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (resource != null) {
                val p: Palette = Palette.from(resource).generate()
                val color =  p.getMutedColor(ContextCompat.getColor(iv.context, R.color.pt_star_color))
                if(color == ContextCompat.getColor(iv.context, R.color.pt_star_color)){
                    val colorDominamnt =  p.getDominantColor(ContextCompat.getColor(iv.context, R.color.pt_star_color))
                    background.setBackgroundColor(colorDominamnt)
                } else {
                    background.setBackgroundColor(color)
                }
            }
            return false
        }
    }).into(this)
}