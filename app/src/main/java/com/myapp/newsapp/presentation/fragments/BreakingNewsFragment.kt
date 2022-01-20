package com.myapp.newsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.myapp.newsapp.R
import com.myapp.newsapp.adapters.NewsAdapter
import com.myapp.newsapp.presentation.NewsState
import com.myapp.newsapp.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        getBreakingNews()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )

        }

        refreshLayout.setOnRefreshListener {

            getBreakingNews()

            refreshLayout.isRefreshing = false
        }
    }

    private fun getBreakingNews() {

        lifecycleScope.launchWhenCreated {
            viewModel.breakingNews.collect { result ->
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
        rvBreakingNews.apply {
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