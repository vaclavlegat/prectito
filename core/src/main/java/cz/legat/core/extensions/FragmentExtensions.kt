package cz.legat.core.extensions

import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment

const val ID_KEY = "id"
const val IS_BOOK_KEY = "SEARCH_RESULT_TYPE_KEY"

fun Fragment.withId(id: String): Fragment {
    apply {
        arguments = Bundle().apply {
            putString(ID_KEY, id)
        }
    }
    return this
}

fun Fragment.getThemeColor(@AttrRes colorId: Int): Int {
    val typedValue = TypedValue()
    requireActivity().theme.resolveAttribute(colorId, typedValue, true)
    return typedValue.data
}