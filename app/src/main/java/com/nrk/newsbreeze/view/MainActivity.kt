package com.nrk.newsbreeze.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.data.model.LocalArticle
import com.nrk.newsbreeze.databinding.ActivityMainBinding
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
        supportActionBar!!.hide()
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
        binding.apply {

            ivBookmark.setOnClickListener {
                startActivity(Intent(this@MainActivity, BookmarkActivity::class.java))
            }

            svQuery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        Log.i("SearchQuery", "onQueryTextChange: EMPTY")
                        articleAdapter.filter!!.filter("")
                    } else {
                        Log.i("SearchQuery", "onQueryTextChange: search text is $newText")
                        articleAdapter.filter!!.filter(newText)
                    }
                    return true
                }
            })
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
                    binding.progrssBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->
                        articles = newsResponse.articles
                        binding.apply {
                            txtNoData.visibility = View.GONE
                            rvBreakingNews.visibility = View.VISIBLE
                        }
                        updateListParameters()
                        articleAdapter.submitList(newsResponse.articles.toMutableList())
                        articleAdapter.setList(newsResponse.articles.toMutableList())
                    }
                }
                is Resource.Error -> {
                    binding.progrssBar.visibility = View.INVISIBLE
                    isLoading = true
                    it.message?.let { message ->
                        binding.apply {
                            txtNoData.visibility = View.VISIBLE
                            rvBreakingNews.visibility = View.GONE
                            txtNoData.text = "You have no internt connection, Please turn on the data and restart the app"
                        }
//                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                        Log.e("TAG", "Error: $message")
                    }
                }
                is Resource.Loading -> {
                    binding.progrssBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateListParameters() {
        if (!localArticles.isNullOrEmpty()) {
            for (article in articles) {
                article.isSaved = localArticles.filter { s -> s.url == article.url }.size == 1
            }
            articleAdapter.submitList(articles.toMutableList())
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
        val gson = Gson()
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra("selectedArticle", gson.toJson(article))
        intent.putExtra("from", "main")
        startActivity(intent)
    }

    override fun onSaveClicked(article: Article) {
        this.lifecycleScope.launch(Dispatchers.IO) {
            var response = viewModel.saveNews(article)
            lifecycleScope.launch(Dispatchers.Main) {
                if (response != 0L) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}