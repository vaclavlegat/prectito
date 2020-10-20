package cz.legat.prectito.persistence

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.legat.core.model.Book
import java.lang.reflect.Type
import java.util.Date


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromBooksJson(value: String): List<Book> {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()

        val type: Type = Types.newParameterizedType(List::class.java, Book::class.java)
        val jsonAdapter: JsonAdapter<List<Book>> = moshi.adapter(type)

        return jsonAdapter.fromJson(value)?: listOf()
    }

    @TypeConverter
    fun toBooksJson(books: List<Book>): String {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()

        val type: Type = Types.newParameterizedType(List::class.java, Book::class.java)
        val jsonAdapter: JsonAdapter<List<Book>> = moshi.adapter(type)

        return jsonAdapter.toJson(books)
    }
}