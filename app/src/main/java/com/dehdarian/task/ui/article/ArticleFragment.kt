package com.dehdarian.task.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dehdarian.task.databinding.FragmentArticlesBinding
import com.dehdarian.task.model.Article
import com.dehdarian.task.ui.adapter.ArticleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: FragmentArticlesBinding? = null
    private val articleAdapter by lazy {
        ArticleAdapter()
    }
    private val articlesViewModel: ArticleViewModel by viewModels()

    /**
     * This property is only valid between onCreateView and onDestroyView.
     */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticlesBinding.inflate(inflater, container, false)
        setupToolbar()
        /**
         * Observing data from ViewModel
         */
        articlesViewModel.articles.observe(viewLifecycleOwner)
        {
            it.data?.articles?.let { articles ->
                articleAdapter.submitList(articles as ArrayList<Article>)
            }
        }
        setupAdapter()
        return binding.root
    }

    /**
     * Setup custom toolbar for Article fragment
     */
    private fun setupToolbar() {
        binding.toolbarArticle.ivBack.visibility=View.GONE
        binding.toolbarArticle.llSearchBar.visibility=View.GONE
        binding.toolbarArticle.llClear.visibility=View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Setup adapter to fill articles in adapter of recyclerview article fragment
     */
    private fun setupAdapter() {
        binding.recyclerviewArticles.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}