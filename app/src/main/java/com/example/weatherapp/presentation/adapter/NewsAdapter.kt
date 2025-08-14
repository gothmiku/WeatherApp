package com.example.weatherapp.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Article


class NewsAdapter(
    private var newsList: List<Article>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    fun updateData(newResponseList: List<Article>) {
        newsList = newResponseList
        notifyDataSetChanged() // This tells the RecyclerView to refresh all items
    }

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img : ImageView = view.findViewById(R.id.newsImage)
        val title : TextView = view.findViewById(R.id.newsTitle)
        val desc : TextView = view.findViewById(R.id.newsDesc)
        val outlet : TextView = view.findViewById(R.id.newsOutlet)
        val frameBound : View = view.findViewById(R.id.frameBound)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val apiNews = newsList[position]

        holder.outlet.text = apiNews.source.name
        holder.desc.text = apiNews.content
        holder.title.text = apiNews.title
        holder.desc.isSelected = true
        holder.title.setOnHoverListener { v,event ->
            when(event.action){
                MotionEvent.ACTION_HOVER_ENTER -> {
                    holder.title.isSelected=true
                }
                MotionEvent.ACTION_HOVER_EXIT -> {
                    holder.title.isSelected=false
                }
            }
            true
        }
        val url = apiNews.url

        holder.frameBound.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            false
        }


        holder.frameBound.setOnClickListener {

            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(holder.itemView.context, Uri.parse(url))
        }

        Glide.with(holder.itemView.context)
            .load(newsList[position].urlToImage)
            .placeholder(R.drawable.ic_launcher_background) // optional
            .error(R.drawable.gradients)      // optional
            .into(holder.img)
    }

    override fun getItemCount() = newsList.size
}