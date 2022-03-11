package com.dehdarian.task.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehdarian.task.api.Constants
import com.dehdarian.task.api.Resource
import com.dehdarian.task.model.ArticleResponse
import com.dehdarian.task.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SearchViewModel for SearchFragment
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    private val _articles = MutableLiveData<Resource<ArticleResponse>?>()
    var articles: MutableLiveData<Resource<ArticleResponse>?> = _articles
    private val _totalArticles = MutableLiveData<Int>()
    var totalArticles: LiveData<Int> = _totalArticles

    suspend fun search(query:String){
        viewModelScope.launch {
            val response = repository.search(Constants.TOKEN,query)
            if (response.isSuccessful){
                response.body()?.let {
                    _articles.postValue(Resource.Success(it))
                    _totalArticles.postValue( it.totalArticles)
                }
            }else{
                _articles.postValue(Resource.Error(""))
            }
        }

    }
    suspend fun searchByFilter(
        query: String,
        from: String,
        to: String,
        sortBy:String,
        type: String
    ) {
        viewModelScope.launch {
            val response = repository.searchByFilter(query, from, to, type,sortBy, Constants.TOKEN)
            if (response.isSuccessful) {
                response.body()?.let {
                    _articles.postValue(Resource.Success(it))
                    _totalArticles.postValue(it.totalArticles)
                }
            } else {
                _articles.postValue(Resource.Error(""))
            }

        }

    }

}