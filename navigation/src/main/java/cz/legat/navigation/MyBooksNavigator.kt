package cz.legat.navigation

import android.content.Context
import android.content.Intent

interface MyBooksNavigator {
    fun getOpenScannerIntent(context: Context): Intent
}