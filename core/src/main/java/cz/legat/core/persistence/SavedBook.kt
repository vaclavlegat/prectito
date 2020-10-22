package cz.legat.core.persistence

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedBook(
    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "subtitle") val subtitle: String? = null,
    @ColumnInfo(name = "author") val author: String? = null,
    @ColumnInfo(name = "language") val language: String? = null,
    @ColumnInfo(name = "isbn") val isbn: String? = null,
    @ColumnInfo(name = "published_date") val publishedDate: String? = null,
    @ColumnInfo(name = "page_count") val pageCount: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid?:0)
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(author)
        parcel.writeString(language)
        parcel.writeString(isbn)
        parcel.writeString(publishedDate)
        parcel.writeString(pageCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SavedBook> {
        override fun createFromParcel(parcel: Parcel): SavedBook {
            return SavedBook(parcel)
        }

        override fun newArray(size: Int): Array<SavedBook?> {
            return arrayOfNulls(size)
        }
    }
}