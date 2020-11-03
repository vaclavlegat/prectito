package cz.legat.books.navigation

import android.content.Context
import android.content.Intent
import cz.legat.books.ui.detail.DetailActivity
import cz.legat.core.extensions.intent
import cz.legat.navigation.BooksNavigator

internal class BooksNavigatorImpl: BooksNavigator {

    override fun getOpenDetailIntent(context: Context, id: String): Intent {
        return context.intent<DetailActivity>().putExtra("id", id)
    }
}