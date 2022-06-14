package com.nrk.newsbreeze.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nrk.newsbreeze.R
import com.nrk.newsbreeze.data.model.Article
import com.nrk.newsbreeze.databinding.ItemArticlePreviewBinding
import com.nrk.newsbreeze.utils.DateUtil
import java.util.*


class ArticlesAdapter(private val listener: OnItemClickListener): ListAdapter<Article, ArticlesAdapter.ArticleViewHolder>(DiffCallback()), Filterable {

    private var fullList: List<Article>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    fun setList(list: MutableList<Article>?) {
        fullList = list
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
                if(article.isSaved == true){
                    ivSave.setImageResource(R.drawable.ic_saved)
                }
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

    override fun getFilter(): Filter? {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Article> = ArrayList()
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
            submitList(results.values as MutableList<Article>)
//            notifyDataSetChanged()
        }
    }

}