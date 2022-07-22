package com.example.androidcleanarchitecture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.androidcleanarchitecture.R
import com.example.androidcleanarchitecture.databinding.FragmentSchoolInfoBinding
import com.example.androidcleanarchitecture.model.SATScores


import com.example.androidcleanarchitecture.network.ResponseModel
import com.example.androidcleanarchitecture.viewmodel.SchoolsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SchooInfoFragment : BaseFragment(R.layout.fragment_school_info) {

    private lateinit var binding: FragmentSchoolInfoBinding
    private val viewModel: SchoolsViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_school_info, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewLifecycleOwner.lifecycleScope.launch {
            // viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.schoolInfoData.collect { it ->
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
                        try {
                            if (it.data?.body()?.get(0)?.dbn.isNullOrEmpty()) {
                                Toast.makeText(
                                    requireContext(),
                                    "No data found.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                upDateWithSchoolInfor(it.data?.body()?.get(0))

                            }
                        } catch (e: Exception) {
                            val bottomNavigationView =
                                activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
                            bottomNavigationView.selectedItemId = R.id.newsFragment
                        }
                    }
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)

    }


    /**
     * Updates the SAT Scores once available from DB
     * @param score
     */
    private fun upDateWithSchoolInfor(schoolInfo: SATScores?) {
        if (schoolInfo != null) {
            val schoolName = schoolInfo.getSchool_name()
            val testTakers = schoolInfo.getNum_of_sat_test_takers()
            val readingAvg = schoolInfo.getSat_critical_reading_avg_score()
            val dbn = schoolInfo.dbn.toString()
            val satWriting =
                schoolInfo.getSat_writing_avg_score()
            val satMath =
                schoolInfo.getSat_math_avg_score()
            binding.mathAvg.text = satMath
            binding.writingAvg.text = satWriting
            binding.readingAvg.text = readingAvg
            binding.schoolName.text = schoolName
            binding.testTakersNum.text = testTakers
            binding.root.refreshDrawableState()

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



