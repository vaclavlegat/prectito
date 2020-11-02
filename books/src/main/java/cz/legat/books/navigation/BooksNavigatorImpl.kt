package cz.legat.books.navigation

import android.content.Context
import android.content.Intent
import cz.legat.books.ui.detail.DetailActivity
import cz.legat.navigation.BooksNavigator

internal class BooksNavigatorImpl: BooksNavigator {

    override fun getOpenDetailIntent(context: Context, id: String): Intent {
        return DetailActivity.intent(context, id)
    }
}