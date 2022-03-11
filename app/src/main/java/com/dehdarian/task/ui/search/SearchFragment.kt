package com.dehdarian.task.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dehdarian.task.R
import com.dehdarian.task.api.Constants
import com.dehdarian.task.databinding.FragmentSearchBinding
import com.dehdarian.task.model.Article
import com.dehdarian.task.ui.adapter.ArticleAdapter
import com.dehdarian.task.ui.adapter.HistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var histories = arrayListOf<String>()
    private var _binding: FragmentSearchBinding? = null
    private var from = ""
    private var to = ""
    private var type = ""
    private var isContent=false
    private var isDes=false
    private var isTitle=false
    private var searchIn=""
    private var isShowHistoryKeyword=true

    /**
     * This property is only valid between onCreateView and onDestroyView
     */
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private val articleAdapter by lazy {
        ArticleAdapter()
    }
    private val historyAdapter by lazy {
        HistoryAdapter()
    }
    var submitQuery = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.toolbarSearch.llClear.visibility = View.GONE
        binding.filter1.rlRootFilter1.visibility = View.GONE
        binding.filter2.rlRootFilter2.visibility = View.GONE

        /**
         * Open Bottom sheet on search view
         */
        binding.toolbarSearch.ivSortData.setOnClickListener()
        {
            val modalBottomSheet = ModalBottomSheet(submitQuery,from,to,searchIn,binding.recyclerviewSearch)
            modalBottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
        }

        /**
         * Search view onQueryTextChange ,onQueryTextSubmit listener
         */
        binding.toolbarSearch.searchview.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                submitQuery = newText
                binding.recyclerviewHistory.visibility=View.VISIBLE
                binding.recyclerviewSearch.visibility=View.GONE
                if(isShowHistoryKeyword) {
                    isShowHistoryKeyword = false
                    getAllHistories()
                }
                return false
            }

            /**
             * Submitting the search query
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                submitQuery = query.trim()
                addHistory(submitQuery)
                isShowHistoryKeyword=true
                binding.recyclerviewHistory.visibility=View.GONE
                binding.recyclerviewSearch.visibility=View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    searchViewModel.search(query)
                }
                return false
            }
        })
        binding.toolbarSearch.searchview.setOnCloseListener {
            binding.recyclerviewHistory.visibility=View.GONE
            binding.recyclerviewSearch.visibility=View.VISIBLE
            return@setOnCloseListener true
        }

        /**
         * Observing news and call setupAdapter to map data on recyclerview
         */
        searchViewModel.articles.observe(viewLifecycleOwner)
        {
            it?.data?.articles?.let { articles ->
                articleAdapter.submitList(articles as ArrayList<Article>)
                setupAdapter()

            }
        }

        /**
         * Updating the news's numbers in title of search fragment
         */
        searchViewModel.totalArticles.observe(viewLifecycleOwner) {
            binding.tvSearchTitle.text = "$it "+getString(R.string.news)
        }

        binding.toolbarSearch.ivFilter.setOnClickListener()
        {
            binding.filter1.rlRootFilter1.visibility = View.VISIBLE
            binding.toolbarSearch.llClear.visibility = View.VISIBLE
        }

        /**
         * Apply filter by date type
         **/
        binding.filter1.btnApplyFilter.setOnClickListener()
        {
            from = binding.filter1.etStart.text.toString()
            from+="T00:00:01Z"
            to = binding.filter1.etEnd.text.toString()
            to+="T23:59:59Z"
            type = binding.filter1.tvSearchInValue.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                searchViewModel.searchByFilter(submitQuery, from, to,Constants.PUBLISH_AT, type)
                binding.filter1.rlRootFilter1.visibility = View.GONE
            }
        }
        binding.filter1.rlSearchInRow.setOnClickListener()
        {
            binding.filter1.rlRootFilter1.visibility = View.GONE
            binding.filter2.rlRootFilter2.visibility = View.VISIBLE
            from = binding.filter1.etStart.text.toString()
            from+="T00:00:01Z"
            to = binding.filter1.etEnd.text.toString()
            to+="T23:59:59Z"
            type = binding.filter1.tvSearchInValue.text.toString()
        }

        /**
         * Apply filter by search in type
         **/
        binding.filter2.btnApplyFilter2.setOnClickListener()
        {
            isContent = binding.filter2.swContent.isChecked
            isTitle = binding.filter2.swTitle.isChecked
            isDes = binding.filter2.swDes.isChecked
            val types = ArrayList<String>()
            if (isContent){
                types.add("content")
            }
            if (isTitle)
            {
                types.add("title")
            }
            if (isDes)
            {
                types.add("description")
            }
            searchIn = types.joinToString(",")
            if(types.size==3)
            {
                binding.filter1.tvSearchInValue.text ="all"
            }else
            {
                binding.filter1.tvSearchInValue.text =searchIn
            }
            CoroutineScope(Dispatchers.Main).launch {
                searchViewModel.searchByFilter(submitQuery, from, to,Constants.PUBLISH_AT, searchIn)
                binding.filter2.rlRootFilter2.visibility = View.GONE
                binding.toolbarSearch.llClear.visibility=View.GONE
            }
        }
        binding.filter1.topbarFilter1.llClear.setOnClickListener()
        {
            binding.filter1.etStart.setText("")
            binding.filter1.etEnd.setText("")
        }

        /**
         * Clear data of filter boxes
         */
        binding.filter2.topbarFilter2.llClear.setOnClickListener()
        {
            binding.filter2.swContent.isChecked = false
            binding.filter2.swDes.isChecked = false
            binding.filter2.swTitle.isChecked = false
        }

        binding.filter1.topbarFilter1.ivBack.setOnClickListener()
        {
            binding.filter1.rlRootFilter1.visibility=View.GONE
        }

        /**
         * Click listener of Back button
         */
        binding.filter2.topbarFilter2.ivBack.setOnClickListener()
        {
            binding.filter2.rlRootFilter2.visibility=View.GONE
        }

        return binding.root
    }

    /**
     * Setup adapter to fill articles in adapter of recyclerview in search fragment
     */
    private fun setupAdapter() {
        binding.recyclerviewSearch.apply {
            adapter = articleAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    /**
     * Setup adapter to fill histories in adapter of recyclerview in search fragment
     */
    private fun setupHistoryAdapter() {

        historyAdapter.submitList(histories)
        binding.recyclerviewHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Add keyword to shared preferences
     */
    private fun addHistory(history:String){
        val preferences = binding.recyclerviewHistory.context.getSharedPreferences("history", Context.MODE_PRIVATE)
        val stringList = preferences.getString("history", "")
        if(stringList.isNullOrEmpty())
        {
            val jsonArray =JSONArray()
            jsonArray.put(history)
            with (preferences.edit()) {
                putString("history",jsonArray.toString())
                apply()
            }
        }else
        {
            try {
                var isThere=false
                val jsonArray = JSONArray(stringList)
                for (i in 0 until jsonArray.length()) {
                    if(jsonArray.get(i).equals(history))
                    {
                        isThere=true
                        break
                    }
                }
                if (!isThere)
                {
                    jsonArray.put(history)
                }

                with (preferences.edit()) {
                    putString("history",jsonArray.toString())
                    apply()
                }
            }catch (e:JSONException)
            {
                e.printStackTrace()
            }
        }
    }

    /**
     * Get all keyword(s) from shared preferences to show in recyclerview with setupAdapter
     */
    private fun getAllHistories() {
            val sharedPref = activity?.getSharedPreferences("history",Context.MODE_PRIVATE) ?: return
            val stringList = sharedPref.getString("history", "")

            if(!stringList.isNullOrEmpty())
            {
                histories.clear()
                val jsonArray =JSONArray(stringList)
                for (i in 0 until jsonArray.length()) {
                    histories.add(jsonArray.getString(i))
                }
                setupHistoryAdapter()
            }
    }
}