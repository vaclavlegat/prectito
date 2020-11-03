package cz.legat.authors.navigation

import android.content.Context
import android.content.Intent
import cz.legat.authors.ui.DetailActivity
import cz.legat.authors.ui.filter.FilterActivity
import cz.legat.core.extensions.intent
import cz.legat.navigation.AuthorsNavigator

internal class AuthorsNavigatorImpl: AuthorsNavigator {

    override fun getOpenDetailIntent(context: Context, id: String): Intent {
        return context.intent<DetailActivity>().putExtra("id", id)
    }

    override fun getCountryFilterIntent(context: Context): Intent {
        return context.intent<FilterActivity>()
    }
}