package cz.legat.navigation

import android.content.Context
import android.content.Intent

interface SearchNavigator {
    fun getOpenSearchIntent(context: Context): Intent
}