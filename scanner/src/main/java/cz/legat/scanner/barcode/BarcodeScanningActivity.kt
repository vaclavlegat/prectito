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
import cz.legat.scanner.barcode.barcodedetection.BarcodeManualFragment
import cz.legat.scanner.barcode.barcodedetection.BarcodeProcessor
import cz.legat.scanner.barcode.barcodedetection.BarcodeResultFragment
import cz.legat.scanner.barcode.camera.CameraSource
import cz.legat.scanner.databinding.ActivityBarcodeScanningBinding
import cz.legat.scanner.ui.ISBNViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import timber.log.Timber
import java.io.IOException

/** Demonstrates the barcode scanning workflow using camera preview.  */
@AndroidEntryPoint
class BarcodeScanningActivity : AppCompatActivity(),
    BarcodeResultFragment.OnDismissResultDialogListener {

    private val viewModel: ISBNViewModel by viewModels()

    private var cameraSource: CameraSource? = null

    private lateinit var binding: ActivityBarcodeScanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScanningBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding.cameraPreviewGraphicOverlay.apply {
            cameraSource = CameraSource(this)
        }

        binding.cameraPreview.applySystemWindowInsetsToPadding(top = true)
        binding.topBar.applySystemWindowInsetsToPadding(top = true)

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
            when (workflowState) {
                ISBNViewModel.WorkflowState.DETECTING -> {
                    binding.bottomPromptChip.visible()
                    binding.bottomPromptChip.setText(R.string.prompt_point_at_a_barcode)
                    startCameraPreview()
                }
                ISBNViewModel.WorkflowState.DETECTED, ISBNViewModel.WorkflowState.SEARCHED -> {
                    binding.bottomPromptChip.gone()
                    stopCameraPreview()
                }
                else -> binding.bottomPromptChip.gone()
            }
        })

        viewModel.detectedBarcode.observe(this, Observer { barcode ->
            if (barcode != null) {
                viewModel.getBookByISBN(barcode.rawValue ?: "").observe(this, Observer {
                    it?.let {
                        BarcodeResultFragment.show(supportFragmentManager, it)
                    }
                })
            }
        })
    }

    override fun onDismissResultDialog() {
        viewModel.setWorkflowState(ISBNViewModel.WorkflowState.DETECTING)
    }
}
