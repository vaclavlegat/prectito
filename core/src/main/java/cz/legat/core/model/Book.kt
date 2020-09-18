package cz.legat.core.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Book(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: cz.legat.core.model.Author? = null,
    @Json(name = "description") val description: String,
    @Json(name = "isbn") val isbn: String? = null,
    @Json(name = "published") val published: String? = null,
    @Json(name = "numberOfPages") val numberOfPages: String? = null,
    @Json(name = "imgLink") val imgLink: String,
    @Json(name = "genre") val genre: String? = null,
    @Json(name = "language") val language: String? = null,
    @Json(name = "translator") val translator: String? = null,
    @Json(name = "publisher") val publisher: String? = null,
    @Json(name = "rating") val rating: String? = null,
    @Json(name = "ratingsCount") val ratingsCount: String? = null
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readParcelable(cz.legat.core.model.Author::class.java.classLoader),
                parcel.readString()!!,
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()!!,
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel?, flag: Int) {
                parcel?.writeString(id)
                parcel?.writeString(title)
                parcel?.writeParcelable(author, flag)
                parcel?.writeString(description)
                parcel?.writeString(isbn)
                parcel?.writeString(published)
                parcel?.writeString(numberOfPages)
                parcel?.writeString(imgLink)
                parcel?.writeString(genre)
                parcel?.writeString(language)
                parcel?.writeString(translator)
                parcel?.writeString(publisher)
                parcel?.writeString(rating)
                parcel?.writeString(ratingsCount)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Book> {
                override fun createFromParcel(parcel: Parcel): Book {
                        return Book(parcel)
                }

                override fun newArray(size: Int): Array<Book?> {
                        return arrayOfNulls(size)
                }
        }
}