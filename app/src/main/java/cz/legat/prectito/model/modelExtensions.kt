package cz.legat.prectito.model

import cz.legat.prectito.model.google.VolumeInfo
import cz.legat.prectito.persistence.SavedBook

fun VolumeInfo.toSavedBook(): SavedBook{

    val title = this.title
    val subtitle = this.subtitle
    val publishedDate = this.publishedDate
    val language = this.language
    val author = this.authors[0]
    val isbn = this.industryIdentifiers.find { it.type == "ISBN_13" }?.identifier
        ?: this.industryIdentifiers[0].identifier
    val pageCount = this.pageCount

    return SavedBook(
        title = title,
        subtitle = subtitle,
        publishedDate = publishedDate,
        language = language,
        author = author,
        isbn = isbn,
        pageCount = pageCount
    )
}