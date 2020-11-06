package cz.legat.books.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import cz.legat.books.ui.detail.DetailActivity
import cz.legat.core.extensions.intent
import cz.legat.navigation.BooksNavigator

internal class BooksNavigatorImpl: BooksNavigator {

    override fun getOpenDetailIntent(context: Context, id: String): Intent {
        return context.intent<DetailActivity>().putExtra("id", id)
    }

    override fun getOpenPdfIntent(context: Context, url: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = Uri.parse("content://$url")
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }
}