package cz.legat.core.extensions

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.CollapsingToolbarLayout
import cz.legat.core.R

fun TextView.fadeInText(value: String?) {
    text = value
    visibility = View.VISIBLE
    alpha = 0f
    animate().apply {
        duration = 200
        alpha(1f)
    }
}

fun TextView.fadeInTextFromStart(value: String?) {
    text = value
    visibility = View.VISIBLE
    alpha = 0f
    animate().apply {
        duration = 200
        alpha(1f)
    }
}

fun CollapsingToolbarLayout.fadeInText(value: String?) {
    title = value
    visibility = View.VISIBLE
    alpha = 0f
    animate().apply {
        duration = 500
        alpha(1f)
    }
}

fun TextView.animateRating(rating: Int){
    ValueAnimator.ofInt(rating).apply {
        addUpdateListener { animation ->
            text = "${animation.animatedValue as Int} %"
        }
        visibility = View.VISIBLE
        duration = 1000
        start()
    }
}
