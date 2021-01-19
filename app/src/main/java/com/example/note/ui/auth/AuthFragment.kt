package com.example.note.ui.auth

import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import com.example.note.R
import com.example.note.data.remote.BasicAuthInterceptor
import com.example.note.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment :BaseFragment(){
    private val viewModel:AuthViewModel by viewModels()
    @Inject
    lateinit var sharedPref: SharedPreferences
    @Inject
    lateinit var basicAuthInterceptor : BasicAuthInterceptor
}