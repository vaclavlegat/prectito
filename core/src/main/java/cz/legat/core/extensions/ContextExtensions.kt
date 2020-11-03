package cz.legat.core.extensions

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

inline fun <reified T : AppCompatActivity> Context.intent(): Intent {
    return Intent(this, T::class.java)
}