package com.nrk.newsbreeze.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import com.nrk.newsbreeze.data.model.LocalArticle
import com.nrk.newsbreeze.databinding.ActivityBookmarkBinding
import com.nrk.newsbreeze.view.adapter.BookmarkArticlesAdapter
import com.nrk.newsbreeze.viewmodel.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkActivity : AppCompatActivity(), BookmarkArticlesAdapter.OnItemClickListener {
    private val viewModel: BookmarkViewModel by viewModels()
    private lateinit var binding: ActivityBookmarkBinding
    private lateinit var articleAdapter: BookmarkArticlesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        setupUi()
        setupRecyclerView()
        setupData()
    }

    private fun setupUi() {
        binding.apply {

            ivBack.setOnClickListener {
                onBackPressed()
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

    private fun setupData() {
        viewModel.getAllArticles().observe(this) {
            binding.apply {
                if (it.isEmpty()) {
                    rvSavedNews.visibility = View.GONE
                    ivDown.visibility = View.GONE
                    txtNoData.visibility = View.VISIBLE
                } else {
                    rvSavedNews.visibility = View.VISIBLE
                    ivDown.visibility = View.VISIBLE
                    txtNoData.visibility = View.GONE
                    articleAdapter.submitList(it)
                    articleAdapter.setList(it as MutableList<LocalArticle>)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        articleAdapter = BookmarkArticlesAdapter(this)
        binding.apply {
            rvSavedNews.apply {
                adapter = articleAdapter
                setHasFixedSize(true)
            }
        }
    }

    override fun onItemClicked(article: LocalArticle) {
        val gson = Gson()
        val intent = Intent(this, NewsDetailActivity::class.java)
        intent.putExtra("from", "bookmark")
        intent.putExtra("selectedSavedArticle", gson.toJson(article))
        startActivity(intent)
    }
}