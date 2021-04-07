package cz.legat.core.extensions

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

inline fun <reified T : AppCompatActivity> Context.intent(): Intent {
    return Intent(this, T::class.java)
}

fun Context.dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).roundToInt()
}