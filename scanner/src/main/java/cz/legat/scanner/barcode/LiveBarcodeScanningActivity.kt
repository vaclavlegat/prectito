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

package cz.legat.scanner.barcode

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.common.base.Objects
import cz.legat.scanner.R
import cz.legat.scanner.barcode.barcodedetection.BarcodeField
import cz.legat.scanner.barcode.barcodedetection.BarcodeManualFragment
import cz.legat.scanner.barcode.barcodedetection.BarcodeProcessor
import cz.legat.scanner.barcode.barcodedetection.BarcodeResultFragment
import cz.legat.scanner.barcode.camera.CameraSource
import cz.legat.scanner.barcode.camera.CameraSourcePreview
import cz.legat.scanner.barcode.camera.GraphicOverlay
import cz.legat.scanner.barcode.camera.WorkflowModel
import cz.legat.scanner.ui.ISBNViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

/** Demonstrates the barcode scanning workflow using camera preview.  */
@AndroidEntryPoint
class LiveBarcodeScanningActivity : AppCompatActivity(), OnClickListener {

    private val viewModel: ISBNViewModel by viewModels()

    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var flashButton: View? = null
    private var manualButton: View? = null
    private var promptChip: Chip? = null
    private var promptChipAnimator: AnimatorSet? = null
    private var workflowModel: WorkflowModel? = null
    private var currentWorkflowState: WorkflowModel.WorkflowState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_live_barcode_kotlin)
        preview = findViewById(R.id.camera_preview)
        graphicOverlay = findViewById<GraphicOverlay>(R.id.camera_preview_graphic_overlay).apply {
            setOnClickListener(this@LiveBarcodeScanningActivity)
            cameraSource = CameraSource(this)
        }

        promptChip = findViewById(R.id.bottom_prompt_chip)
        promptChipAnimator =
            (AnimatorInflater.loadAnimator(
                this,
                R.animator.bottom_prompt_chip_enter
            ) as AnimatorSet).apply {
                setTarget(promptChip)
            }

        findViewById<View>(R.id.close_button).setOnClickListener(this)
        flashButton = findViewById<View>(R.id.flash_button).apply {
            setOnClickListener(this@LiveBarcodeScanningActivity)
        }

        manualButton = findViewById<View>(R.id.manual_button).apply {
            setOnClickListener(this@LiveBarcodeScanningActivity)
        }

        setUpWorkflowModel()
    }

    override fun onResume() {
        super.onResume()

        workflowModel?.markCameraFrozen()
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED
        cameraSource?.setFrameProcessor(BarcodeProcessor(graphicOverlay!!, workflowModel!!))
        workflowModel?.setWorkflowState(WorkflowModel.WorkflowState.DETECTING)
    }

    override fun onPostResume() {
        super.onPostResume()
        BarcodeResultFragment.dismiss(supportFragmentManager)
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.close_button -> onBackPressed()
            R.id.flash_button -> {
                flashButton?.let {
                    if (it.isSelected) {
                        it.isSelected = false
                        cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                    } else {
                        it.isSelected = true
                        cameraSource!!.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                    }
                }
            }
            R.id.manual_button -> {
                BarcodeManualFragment.show(supportFragmentManager)
            }
        }
    }

    private fun startCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        val cameraSource = this.cameraSource ?: return
        if (!workflowModel.isCameraLive) {
            try {
                workflowModel.markCameraLive()
                preview?.start(cameraSource)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to start camera preview!", e)
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        if (workflowModel.isCameraLive) {
            workflowModel.markCameraFrozen()
            flashButton?.isSelected = false
            preview?.stop()
        }
    }

    private fun setUpWorkflowModel() {
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel::class.java)

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel!!.workflowState.observe(this, Observer { workflowState ->
            if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                return@Observer
            }

            currentWorkflowState = workflowState
            Log.d(TAG, "Current workflow state: ${currentWorkflowState!!.name}")

            val wasPromptChipGone = promptChip?.visibility == View.GONE

            when (workflowState) {
                WorkflowModel.WorkflowState.DETECTING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_point_at_a_barcode)
                    startCameraPreview()
                }
                WorkflowModel.WorkflowState.CONFIRMING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_move_camera_closer)
                    startCameraPreview()
                }
                WorkflowModel.WorkflowState.SEARCHING -> {
                    promptChip?.visibility = View.VISIBLE
                    promptChip?.setText(R.string.prompt_searching)
                    stopCameraPreview()
                }
                WorkflowModel.WorkflowState.DETECTED, WorkflowModel.WorkflowState.SEARCHED -> {
                    promptChip?.visibility = View.GONE
                    stopCameraPreview()
                }
                else -> promptChip?.visibility = View.GONE
            }

            val shouldPlayPromptChipEnteringAnimation =
                wasPromptChipGone && promptChip?.visibility == View.VISIBLE
            promptChipAnimator?.let {
                if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
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

        workflowModel?.detectedBarcode?.observe(this, Observer { barcode ->
            if (barcode != null) {
                viewModel.getBookByISBN(barcode.rawValue ?: "")
            }
        })
    }

    companion object {
        private const val TAG = "LiveBarcodeActivity"

        fun intent(context: Context): Intent {
            return Intent(context, LiveBarcodeScanningActivity::class.java)
        }
    }
}
