package com.nrk.newsbreeze.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.data.model.LocalArticle
import com.nrk.newsbreeze.databinding.ActivityMainBinding
import com.nrk.newsbreeze.utils.QUERY_PAGE_SIZE
import com.nrk.newsbreeze.utils.Resource
import com.nrk.newsbreeze.view.adapter.ArticlesAdapter
import com.nrk.newsbreeze.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ArticlesAdapter.OnItemClickListener {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var articleAdapter: ArticlesAdapter
    private lateinit var localArticles: List<LocalArticle>
    private lateinit var articles: List<Article>
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide();
        setUi()
        setupRecyclerView()
        setupData()
    }

    private fun setupData() {
        viewModel.getAllArticles().observe(this) {
            localArticles = it
        }
    }

    private fun setUi() {
        binding.ivBookmark.setOnClickListener{
            startActivity(Intent(this@MainActivity, BookmarkActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticlesAdapter(this)

        binding.apply {
            rvBreakingNews.apply {
                adapter = articleAdapter
                setHasFixedSize(true)
            }
        }

        viewModel.breakingNews.observe(this) {
            when (it) {
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->
                        articles = newsResponse.articles
                        updateListParameters()
                        articleAdapter.submitList(newsResponse.articles.toList())
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = true
                    it.message?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        Log.e("TAG", "Error: $message")
                    }
                }
                is Resource.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateListParameters() {
        if(!localArticles.isNullOrEmpty()){
            for (article in articles){
                article.isSaved = localArticles.filter { s -> s.url == article.url }.size == 1
            }
            articleAdapter.submitList(articles.toList())
        }
    }

    override fun onItemClicked(article: Article) {
        val gson = Gson()
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra("selectedArticle", gson.toJson(article))
        intent.putExtra("from", "main")
        startActivity(intent)
    }

    override fun onReadClicked(article: Article) {
    }

    override fun onSaveClicked(article: Article) {
        this.lifecycleScope.launch(Dispatchers.IO) {
            var response = viewModel.saveNews(article)
            lifecycleScope.launch(Dispatchers.Main) {
                if (response != 0L) {
                    localArticles += 
                    updateListParameters()
                    Toast.makeText(
                        this@MainActivity,
                        "Article saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Article not saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}