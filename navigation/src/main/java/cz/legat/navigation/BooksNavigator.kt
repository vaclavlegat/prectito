package cz.legat.navigation

import android.content.Context
import android.content.Intent

interface BooksNavigator {
    fun getOpenDetailIntent(context: Context, id: String): Intent
}