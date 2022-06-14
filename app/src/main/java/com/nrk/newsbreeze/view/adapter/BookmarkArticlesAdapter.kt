package com.nrk.newsbreeze.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.data.model.LocalArticle
import com.nrk.newsbreeze.databinding.BookmarkArticleItemPreviewBinding
import com.nrk.newsbreeze.databinding.ItemArticlePreviewBinding
import com.nrk.newsbreeze.utils.DateUtil
import java.util.*

class BookmarkArticlesAdapter(private val listener: OnItemClickListener): ListAdapter<LocalArticle, BookmarkArticlesAdapter.BookmarkArticleViewHolder>(DiffCallback()),
    Filterable {

    private var fullList: List<LocalArticle>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkArticleViewHolder {
        val binding = BookmarkArticleItemPreviewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return BookmarkArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    fun setList(list: MutableList<LocalArticle>?) {
        fullList = list
    }

    inner class BookmarkArticleViewHolder(private val binding: BookmarkArticleItemPreviewBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val article = getItem(position)
                        listener.onItemClicked(article)
                    }
                }
            }
        }

        fun bind(article: LocalArticle){
            binding.apply {
                Glide.with(itemView)
                    .load(article.urlToImage)
                    .into(ivArticleImage)
                tvTitle.text = article.title
                tvDate.text = DateUtil.changeDateFormat(article.publishedAt)
                tvAuthor.text = article.author
                tvHashTag.text = "#" + article.source!!.name
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(article: LocalArticle)
    }


    class DiffCallback : DiffUtil.ItemCallback<LocalArticle>(){
        override fun areItemsTheSame(oldItem: LocalArticle, newItem: LocalArticle): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: LocalArticle, newItem: LocalArticle): Boolean {
            return oldItem == newItem
        }

    }


    override fun getFilter(): Filter? {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<LocalArticle> = ArrayList()
            if (constraint.isEmpty()) {
                fullList?.let { filteredList.addAll(it) }
            } else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (store in fullList!!) {
                    if (store.title?.lowercase()!!.contains(filterPattern)) {
                        filteredList.add(store)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            submitList(results.values as MutableList<LocalArticle>)
//            notifyDataSetChanged()
        }
    }
}