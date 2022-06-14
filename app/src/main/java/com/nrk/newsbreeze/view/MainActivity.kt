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
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide();
        setUi()
        setupRecyclerView()
    }

    private fun setUi() {
        binding.ivBookmark.setOnClickListener{
            startActivity(Intent(this@MainActivity, BookmarkActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        val articleAdapter = ArticlesAdapter(this)

        binding.apply {
            rvBreakingNews.apply {
                adapter = articleAdapter
                setHasFixedSize(true)
                //addOnScrollListener(this@MainActivity.scrollListener)
            }
        }

        viewModel.breakingNews.observe(this) {
            when (it) {
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { newsResponse ->
                        articleAdapter.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage)
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)
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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { //State is scrolling
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalVisibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + totalVisibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews("tr")
                isScrolling = false
            }
        }
    }


    override fun onItemClicked(article: Article) {
        val gson = Gson()
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra("selectedArticle", gson.toJson(article))
        startActivity(intent)
    }

    override fun onReadClicked(article: Article) {
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
}