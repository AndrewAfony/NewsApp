package com.myapp.newsapp.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapp.newsapp.R
import com.myapp.newsapp.adapters.NewsAdapter
import com.myapp.newsapp.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        etSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                viewModel.searchNews(it.toString())
            }
        }

        searchNews()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )

        }

    }

    private fun searchNews() {

        lifecycleScope.launchWhenCreated {

            viewModel.searchNews.collect { result ->
                when (result.isLoading) {
                    true -> {
                        showProgressBar()
                    }
                    false -> {
                        hideProgressBar()
                    }
                }

                if(result.error != null) {
                    Toast.makeText(activity, result.error, Toast.LENGTH_LONG).show()
                }

                newsAdapter.differ.submitList(result.news?.articles)
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
}