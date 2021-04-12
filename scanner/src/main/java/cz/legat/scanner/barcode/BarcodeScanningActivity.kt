package cz.legat.scanner.barcode

import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import cz.legat.scanner.R
import cz.legat.scanner.barcode.barcodedetection.BarcodeField
import cz.legat.scanner.barcode.barcodedetection.BarcodeManualFragment
import cz.legat.scanner.barcode.barcodedetection.BarcodeProcessor
import cz.legat.scanner.barcode.barcodedetection.BarcodeResultFragment
import cz.legat.scanner.barcode.camera.CameraSource
import cz.legat.scanner.databinding.ActivityBarcodeScanningBinding
import cz.legat.scanner.ui.ISBNViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.IOException

/** Demonstrates the barcode scanning workflow using camera preview.  */
@AndroidEntryPoint
class BarcodeScanningActivity : AppCompatActivity() {

    private val viewModel: ISBNViewModel by viewModels()

    private var cameraSource: CameraSource? = null

    private lateinit var binding: ActivityBarcodeScanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScanningBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.cameraPreviewGraphicOverlay.apply {
            cameraSource = CameraSource(this)
        }

        binding.closeButton.setOnClickListener {
            onBackPressed()
        }
        binding.flashButton.apply {
            setOnClickListener {
                binding.flashButton.let {
                    if (it.isSelected) {
                        it.isSelected = false
                        cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                    } else {
                        it.isSelected = true
                        cameraSource!!.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                    }
                }
            }
        }

        binding.manualButton.apply {
            setOnClickListener {
                BarcodeManualFragment.show(supportFragmentManager)
            }
        }

        setUpWorkflowModel()
    }

    override fun onResume() {
        super.onResume()

        viewModel.markCameraFrozen()
        cameraSource?.setFrameProcessor(
            BarcodeProcessor(
                binding.cameraPreviewGraphicOverlay,
                viewModel
            )
        )
        viewModel.setWorkflowState(ISBNViewModel.WorkflowState.DETECTING)
    }

    override fun onPostResume() {
        super.onPostResume()
        BarcodeResultFragment.dismiss(supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
    }

    private fun startCameraPreview() {
        val cameraSource = this.cameraSource ?: return
        if (!viewModel.isCameraLive) {
            try {
                viewModel.markCameraLive()
                binding.cameraPreview.start(cameraSource)
            } catch (e: IOException) {
                Timber.e("Failed to start camera preview!")
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        if (viewModel.isCameraLive) {
            viewModel.markCameraFrozen()
            binding.flashButton.isSelected = false
            binding.cameraPreview.stop()
        }
    }

    private fun setUpWorkflowModel() {
        viewModel.workflowState.observe(this, Observer { workflowState ->

            val wasPromptChipGone = binding.bottomPromptChip.visibility == View.GONE

            when (workflowState) {
                ISBNViewModel.WorkflowState.DETECTING -> {
                    binding.bottomPromptChip.visible()
                    binding.bottomPromptChip.setText(R.string.prompt_point_at_a_barcode)
                    startCameraPreview()
                }
                ISBNViewModel.WorkflowState.CONFIRMING -> {
                    binding.bottomPromptChip.visible()
                    binding.bottomPromptChip.setText(R.string.prompt_move_camera_closer)
                    startCameraPreview()
                }
                ISBNViewModel.WorkflowState.SEARCHING -> {
                    binding.bottomPromptChip.visible()
                    binding.bottomPromptChip.setText(R.string.prompt_searching)
                    stopCameraPreview()
                }
                ISBNViewModel.WorkflowState.DETECTED, ISBNViewModel.WorkflowState.SEARCHED -> {
                    binding.bottomPromptChip.gone()
                    stopCameraPreview()
                }
                else -> binding.bottomPromptChip.gone()
            }
        })

        viewModel.searchedBook.observe(this,
            Observer { savedBook ->
                if (!savedBook.title.isNullOrEmpty()) {
                    val barcodeFieldList = ArrayList<BarcodeField>()
                    barcodeFieldList.add(BarcodeField("Title", savedBook.title ?: ""))
                    if (savedBook.subtitle?.isNotEmpty() == true) {
                        barcodeFieldList.add(BarcodeField("Subtitle", savedBook.subtitle!!))
                    }
                    barcodeFieldList.add(BarcodeField("Author", savedBook.author ?: ""))
                    barcodeFieldList.add(
                        BarcodeField(
                            "Published date",
                            savedBook.publishedDate?.take(4) ?: ""
                        )
                    )
                    barcodeFieldList.add(BarcodeField("ISBN", savedBook.isbn ?: ""))
                    BarcodeResultFragment.show(supportFragmentManager, barcodeFieldList, savedBook)
                } else {
                    val barcodeFieldList = ArrayList<BarcodeField>()
                    barcodeFieldList.add(BarcodeField("Title", ""))
                    barcodeFieldList.add(BarcodeField("ISBN", savedBook.isbn ?: ""))
                    BarcodeResultFragment.show(supportFragmentManager, barcodeFieldList, savedBook)
                }
            })

        viewModel.detectedBarcode.observe(this, Observer { barcode ->
            if (barcode != null) {
                viewModel.getBookByISBN(barcode.rawValue ?: "")
            }
        })
    }
}
