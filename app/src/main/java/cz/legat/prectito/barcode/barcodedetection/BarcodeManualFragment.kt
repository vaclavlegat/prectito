/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.legat.prectito.barcode.barcodedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cz.legat.prectito.R
import cz.legat.prectito.ui.main.my.ISBNViewModel
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
