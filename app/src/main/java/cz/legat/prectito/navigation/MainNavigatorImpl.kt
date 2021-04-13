package cz.legat.prectito.navigation

import android.content.Context
import android.content.Intent
import cz.legat.core.extensions.intent
import cz.legat.navigation.MainNavigator
import cz.legat.prectito.MainActivity

class MainNavigatorImpl : MainNavigator{
    override fun openMainIntent(context: Context): Intent {
        return context.intent<MainActivity>()
    }
}