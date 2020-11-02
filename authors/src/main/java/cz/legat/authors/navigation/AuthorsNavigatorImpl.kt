package cz.legat.authors.navigation

import android.content.Context
import android.content.Intent
import cz.legat.authors.ui.DetailActivity
import cz.legat.authors.ui.filter.FilterActivity
import cz.legat.navigation.AuthorsNavigator

internal class AuthorsNavigatorImpl: AuthorsNavigator {

    override fun getOpenDetailIntent(context: Context, id: String): Intent {
        return DetailActivity.intent(context, id)
    }

    override fun getCountryFilterIntent(context: Context): Intent {
        return FilterActivity.intent(context)
    }
}