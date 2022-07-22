package com.example.androidcleanarchitecture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidcleanarchitecture.R
import com.example.androidcleanarchitecture.adapter.SchoolsListAdapter
import com.example.androidcleanarchitecture.databinding.FragmentNewsBinding
import com.example.androidcleanarchitecture.model.School
import com.example.androidcleanarchitecture.network.ResponseModel
import com.example.androidcleanarchitecture.viewmodel.SchoolsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SchoolsFragment : Fragment() {
    private lateinit var launch: Job
    private lateinit var binding: FragmentNewsBinding
    private val viewModel: SchoolsViewModel by sharedViewModel()
    private lateinit var newsListAdapter: SchoolsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.example.androidcleanarchitecture.R.layout.fragment_news,
            null,
            false
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(vnewsListAdapteriew: View, savedInstanceState: Bundle?) {
        newsListAdapter =
            SchoolsListAdapter(ArrayList<School>()) {
            }
        binding.rvSchools.adapter = newsListAdapter

        binding.rvSchools.layoutManager = LinearLayoutManager(requireContext())
        launch = viewLifecycleOwner.lifecycleScope.launch {
          //  viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.schoolData.collectLatest { it ->
                    when (it) {
                        is ResponseModel.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        is ResponseModel.Idle -> {
                            Toast.makeText(requireContext(), "Idle State", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is ResponseModel.Loading -> {
                            showDialog()
                        }
                        is ResponseModel.Success -> {
                            dismissDialog()
                            if (it.data?.body()?.get(0)?.dbn.isNullOrEmpty()) {
                                Toast.makeText(
                                    requireContext(),
                                    "No data found.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                it.data?.body()?.let { schools ->
                                    newsListAdapter =
                                        SchoolsListAdapter(schools as ArrayList<School>) {
                                            redirectToDetails(it)
                                        }
                                    binding.rvSchools.adapter = newsListAdapter
                                }
                            }
                        }
                    }
             //   }
            }
        }

        viewModel.viewModelScope.launch {
            viewModel.category.collect {
                viewModel.getNews(it)
            }
        }
    }

    private fun redirectToDetails(school: School) {
        launch.cancel()
        viewModel.viewModelScope.launch {
            viewModel.getNews2(school.dbn!!)
            val bottomNavigationView =
                activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
            try {
                bottomNavigationView.selectedItemId = R.id.detailNewsFragment
                viewModel.getNews2(school.dbn!!)
                val navController = findNavController();
                val bundle = Bundle()
                bundle.putString("dbn", school.dbn)
                navController.navigate(
                    com.example.androidcleanarchitecture.R.id.action_newsFragment_to_detailNewsFragment,
                    bundle
                )
            } catch (e: Exception) {
                val bottomNavigationView =
                    activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
                bottomNavigationView.selectedItemId = R.id.detailNewsFragment
            }
        }
    }

    private fun showDialog() {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun dismissDialog() {
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.Main) {

            }
        }
    }

}