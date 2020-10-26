package cz.legat.core.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment

const val ID_KEY = "id"
const val IS_BOOK_KEY = "SEARCH_RESULT_TYPE_KEY"
const val ID_NAME = "name"
const val ID_IMG_LINK = "imgLink"

fun Fragment.withId(id: String): Fragment {
    apply {
        arguments = Bundle().apply {
            putString(ID_KEY, id)
        }
    }
    return this
}