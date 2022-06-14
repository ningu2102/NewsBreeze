package com.nrk.newsbreeze.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.data.model.LocalArticle

@Dao
interface ArticleDao {

    @Query("SELECT * FROM tblArticle")
    fun getArticles() : LiveData<List<LocalArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: LocalArticle) : Long

    @Delete
    suspend fun delete(article: LocalArticle)

    @Query("DELETE FROM tblArticle")
    suspend fun deleteAllArticles()
}