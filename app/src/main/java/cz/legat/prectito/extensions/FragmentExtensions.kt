package cz.legat.prectito.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import cz.legat.prectito.navigation.ID_KEY

fun Fragment.withId(id: String): Fragment {
    apply {
        arguments = Bundle().apply {
            putString(ID_KEY, id)
        }
    }
    return this
}