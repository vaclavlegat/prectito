package cz.legat.prectito.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import cz.legat.prectito.R
import cz.legat.prectito.barcode.LiveBarcodeScanningActivity
import cz.legat.prectito.barcode.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ISBNScannerFragment : Fragment() {

    private lateinit var scanBtn: Button

    companion object {
        fun newInstance() : ISBNScannerFragment {
            return ISBNScannerFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pt_book_scan_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scanBtn = view.findViewById(R.id.pt_scan_btn)
        scanBtn.setOnClickListener {
            activity?.startActivity(Intent(activity, LiveBarcodeScanningActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(requireContext())) {
            Utils.requestRuntimePermissions(requireActivity())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
