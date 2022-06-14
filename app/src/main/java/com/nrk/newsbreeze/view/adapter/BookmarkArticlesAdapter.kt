package com.nrk.newsbreeze.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nrk.newsbreeze.data.model.LocalArticle
import com.nrk.newsbreeze.databinding.ItemArticlePreviewBinding
import com.nrk.newsbreeze.utils.DateUtil

class BookmarkArticlesAdapter(private val listener: OnItemClickListener): ListAdapter<LocalArticle, BookmarkArticlesAdapter.BookmarkArticleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return BookmarkArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class BookmarkArticleViewHolder(private val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root) {
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

        fun bind(article: LocalArticle){
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
        fun onItemClicked(article: LocalArticle)

        fun onReadClicked(article: LocalArticle)

        fun onSaveClicked(article: LocalArticle)
    }


    class DiffCallback : DiffUtil.ItemCallback<LocalArticle>(){
        override fun areItemsTheSame(oldItem: LocalArticle, newItem: LocalArticle): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: LocalArticle, newItem: LocalArticle): Boolean {
            return oldItem == newItem
        }

    }
}