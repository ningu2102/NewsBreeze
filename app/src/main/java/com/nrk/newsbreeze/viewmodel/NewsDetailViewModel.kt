package com.nrk.newsbreeze.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrk.newsbreeze.data.mappers.ArticleToLocalArticleMapper
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val articleEventChannel = Channel<ArticleEvent>()
    val articleEvent = articleEventChannel.receiveAsFlow()

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            val mapper = ArticleToLocalArticleMapper()
            var localArticle = mapper.toLocalArticle(article)
            localArticle.addedDate = Date()
            newsRepository.insertArticle(localArticle)
            articleEventChannel.send(ArticleEvent.ShowArticleSavedMessage("Article Saved."))
        }
    }

    sealed class ArticleEvent{
        data class ShowArticleSavedMessage(val message: String): ArticleEvent()
    }

}