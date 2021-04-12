package cz.legat.scanner.barcode.barcodedetection

import androidx.annotation.MainThread
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import cz.legat.scanner.barcode.InputInfo
import cz.legat.scanner.barcode.camera.CameraReticleAnimator
import cz.legat.scanner.barcode.camera.FrameProcessorBase
import cz.legat.scanner.barcode.camera.GraphicOverlay
import cz.legat.scanner.ui.ISBNViewModel
import timber.log.Timber
import java.io.IOException

/** A processor to run the barcode detector.  */
class BarcodeProcessor(graphicOverlay: GraphicOverlay, private val workflowModel: ISBNViewModel) :
    FrameProcessorBase<List<Barcode>>() {

    private val scanner = BarcodeScanning.getClient()
    private val cameraReticleAnimator: CameraReticleAnimator = CameraReticleAnimator(graphicOverlay)

    override fun detectInImage(image: InputImage): Task<List<Barcode>> = scanner.process(image)

    @MainThread
    override fun onSuccess(
        inputInfo: InputInfo,
        results: List<Barcode>,
        graphicOverlay: GraphicOverlay
    ) {

        if (!workflowModel.isCameraLive) return

        Timber.d("Barcode result size: ${results.size}")

        // Picks the barcode, if exists, that covers the center of graphic overlay.

        val barcodeInCenter = results.firstOrNull { barcode ->
            val boundingBox = barcode.boundingBox ?: return@firstOrNull false
            val box = graphicOverlay.translateRect(boundingBox)
            box.contains(graphicOverlay.width / 2f, graphicOverlay.height / 2f)
        }

        graphicOverlay.clear()
        if (barcodeInCenter == null) {
            cameraReticleAnimator.start()
            graphicOverlay.add(BarcodeReticleGraphic(graphicOverlay, cameraReticleAnimator))
            workflowModel.setWorkflowState(ISBNViewModel.WorkflowState.DETECTING)
        } else {
            cameraReticleAnimator.cancel()
            workflowModel.setWorkflowState(ISBNViewModel.WorkflowState.DETECTED)
            workflowModel.detectedBarcode.setValue(barcodeInCenter)
        }
        graphicOverlay.invalidate()
    }

    override fun onFailure(e: Exception) {
        Timber.e("Barcode detection failed!")
    }

    override fun stop() {
        super.stop()
        try {
            scanner.close()
        } catch (e: IOException) {
            Timber.e("Failed to close barcode detector!")
        }
    }
}
