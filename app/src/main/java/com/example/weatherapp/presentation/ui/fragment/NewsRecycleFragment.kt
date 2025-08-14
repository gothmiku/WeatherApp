package com.example.weatherapp.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.presentation.adapter.WeatherAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.presentation.adapter.NewsAdapter
import com.example.weatherapp.presentation.anims.ScrollBounceEffect
import com.example.weatherapp.presentation.viewmodel.NewsViewModel


@AndroidEntryPoint
class NewsRecycleFragment : Fragment(R.layout.news_layout) {

    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        setupRecyclerView(view)
        observeNewsData()
    }

    private fun setupRecyclerView(view: View) {
        adapter = NewsAdapter(emptyList())
        val recyclerView = view.findViewById<RecyclerView>(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.edgeEffectFactory = ScrollBounceEffect()
    }

    private fun observeNewsData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val newsResponse = viewModel.getNews().articles
                Log.d("News","Successfully fetched news")
                Log.d("News","Filtering out null news...")
                val filteredList = newsResponse.filter { it.title != null && it.description != null && it.urlToImage != null && it.url != null }
                adapter.updateData(newsResponse)
                Log.d("News","successfully updated data")
            } catch (e: Exception) {
                Log.e("News", "Error fetching news: ${e.message}")
            }
        }
    }
}