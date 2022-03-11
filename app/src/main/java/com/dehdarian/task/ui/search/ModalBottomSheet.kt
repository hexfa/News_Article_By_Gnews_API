package com.dehdarian.task.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dehdarian.task.R
import com.dehdarian.task.api.Constants
import com.dehdarian.task.model.Article
import com.dehdarian.task.ui.adapter.ArticleAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ModalBottomSheet(submitQuery: String, from: String, to: String, searchIn: String,recyclerView: RecyclerView) : BottomSheetDialogFragment() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val bottomSheetSubmitQuery:String=submitQuery
    private val bottomSheetFrom:String=from
    private val bottomSheetTo:String=to
    private val bottomSheetSearchIn:String=searchIn
    private val bottomSheetRecyclerView=recyclerView
    private val articleAdapter by lazy {
        ArticleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_layout, container, false)
        val rbUploadData = view.findViewById<RadioButton>(R.id.rbUploadData)
        val rbRelevance = view.findViewById<RadioButton>(R.id.rbRelevance)
        rbRelevance.setOnClickListener()
        {
            rbUploadData.isChecked=false
            CoroutineScope(Dispatchers.Main).launch {
                searchViewModel.searchByFilter(bottomSheetSubmitQuery, bottomSheetFrom, bottomSheetTo, Constants.RELEVANCE, bottomSheetSearchIn)
//                dismiss()
            }
        }
        rbUploadData.setOnClickListener()
        {
            rbRelevance.isChecked=false
            CoroutineScope(Dispatchers.Main).launch {
                searchViewModel.searchByFilter(bottomSheetSubmitQuery, bottomSheetFrom, bottomSheetTo, Constants.PUBLISH_AT, bottomSheetSearchIn)

//                dismiss()
            }
        }
        searchViewModel.articles.observe(viewLifecycleOwner)
        {
            it?.data?.articles?.let { articles ->
                articleAdapter.submitList(articles as ArrayList<Article>)
                setupAdapter()

            }
        }
        return view
    }
    //setup adapter to fill articles in adapter of recyclerview in search fragment
    private fun setupAdapter() {
        bottomSheetRecyclerView.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }



}
