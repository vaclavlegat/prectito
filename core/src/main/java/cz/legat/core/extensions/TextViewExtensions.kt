package cz.legat.core.extensions

import android.view.View
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout

fun TextView.fadeInText(value: String?) {
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