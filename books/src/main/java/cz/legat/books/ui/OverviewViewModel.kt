package cz.legat.books.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import cz.legat.core.repository.BooksRepository

class OverviewViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository
) : ViewModel() {

}
