package cz.legat.core.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import cz.legat.core.R
import jp.wasabeef.glide.transformations.BlurTransformation

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

fun ImageView.loadSingleBlurredImg(imgLink: String?) {
    Glide.with(this).load(imgLink).apply(RequestOptions.bitmapTransform(BlurTransformation(15, 3))).into(this)
}