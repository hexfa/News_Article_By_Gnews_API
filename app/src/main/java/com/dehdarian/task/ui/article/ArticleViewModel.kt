package com.dehdarian.task.ui.article

import androidx.lifecycle.*
import com.dehdarian.task.api.Constants
import com.dehdarian.task.api.Resource
import com.dehdarian.task.model.ArticleResponse
import com.dehdarian.task.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * ArticleViewModel for ArticleFragment
 */
@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository
): ViewModel() {

    private val _articles = MutableLiveData<Resource<ArticleResponse>>()
    var articles: LiveData<Resource<ArticleResponse>> = _articles

    init {
        viewModelScope.launch {
            val response = articleRepository.getArticle(Constants.TOKEN)
            if (response.isSuccessful){
                response.body()?.let {
                    _articles.postValue(Resource.Success(it))
                }
            }else{
                _articles.postValue(Resource.Error(""))
            }
        }
    }

}