package cz.legat.scanner.navigation

import android.content.Context
import android.content.Intent
import cz.legat.navigation.MyBooksNavigator
import cz.legat.scanner.barcode.LiveBarcodeScanningActivity

class MyBooksNavigatorImpl: MyBooksNavigator {
    override fun getOpenScannerIntent(context: Context): Intent {
        return LiveBarcodeScanningActivity.intent(context)
    }
}