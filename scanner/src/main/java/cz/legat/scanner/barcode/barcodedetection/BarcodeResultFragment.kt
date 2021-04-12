package cz.legat.scanner.barcode.barcodedetection

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cz.legat.core.persistence.SavedBook
import cz.legat.scanner.R
import cz.legat.scanner.ui.ISBNViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/** Displays the bottom sheet to present barcode fields contained in the detected barcode.  */
@AndroidEntryPoint
class BarcodeResultFragment : BottomSheetDialogFragment() {

    private val viewModel: ISBNViewModel by viewModels()
    private var book: SavedBook? = null

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        val view = layoutInflater.inflate(R.layout.barcode_bottom_sheet, viewGroup)

        val arguments = arguments
        val barcodeFieldList: ArrayList<BarcodeField> =
            if (arguments?.containsKey(ARG_BARCODE_FIELD_LIST) == true) {
                arguments.getParcelableArrayList(ARG_BARCODE_FIELD_LIST) ?: ArrayList()
            } else {
                Timber.e("No barcode field list passed in!")
                ArrayList()
            }

        book = if (arguments?.containsKey("book") == true) {
            arguments.getParcelable<SavedBook>("book")
        } else {
            null
        }

        view.findViewById<RecyclerView>(R.id.barcode_field_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = BarcodeFieldAdapter(barcodeFieldList)
        }.visibility = if (!book?.title.isNullOrEmpty()) View.VISIBLE else View.GONE

        view.findViewById<LinearLayout>(R.id.pt_custom_book_holder_ll).visibility =
            if (!book?.title.isNullOrEmpty()) View.GONE else View.VISIBLE
        view.findViewById<TextView>(R.id.pt_custom_book_isbn_value_tv).text = book?.isbn


        view.findViewById<Button>(R.id.pt_book_save_btn).apply {
            setOnClickListener {
                book?.let {
                    if (it.title.isNullOrEmpty()) {
                        viewModel.saveBook(
                            it.copy(
                                title = view.findViewById<EditText>(R.id.pt_custom_book_title_et).text.toString(),
                                author = view.findViewById<EditText>(R.id.pt_custom_book_author_et).text.toString()
                            )
                        )
                    } else {
                        viewModel.saveBook(it)
                    }
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }

        return view
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        activity?.let {
            // Back to working state after the bottom sheet is dismissed.
            viewModel.setWorkflowState(ISBNViewModel.WorkflowState.DETECTING)
        }
        super.onDismiss(dialogInterface)
    }

    companion object {

        private const val TAG = "BarcodeResultFragment"
        private const val ARG_BARCODE_FIELD_LIST = "arg_barcode_field_list"

        fun show(
            fragmentManager: FragmentManager,
            barcodeFieldArrayList: ArrayList<BarcodeField>,
            savedBook: SavedBook
        ) {
            val barcodeResultFragment = BarcodeResultFragment()
            barcodeResultFragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_BARCODE_FIELD_LIST, barcodeFieldArrayList)
                putParcelable("book", savedBook)
            }
            barcodeResultFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as BarcodeResultFragment?)?.dismiss()
        }
    }
}
