package cz.legat.core

import cz.legat.core.model.Author
import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

open class HtmlParser {

    open fun parseBooksPopular(html: String): List<Book> {
        val document = Jsoup.parse(html)

        val titles = document.select("a[class=biggest odr]")
        val imgAttrs = document.select("img[class=inahled4]")
        val authors = document.select("span[class=pozn]")
        val shortDescs = document.select("p[class=odtop_ten new2]")

        val popularBooks = mutableListOf<Book>()

        for ((index, element) in imgAttrs.withIndex()) {
            val id = titles[index].select("a").attr("href").removePrefix("knihy/")
            val title = titles[index].text()
            val imgSrc = element.attr("src")
            popularBooks.add(
                Book(
                    id = id,
                    title = title,
                    author = Author(name = authors[index].text(), authorId = "", authorImgLink = ""),
                    description = shortDescs[index].text(),
                    imgLink = imgSrc
                )
            )
        }

        return popularBooks
    }

    open fun parseBook(id: String, html: String): Book {
        val document = Jsoup.parse(html)
        return Book(
            id,
            parseBookTitle(document),
            parseBookAuthor(document),
            parseBookDescription(document),
            parseBookIsbn(document),
            parseBookPublishedDate(document),
            parseBookNumberOfPages(document),
            parseBookImgLink(document),
            parseGenre(document),
            parseLanguage(document),
            parseTranslator(document),
            parsePublisher(document),
            parseRating(document),
            parseRatingsCount(document)
        )
    }

    //<h1 itemprop="name">R.U.R.</h1>
    private fun parseBookTitle(document: Document): String {
        return document.select("h1[itemprop=name]").text()
    }

    //<h2 class="jmenaautoru"><em>kniha od:</em> <span itemprop="author"><a href="autori/karel-capek-101">Karel Čapek</a></span></h2>
    private fun parseBookAuthor(document: Document): Author {
        val author = document.select("span[itemprop=author]")
        val id = document.select("span[itemprop=author] > a").attr("href").removePrefix("autori/")

        return Author(id, author.text(),"","")
    }

    //<p id="bdetdesc" class="justify new2 odtop_big" itemprop="description">
    private fun parseBookDescription(document: Document): String {
        return document.select("p[itemprop=description]").text()
    }

    private fun parseBookIsbn(document: Document): String {
        return document.select("span[itemprop=isbn]").text()
    }

    private fun parseBookPublishedDate(document: Document): String {
        return document.select("span[itemprop=datePublished]").text()
    }

    private fun parseBookNumberOfPages(document: Document): String {
        return document.select("td[itemprop=numberOfPages]").text()
    }

    private fun parseLanguage(document: Document): String {
        return document.select("td[itemprop=language]").text()
    }

    private fun parseTranslator(document: Document): String? {
        val translator = document.select("td[itemprop=translator]").text()
        return if (translator.isEmpty()) null else translator
    }

    private fun parsePublisher(document: Document): String {
        return document.select("span[itemprop=publisher]").text()
    }

    private fun parseGenre(document: Document): String {
        return document.select("h5[itemprop=genre]").text()
    }

    private fun parseBookImgLink(document: Document): String {
        return document.select("img[class=kniha_img]").attr("src")
    }

    private fun parseRating(document: Document): String {
        val rating2 = document.select("div[class=hodnoceni_2]")
        val rating3 = document.select("div[class=hodnoceni_3]")
        val rating4 = document.select("div[class=hodnoceni_4]")

        if (rating2.isNotEmpty()) {
            return rating2.text().removeSuffix(" %")
        }
        if (rating3.isNotEmpty()) {
            return rating3.text().removeSuffix(" %")
        }
        if (rating4.isNotEmpty()) {
            return rating4.text().removeSuffix(" %")
        }

        return "0"
    }

    private fun parseRatingsCount(document: Document): String {
        return document.select("a[class=bpointsm]").text()
    }

    private fun parseBookSearchResult(element: Element): Book {
        val id = element.select("a[class=new]").attr("href").removePrefix("knihy/")
        val title = element.select("a[class=new]").text()
        val shortDesc = element.select("span[class=smallfind]").text()
        val imgLink = element.select("img").attr("src")
        return Book(id = id, title = title, description = shortDesc, imgLink = imgLink)
    }

    open fun parseBookSearchResults(html: String): List<Book> {
        val document = Jsoup.parse(html)
        println(document.body().html())
        val searchElements = document.select("p[class=new]")
        if (searchElements.isEmpty()) {
            return listOf()
        }
        searchElements.removeAt(0)

        val results = mutableListOf<Book>()

        for (element in searchElements) {
            results.add(parseBookSearchResult(element))
        }

        return results
    }

    open fun parseAuthors(html: String): List<Author> {
        val document = Jsoup.parse(html)
        val authors = document.select("div[class=autbox]")

        val results = mutableListOf<Author>()

        for (element in authors) {
            results.add(parseAuthor(element))
        }

        return results
    }

    open fun parseAuthor(element: Element): Author {
        val id = element.select("a").attr("href").removePrefix("autori/")
        val name = element.select("a").text()
        val life = element.select("span[class=pozn_light]").text()
        val imgLink = element.select("div[class=circle_aut]").attr("style")
            .removePrefix("background-image:url(").removeSuffix(")")

        return Author(id, name, life, imgLink)
    }

    open fun parseAuthorDetail(id: String, html: String): Author {
        val document = Jsoup.parse(html)
        val name = document.select("h1[itemprop=name]").text()

        val regex = "\\d{4}".toRegex()
        val regexEnd = "- \\d{4}".toRegex()
        val lifeStart = regex.find(document.select("h3[class=norma]").text())
        val lifeEnd = regexEnd.find(document.select("h3[class=norma]").text())

        val life = lifeStart?.value + if (lifeEnd?.value != null) " ${lifeEnd.value}" else ""

        val cv = document.select("div[id=tabcontent]").select("p[class=new2]").text()
        val imgLink = document.select("div[class=circle-avatar]").attr("style")
            .removePrefix("background-image:url(").removeSuffix(")")

        return Author(id, name, life, imgLink, cv)
    }

    open fun parseAuthorBooks(html: String, page: Int): List<Book> {
        val document = Jsoup.parse(html)
        val pager = document.select("div[class=pager]")

        if (pager.isNotEmpty()) {
            val nextPages = pager.first().children()

            var lastPage = 0
            nextPages.forEach {
                try {
                    lastPage = Integer.parseInt(it.text())
                } catch (e: NumberFormatException) {

                }
            }

            if (page > lastPage) {
                return mutableListOf()
            }
        } else {
            if (page > 1) {
                return mutableListOf()
            }
        }

        val searchElements =
            document.select("p[class=new2]").subList(0, document.select("p[class=new2]").size - 2)
        val images = document.select("img[class=inahled4]")
        val results = mutableListOf<Book>()
        searchElements.forEachIndexed { index, element ->
            results.add(parseAuthorBook(element, images[index].attr("src")))
        }
        return results
    }

    open fun parseAuthorBook(element: Element, imgLink: String): Book {
        val id = element.select("a[class=bigger]").attr("href").removePrefix("knihy/")
        val title = element.select("a[class=bigger]").text()
        val shortDesc = element.select("span[class=pozn odl]").text()
        return Book(
            id = id,
            title = title,
            author = Author(name = shortDesc,authorId = "", authorImgLink = ""),
            description = shortDesc,
            imgLink = imgLink
        )
    }

    open fun parseBookComments(html: String): List<Comment> {
        val document = Jsoup.parse(html)
        val comments = document.select("div[class=komentars_user komover]")
        val commentsLast = document.select("div[class=komentars_user_last komover]")

        comments.addAll(commentsLast)

        val results = mutableListOf<Comment>()
        comments.forEach { element ->
            results.add(parseBookComment(element))
        }
        return results
    }

    private fun parseBookComment(element: Element): Comment {
        val id = element.attr("id")
        val user = element.select("div[class=komholdu]").select("a").text()
        val comment = element.select("div[class=komholdu]").select("p").text()
        val avatarLink = element.select("img[class=comimg]").attr("src")
        val date =
            element.select("div[class=komholdu]").select("span[class=pozn_lightest odleft_pet]")
                .text()
        val likes = try {
            Integer.parseInt(element.select("div[class=komholdu]").select("em[class=likes]").text())
        } catch (e: NumberFormatException) {
            0
        }
        val rating = try {

            val trash = element.select("div[class=komholdu]").select("span[class=odpad odl abs]")
            if (trash.isNotEmpty()) {
                0
            } else {
                val stars = Integer.parseInt(
                    element.select("img[class=odl abs]").attr("src").removeSuffix(".png")
                        .removePrefix("https://www.databazeknih.cz/img/content/points/")
                )
                stars
            }
        } catch (e: NumberFormatException) {
            0
        }

        return Comment(id, user, comment, avatarLink, date, likes, rating)
    }

    open fun parseAuthorQuotes(html: String): List<String> {
        val document = Jsoup.parse(html)
        val quotes = document.select("cite")

        val results = mutableListOf<String>()
        quotes.forEach { element ->
            results.add(element.text())
        }

        return results
    }
}