package cz.legat.prectito.navigation

import android.content.Context
import android.content.Intent
import cz.legat.prectito.ui.main.DetailActivity

const val ID_KEY = "id"
const val IS_BOOK_KEY = "SEARCH_RESULT_TYPE_KEY"

fun Context.goToDetailIntent(id: String, isBook: Boolean): Intent {
    val intent = Intent(this, DetailActivity::class.java)
    intent.putExtra(ID_KEY, id)
    intent.putExtra(IS_BOOK_KEY, isBook)
    return intent
}

fun Context.goToBookDetailIntent(id: String): Intent {
    return goToDetailIntent(id, true)
}

fun Context.goToAuthorDetailIntent(id: String): Intent {
    return goToDetailIntent(id, false)
}