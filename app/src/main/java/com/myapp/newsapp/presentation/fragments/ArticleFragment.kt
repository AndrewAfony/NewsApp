package com.myapp.newsapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.myapp.newsapp.R
import com.myapp.newsapp.presentation.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}