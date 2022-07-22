package com.example.androidcleanarchitecture.ui

import androidx.fragment.app.Fragment

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    fun getMain(): MainActivity? {
        return requireActivity() as MainActivity
    }
}