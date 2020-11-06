package cz.legat.navigation

import android.content.Context
import android.content.Intent

interface BooksNavigator {
    fun getOpenDetailIntent(context: Context, id: String): Intent
    fun getOpenPdfIntent(context: Context, url:String): Intent
    fun getOpenBooksIntent(context: Context, type: String): Intent
}