package com.nrk.newsbreeze.view

import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.databinding.ActivityNewsDetailBinding
import com.nrk.newsbreeze.viewmodel.NewsDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    private val viewModel: NewsDetailViewModel by viewModels()
    private lateinit var binding: ActivityNewsDetailBinding
    private lateinit var article: Article
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        article = intent.getParcelableExtra("selectedArticle")!!
        setUi()
    }

    private fun setUi() {
        binding.apply {
            val article = article
            webView.apply {
                webViewClient = WebViewClient()
                if (article != null) {
                    article.url?.let {
                        loadUrl(article.url.toString())
                    }
                }
            }

            fab.setOnClickListener {
                if (article != null) {
                    viewModel.saveArticle(article)
                }
                Toast.makeText(this@NewsDetailActivity, "Note saved successfully", Toast.LENGTH_SHORT).show()

            }
        }

//        this.lifecycleScope.launchWhenCreated {
//            viewModel.articleEvent.collect { event ->
//                when (event) {
//                    is ArticleViewModel.ArticleEvent.ShowArticleSavedMessage -> {
//                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

    }
}