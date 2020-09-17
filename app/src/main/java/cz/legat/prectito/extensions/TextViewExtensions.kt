package cz.legat.prectito.extensions

import android.view.View
import android.widget.TextView

fun TextView.fadeInText(value: String?) {
    text = value
    visibility = View.VISIBLE
    alpha = 0f
    animate().apply {
        duration = 200
        alpha(1f)
    }
}