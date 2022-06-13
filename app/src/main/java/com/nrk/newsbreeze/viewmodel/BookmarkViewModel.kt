package com.nrk.newsbreeze.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val savedArticleEventChannel = Channel<SavedArticleEvent>()
    val savedArticleEvent = savedArticleEventChannel.receiveAsFlow()

    fun getAllArticles() = newsRepository.getAllArticles()

    fun onArticleSwiped(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
            savedArticleEventChannel.send(SavedArticleEvent.ShowUndoDeleteArticleMessage(article))
        }
    }

    fun onUndoDeleteClick(article: Article) {
        viewModelScope.launch {
            newsRepository.insertArticle(article)
        }
    }

    sealed class SavedArticleEvent {
        data class ShowUndoDeleteArticleMessage(val article: Article) : SavedArticleEvent()
    }
}