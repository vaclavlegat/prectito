package cz.legat.navigation

import android.content.Context
import android.content.Intent

interface AuthorsNavigator {
    fun getOpenDetailIntent(context: Context, id: String): Intent
    fun getCountryFilterIntent(context: Context): Intent
}