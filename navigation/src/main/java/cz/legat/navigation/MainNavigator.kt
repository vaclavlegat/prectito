package cz.legat.navigation

import android.content.Context
import android.content.Intent

interface MainNavigator {
    fun openMainIntent(context: Context): Intent
}