package cz.legat.core.model

data class Overview(val popularBooks: List<Book>, val newBooks: List<Book>, val eBooks: List<Book>)