package com.nrk.newsbreeze.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.databinding.ItemArticlePreviewBinding
import com.nrk.newsbreeze.utils.DateUtil

class ArticlesAdapter(private val listener: OnItemClickListener): ListAdapter<Article, ArticlesAdapter.ArticleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val article = getItem(position)
                        listener.onItemClicked(article)
                    }
                }
                btnRead.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val article = getItem(position)
                        listener.onReadClicked(article)
                    }
                }
                btnSave.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val article = getItem(position)
                        listener.onSaveClicked(article)
                    }
                }
            }
        }

        fun bind(article: Article){
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(ivArticleImage)
                tvDescription.text = article.description
                tvTitle.text = article.title
                tvPublishedAt.text = DateUtil.changeDateFormat(article.publishedAt)
//                btnRead.setOnClickListener {
//                    var intent: Intent = Intent(, NewsDetailActivity::class.java)
//                    intent.putExtra("selectedArticle", article)
//                    startActivity(intent)
//                }
//                tvSource.text = article.source?.name
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(article: Article)

        fun onReadClicked(article: Article)

        fun onSaveClicked(article: Article)
    }


    class DiffCallback : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
}