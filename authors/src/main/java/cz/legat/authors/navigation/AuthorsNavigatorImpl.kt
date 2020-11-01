package cz.legat.authors.navigation

import android.content.Context
import android.content.Intent
import cz.legat.authors.DetailActivity
import cz.legat.authors.filter.FilterActivity
import cz.legat.navigation.AuthorsNavigator

class AuthorsNavigatorImpl: AuthorsNavigator {

    override fun getOpenDetailIntent(context: Context, id: String): Intent {
        return DetailActivity.intent(context, id)
    }

    override fun getCountryFilterIntent(context: Context): Intent {
        return FilterActivity.intent(context)
    }
}