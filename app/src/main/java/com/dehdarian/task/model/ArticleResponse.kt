package com.dehdarian.task.model


/**
 * Set the general data classes
 */
data class ArticleResponse(
     val totalArticles:Int,
     val articles:List<Article>
)
data class Article(
     val title:String,
     val description:String,
     val content:String,
     val url:String,
     val image:String,
     val publishedAt:String,
     val source: Source
)
data class Source(
     val name:String,
     val url:String
)