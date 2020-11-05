package cz.legat.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.legat.core.model.Author
import cz.legat.core.model.Book
import cz.legat.core.model.SearchResult
import cz.legat.core.repository.AuthorsRepository
import cz.legat.core.repository.BooksRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    lateinit var booksRepository: BooksRepository

    @MockK
    lateinit var authorsRepository: AuthorsRepository

    @RelaxedMockK lateinit var booksObserver: Observer<List<SearchResult>>

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun searchTest() {
        val book1 = Book(id ="1",
            title = "ahoj",
            author = Author("2", "ahoj", "", ""),
            imgLink = "img",
            description = "")
        val book2 = Book(id ="1",
            title = "ahoj",
            author = Author("2", "ahoj", "", ""),
            imgLink = "img",
            description = "")
        val book3 = Book(id ="1",
            title = "ahoj",
            author = Author("2", "ahoj", "", ""),
            imgLink = "img",
            description = "")

        val author1 = Author("2", "ahoj", "", "")
        val author2 = Author("2", "ahoj", "", "")

        val books = listOf(book1, book2, book3)
        val authors = listOf(author1, author2)

        coEvery { booksRepository.searchBook("ahoj") } returns books
        coEvery { authorsRepository.searchAuthor("ahoj") } returns authors

        val viewModel = SearchResultsViewModel(booksRepository, authorsRepository).apply {
            searchBooks.observeForever(booksObserver)
        }

        viewModel.searchBook("ahoj")

        val all = mutableListOf<SearchResult>().apply {
            addAll(books)
            addAll(authors)
        }
        verify {
            booksObserver.onChanged(
                all
            )
        }
    }
}