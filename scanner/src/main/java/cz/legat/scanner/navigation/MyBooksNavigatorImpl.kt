package cz.legat.scanner.navigation

import android.content.Context
import android.content.Intent
import cz.legat.core.extensions.intent
import cz.legat.navigation.MyBooksNavigator
import cz.legat.scanner.barcode.BarcodeScanningActivity

internal class MyBooksNavigatorImpl: MyBooksNavigator {
    override fun getOpenScannerIntent(context: Context): Intent {
        return context.intent<BarcodeScanningActivity>()
    }
}