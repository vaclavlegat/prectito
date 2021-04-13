package cz.legat.scanner.barcode.barcodedetection

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cz.legat.core.persistence.SavedBook
import cz.legat.scanner.R
import cz.legat.scanner.databinding.BarcodeBottomSheetBinding
import cz.legat.scanner.ui.ISBNViewModel
import dagger.hilt.android.AndroidEntryPoint

/** Displays the bottom sheet to present barcode fields contained in the detected barcode.  */
@AndroidEntryPoint
class BarcodeResultFragment : BottomSheetDialogFragment() {

    interface OnDismissResultDialogListener {
        fun onDismissResultDialog()
    }

    private val viewModel: ISBNViewModel by viewModels()

    private lateinit var binding: BarcodeBottomSheetBinding
    private var dismissListener: OnDismissResultDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dismissListener = context as OnDismissResultDialogListener
    }

    override fun onDetach() {
        super.onDetach()
        dismissListener = null
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        binding = BarcodeBottomSheetBinding.inflate(layoutInflater, viewGroup, false)

        val book = arguments?.getParcelable<SavedBook>(BOOK)

        binding.ptCustomBookTitleEt.setText("${book?.title}${if (book?.subtitle != null) book.subtitle else ""}")
        binding.ptCustomBookAuthorEt.setText(book?.author)

        binding.ptCustomBookIsbnValueTv.text = book?.isbn

        binding.ptBookSaveBtn.apply {
            setOnClickListener {
                book?.let {
                    if (it.title.isNullOrEmpty()) {
                        viewModel.saveBook(
                            it.copy(
                                title = binding.ptCustomBookTitleEt.text.toString(),
                                author = binding.ptCustomBookAuthorEt.text.toString()
                            )
                        )
                    } else {
                        viewModel.saveBook(it)
                    }
                    Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_SHORT)
                        .show()
                    dismiss()
                }
            }
        }

        return binding.root
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        dismissListener?.onDismissResultDialog()
        super.onDismiss(dialogInterface)
    }

    companion object {

        private const val TAG = "BarcodeResultFragment"
        private const val BOOK = "book"

        fun show(
            fragmentManager: FragmentManager,
            savedBook: SavedBook
        ) {
            val barcodeResultFragment = BarcodeResultFragment()
            barcodeResultFragment.arguments = Bundle().apply {
                putParcelable(BOOK, savedBook)
            }
            barcodeResultFragment.show(fragmentManager, TAG)
        }

        fun dismiss(fragmentManager: FragmentManager) {
            (fragmentManager.findFragmentByTag(TAG) as BarcodeResultFragment?)?.dismiss()
        }
    }
}
