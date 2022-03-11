package com.dehdarian.task.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dehdarian.task.databinding.ItemArticlesBinding
import com.dehdarian.task.model.Article
import com.dehdarian.task.ui.WebViewActivity

/**
 * News adapter to map data on recyclerview
 */
class ArticleAdapter :
    RecyclerView.Adapter<ArticleAdapter.CustomViewHolder>() {

    private val dataList : ArrayList<Article> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(articles : ArrayList<Article>){
        dataList.clear()
        dataList.addAll(articles)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {

        val itemBinding =
            ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBinding)

    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position])

    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(private val itemNewsBinding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(itemNewsBinding.root) {
        /**
         * Bind news data on each item of recyclerview (image,title,description and web view url)
         */
        fun bind(article: Article)
        {
            Glide.with(itemNewsBinding.root.context).load(article.image).into(itemNewsBinding.ivImage)
            itemNewsBinding.tvTitle.text = article.title
            itemNewsBinding.tvDes.text = article.description
            itemNewsBinding.rlRootItem.setOnClickListener()
            {
                val intent=Intent(itemNewsBinding.rlRootItem.context,WebViewActivity::class.java)
                intent.putExtra("url",article.url)
                itemNewsBinding.rlRootItem.context.startActivity(intent)
            }
        }
    }
}