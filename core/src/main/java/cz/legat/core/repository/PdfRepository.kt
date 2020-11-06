package cz.legat.core.repository

import okhttp3.ResponseBody

interface PdfRepository {

    suspend fun downloadPdf(url: String): ResponseBody?
}