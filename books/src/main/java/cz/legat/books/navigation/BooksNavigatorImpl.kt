package cz.legat.books.navigation

import android.content.Context
import android.content.Intent
import cz.legat.books.DetailActivity
import cz.legat.navigation.BooksNavigator

class BooksNavigatorImpl: BooksNavigator {

    override fun getOpenDetailIntent(context: Context, id: String): Intent {
        return DetailActivity.intent(context, id)
    }
}