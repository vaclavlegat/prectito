package cz.legat.prectito.navigation

import android.content.Context
import android.content.Intent
import cz.legat.core.extensions.ID_IMG_LINK
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.extensions.ID_NAME
import cz.legat.core.extensions.IS_BOOK_KEY
import cz.legat.prectito.ui.main.DetailActivity

fun Context.goToDetailIntent(id: String, isBook: Boolean, name: String? = null, imgLink:String? = null): Intent {
    val intent = Intent(this, DetailActivity::class.java)
    intent.putExtra(ID_KEY, id)
    intent.putExtra(IS_BOOK_KEY, isBook)
    intent.putExtra(ID_NAME, name)
    intent.putExtra(ID_IMG_LINK, imgLink)
    return intent
}

fun Context.goToBookDetailIntent(id: String): Intent {
    return goToDetailIntent(id, true)
}

fun Context.goToAuthorDetailIntent(id: String, name: String, imgLink:String): Intent {
    return goToDetailIntent(id, false, name, imgLink)
}