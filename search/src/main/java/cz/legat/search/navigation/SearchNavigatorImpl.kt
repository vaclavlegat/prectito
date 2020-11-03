package cz.legat.search.navigation

import android.content.Context
import android.content.Intent
import cz.legat.core.extensions.intent
import cz.legat.navigation.SearchNavigator
import cz.legat.search.SearchResultsActivity

internal class SearchNavigatorImpl : SearchNavigator {
    override fun getOpenSearchIntent(context: Context): Intent {
        return context.intent<SearchResultsActivity>()
    }
}