package com.example.note.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.note.R
import com.example.note.data.remote.BasicAuthInterceptor
import com.example.note.other.Constants.KEY_LOGGED_IN_EMAIL
import com.example.note.other.Constants.KEY_PASSWORD
import com.example.note.other.Constants.NO_EMAIL
import com.example.note.other.Constants.NO_PASSWORD
import com.example.note.other.Status
import com.example.note.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment :BaseFragment(R.layout.fragment_auth){
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var curEmail: String? = null
    private var curPassword:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isLoggedIn()){
            authenticateApi(curEmail?:"", curPassword?:"")
            redirectLogin()
        }
        subscribeToObservers()
        btnRegister.setOnClickListener {
            val email = etRegisterEmail.text.toString()
            val password = etRegisterPassword.text.toString()
            val confirmPassword =etRegisterPasswordConfirm.text.toString()
            viewModel.register(email, password, confirmPassword)
        }
        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()
            viewModel.login(email, password)
        }
    }
    private fun isLoggedIn():Boolean{
        curEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        curPassword = sharedPref.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD
        return curEmail != NO_EMAIL && curPassword != NO_PASSWORD
    }
    private fun authenticateApi(email: String, password: String){
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }



    private fun redirectLogin(){
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.authFragment, true)
            .build()
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToNotesFragment(),
            navOptions
        )
    }



    private fun subscribeToObservers(){
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer{ result->
        result.let {
            when(result.status){
                Status.SUCCESS -> {
                    loginProgressBar.visibility = View.GONE
                    showSnackbar(result.data ?:"Successfully logged in")
                    sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, curEmail).apply()
                    sharedPref.edit().putString(KEY_PASSWORD, curPassword).apply()
                    authenticateApi(curEmail?:"",curPassword?:"")
                    Timber.d("CALLED")
                    redirectLogin()
                }
                Status.ERROR ->{
                    loginProgressBar.visibility = View.GONE
                    showSnackbar(result.message ?:"A unknown error occurred")
                }
                Status.LOADING ->{
                    loginProgressBar.visibility = View.VISIBLE
                }
            }
        }
        } )
        viewModel.registerStatus.observe(viewLifecycleOwner, Observer { result ->
            when(result.status){
                Status.SUCCESS -> {
                    registerProgressBar.visibility = View.GONE
                    showSnackbar(result.data ?:"Successfully registered an account")
                }
                Status.ERROR ->{
                    registerProgressBar.visibility = View.GONE
                    showSnackbar(result.message?:"An unknown error occurred")
                }
                Status.LOADING ->{
                    registerProgressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}


















