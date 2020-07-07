package cz.legat.prectito.model

data class Comment(
    val id: String,
    val user: String,
    val comment: String,
    val avatarLink: String,
    val date: String,
    val likes: Int?,
    val rating: Int
)