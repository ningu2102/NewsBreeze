package com.nrk.newsbreeze.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nrk.newsbreeze.data.mappers.DateConverter
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.data.model.LocalArticle
import com.nrk.newsbreeze.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [LocalArticle::class], version = 3)
@TypeConverters(Converters::class, DateConverter::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    class Callback @Inject constructor(
        private val database: Provider<ArticleDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}