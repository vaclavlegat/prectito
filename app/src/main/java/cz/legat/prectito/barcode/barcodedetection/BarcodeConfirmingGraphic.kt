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

import android.graphics.Canvas
import android.graphics.Path
import com.google.mlkit.vision.barcode.Barcode
import cz.legat.prectito.barcode.camera.GraphicOverlay

/** Guides user to move camera closer to confirm the detected barcode.  */
internal class BarcodeConfirmingGraphic(overlay: GraphicOverlay, private val barcode: Barcode) :
    BarcodeGraphicBase(overlay) {

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val path = Path()
        // To have a completed path with all corners rounded.
        path.moveTo(boxRect.left, boxRect.top)
        path.lineTo(boxRect.right, boxRect.top)
        path.lineTo(boxRect.right, boxRect.bottom)
        path.lineTo(boxRect.left, boxRect.bottom)
        path.close()
        canvas.drawPath(path, pathPaint)
    }
}
