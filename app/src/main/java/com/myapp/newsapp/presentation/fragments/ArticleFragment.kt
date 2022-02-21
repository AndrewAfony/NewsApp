package com.myapp.newsapp.presentation.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.myapp.newsapp.R
import com.myapp.newsapp.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_article.*

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val viewModel: NewsViewModel by viewModels()
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article

        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }

        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.favorite -> {
                    viewModel.saveArticle(article)
                    updateMenu()
                    Snackbar.make(view, "Article Saved", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.more -> {

                    true
                }
                else -> false
            }
        }

    }

    private fun updateMenu() {

        val item = toolbar.menu.findItem(R.id.favorite)
        item.setIcon(R.drawable.ic_favorite)
        item.isChecked = !item.isChecked
    }
}