package com.example.androidcleanarchitecture.utils

import android.app.AlertDialog
import com.example.androidcleanarchitecture.R
import com.example.androidcleanarchitecture.ui.BaseFragment
import com.example.androidcleanarchitecture.viewmodel.SchoolsViewModel
import com.hadiyarajesh.flower.Resource
import kotlinx.coroutines.flow.Flow


fun BaseFragment.progressOn() {
    getMain()?.showProgress()
}

fun BaseFragment.progressOff() {
    getMain()?.hideProgress()
}

fun BaseFragment.showError(errorMessage: String) {
    context?.apply {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.attention_text))
            .setMessage(errorMessage)
            .setPositiveButton("OK", null)
            .show()
    }
}


suspend fun <T> Flow<Resource<T>>.foldApiStates(
    onSuccess: suspend (T) -> Unit,
    onLoading: suspend (SchoolsViewModel.State.LoadingState) -> Unit,
    onError: suspend (SchoolsViewModel.State.ErrorState) -> Unit
) {
    this.collect { resource: Resource<T> ->
        when (resource.status) {
            Resource.Status.LOADING -> {
                onLoading(SchoolsViewModel.State.LoadingState())
            }
            Resource.Status.SUCCESS -> {
                resource.data?.let { onSuccess(it) }
                SchoolsViewModel.State.SuccessState(resource)
            }
            is Resource.Status.ERROR -> {
                val error = resource.status as Resource.Status.ERROR
                onError(SchoolsViewModel.State.ErrorState(error.message, error.statusCode))
            }
        }
    }
}
