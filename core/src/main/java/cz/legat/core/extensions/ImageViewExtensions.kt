package cz.legat.core.extensions

import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import cz.legat.core.R

fun ImageView.tintedDrawable(@DrawableRes drawableRes: Int, @AttrRes colorRes: Int) {
    val vectorDrawable = VectorDrawableCompat.create(
        this.context.resources,
        drawableRes,
        null
    )

    val drawable = DrawableCompat.wrap(vectorDrawable!!)
    val typedValue = TypedValue()
    this.context.theme.resolveAttribute(colorRes, typedValue, true)
    val color = typedValue.data

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
    return Glide.with(this).load(imgLink).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
        .placeholder(R.drawable.pt_img_placeholder)
        .transition(DrawableTransitionOptions.withCrossFade(200))
}