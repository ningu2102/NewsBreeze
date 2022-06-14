package com.nrk.newsbreeze.data.mappers

import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.data.model.LocalArticle

class ArticleToLocalArticleMapper {
    companion object Factory {
        fun create(): ArticleToLocalArticleMapper = ArticleToLocalArticleMapper()
    }

    fun toArticle(localArticle: LocalArticle): Article {
        return Article(
            localArticle.author,
            localArticle.content,
            localArticle.description,
            localArticle.publishedAt,
            localArticle.source,
            localArticle.title,
            localArticle.url,
            localArticle.urlToImage,
            null
        )
    }

    fun toLocalArticle(article: Article): LocalArticle {
        return LocalArticle(
            null,
            article.author,
            article.content,
            article.description,
            article.publishedAt,
            article.source,
            article.title,
            article.url,
            article.urlToImage,
            null
        )

    }
}