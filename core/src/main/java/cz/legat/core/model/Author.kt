package cz.legat.core.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Author(
    @Json(name = "authorId") val authorId: String? = null,
    @Json(name = "name") val name: String,
    @Json(name = "life") val life: String? = null,
    @Json(name = "authorImgLink") val authorImgLink: String? = null,
    @Json(name = "cv") val cv: String? = null,
    val books: List<Book>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Book.CREATOR)
    ) {
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(authorId)
        parcel?.writeString(name)
        parcel?.writeString(life)
        parcel?.writeString(authorImgLink)
        parcel?.writeString(cv)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Author> {
        override fun createFromParcel(parcel: Parcel): Author {
            return Author(parcel)
        }

        override fun newArray(size: Int): Array<Author?> {
            return arrayOfNulls(size)
        }
    }
}