package cz.legat.scanner.barcode.barcodedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cz.legat.scanner.databinding.PtBarcodeManualBottomSheetBinding
import cz.legat.scanner.ui.ISBNViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeManualFragment : BottomSheetDialogFragment() {

    private val viewModel: ISBNViewModel by activityViewModels()
    private lateinit var binding: PtBarcodeManualBottomSheetBinding

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        binding = PtBarcodeManualBottomSheetBinding.inflate(layoutInflater, viewGroup, false)

        binding.ptBookSearchBtn.apply {
            setOnClickListener {
                viewModel.getBookByISBN(binding.ptCustomIsbnEt.text.toString())
                dismiss(parentFragmentManager)
            }
        }

        return binding.root
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
