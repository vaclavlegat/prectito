package cz.legat.books.repository

import cz.legat.books.data.remote.PdfService
import cz.legat.core.base.BaseRepository
import cz.legat.core.repository.PdfRepository
import okhttp3.ResponseBody
import javax.inject.Inject

class PdfRepositoryImpl @Inject constructor(
    private val pdfService: PdfService
) : PdfRepository, BaseRepository() {

    override suspend fun downloadPdf(url: String) : ResponseBody? {
        return pdfService.downloadPdf(url.removePrefix("https://web2.mlp.cz/")).body()
    }
}