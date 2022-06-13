package com.nrk.newsbreeze.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.nrk.newsbreeze.R
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.databinding.ActivityBookmarkBinding
import com.nrk.newsbreeze.databinding.ActivityNewsDetailBinding
import com.nrk.newsbreeze.view.adapter.ArticlesAdapter
import com.nrk.newsbreeze.viewmodel.BookmarkViewModel
import com.nrk.newsbreeze.viewmodel.NewsDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkActivity : AppCompatActivity(), ArticlesAdapter.OnItemClickListener {
    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var articleAdapter: ArticlesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupData()
    }

    private fun setupData() {
        viewModel.getAllArticles().observe(this) {
            articleAdapter.submitList(it)
        }

    }

    private fun setupRecyclerView() {
        articleAdapter = ArticlesAdapter(this)
        binding.apply {
            rvSavedNews.apply {
                adapter = articleAdapter
                setHasFixedSize(true)
            }
        }
    }

    override fun onItemClicked(article: Article) {

    }

    override fun onReadClicked(article: Article) {
    }

    override fun onSaveClicked(article: Article) {
    }
}