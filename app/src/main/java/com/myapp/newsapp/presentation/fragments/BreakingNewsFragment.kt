package com.myapp.newsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.newsapp.R
import com.myapp.newsapp.adapters.NewsAdapter
import com.myapp.newsapp.presentation.NewsState
import com.myapp.newsapp.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.flow.collect

private const val QUERY_PAGE_SIZE = 20

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

                val totalPages = (result.news?.totalResults?.div(QUERY_PAGE_SIZE))?.plus(2)
                isLastPage = viewModel.breakingNewsPage == totalPages

                if (isLastPage) {
                    rvBreakingNews.setPadding(0, 0, 0, 0)
                }

                newsAdapter.differ.submitList(result.news?.articles?.toList())
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
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

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemsCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemsCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalIsMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem &&
                    isNotAtBeginning && isTotalIsMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("ru")
                isScrolling = false
            }
        }
    }
}