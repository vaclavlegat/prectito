package cz.legat.scanner.barcode.barcodedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cz.legat.scanner.R
import cz.legat.scanner.ui.ISBNViewModel
import dagger.hilt.android.AndroidEntryPoint

/** Displays the bottom sheet to present barcode fields contained in the detected barcode.  */
@AndroidEntryPoint
class BarcodeManualFragment : BottomSheetDialogFragment() {

    private val viewModel: ISBNViewModel by activityViewModels()

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.pt_barcode_manual_bottom_sheet, viewGroup)

        view.findViewById<Button>(R.id.pt_book_search_btn).apply {
            setOnClickListener {
                viewModel.getBookByISBN(view.findViewById<EditText>(R.id.pt_custom_isbn_et).text.toString())
                dismiss(parentFragmentManager)
            }
        }

        return view
    }

    companion object {

        private const val TAG = "BarcodeManualFragment"

        fun show(
            fragmentManager: FragmentManager
        ) {
            val barcodeManualFragment = BarcodeManualFragment()
            barcodeManualFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as BarcodeManualFragment?)?.dismiss()
        }
    }
}
