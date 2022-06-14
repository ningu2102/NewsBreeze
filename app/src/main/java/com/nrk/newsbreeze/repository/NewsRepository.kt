package com.nrk.newsbreeze.repository

import com.nrk.newsbreeze.data.local.ArticleDao
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.data.model.LocalArticle
import com.nrk.newsbreeze.data.model.NewsResponse
import com.nrk.newsbreeze.data.remote.NewsApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDao: ArticleDao
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return newsApi.getBreakingNews(countryCode,pageNumber)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse>{
        return newsApi.searchForNews(searchQuery, pageNumber)
    }

    fun getAllArticles() = articleDao.getArticles()

    suspend fun insertArticle(article: LocalArticle) = articleDao.insert(article)

    suspend fun deleteArticle(article: LocalArticle) = articleDao.delete(article)

    suspend fun deleteAllArticles() = articleDao.deleteAllArticles()
}