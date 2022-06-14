package com.nrk.newsbreeze.view

import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.databinding.ActivityNewsDetailBinding
import com.nrk.newsbreeze.utils.DateUtil
import com.nrk.newsbreeze.viewmodel.NewsDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    private val viewModel: NewsDetailViewModel by viewModels()
    private lateinit var binding: ActivityNewsDetailBinding
    private lateinit var article: Article
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        getDataFromIntent()
        setUi()
    }

    private fun getDataFromIntent() {
        val gson = Gson()
        article = gson.fromJson(intent.getStringExtra("selectedArticle"), Article::class.java)
    }

    private fun setUi() {
        binding.apply {
            Glide.with(this@NewsDetailActivity)
                .load(article.urlToImage)
                .into(ivArticleImage)
            tvDate.text = DateUtil.changeDateFormat(article.publishedAt)
            tvTitle.text = article.title
            tvAuthor.text = article.author
            tvAuthorDesc.text = article.source!!.name
            tvDesc.text = article.description

            btnSave.setOnClickListener {
                viewModel.saveArticle(article)
                Toast.makeText(
                    this@NewsDetailActivity,
                    "Note saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}