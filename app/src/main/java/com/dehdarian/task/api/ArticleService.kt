package com.dehdarian.task.api

import com.dehdarian.task.model.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Define apis of articles
 */
interface ArticleService {

    @GET(Constants.ARTICLE)
    suspend fun getArticles(@Query("token")  token:String): Response<ArticleResponse>

    @GET(Constants.SEARCH)
    suspend fun search(@Query("q") query: String, @Query("token") token: String): Response<ArticleResponse>

    @GET(Constants.SEARCH)
    suspend fun searchByFilter(@Query("q")  query:String,
                               @Query("from")  from:String,
                               @Query("to")  to:String,
                               @Query("in")  type:String,
                               @Query("sortby")  sortby:String,
                               @Query("token")  token:String): Response<ArticleResponse>
}