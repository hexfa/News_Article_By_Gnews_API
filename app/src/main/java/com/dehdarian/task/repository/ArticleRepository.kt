package com.dehdarian.task.repository

import com.dehdarian.task.api.ArticleService
import javax.inject.Inject

/**
 * The repository layer to call method from network and database
 */
class ArticleRepository @Inject constructor(
    private val articleService: ArticleService
){
    /**
     * get all news
     */
    suspend fun getArticle(token:String) =
        articleService.getArticles(token)
    /**
     * search news by query
     */
    suspend fun search(token:String,query:String) =
        articleService.search(query,token)
    /**
     * search news by query,date and search type(title,description,content)
     */
    suspend fun searchByFilter(query:String,from:String,to:String,type:String,sortBy:String,token:String) =
        articleService.searchByFilter(query,from,to,type,sortBy,token)
}