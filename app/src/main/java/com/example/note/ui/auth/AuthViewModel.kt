package com.example.note.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.other.Resource
import com.example.note.repository.NoteRepository
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
        private val repository: NoteRepository
) : ViewModel() {
        private val _registerStatus = MutableLiveData<Resource<String>>()
        val registerStatus: LiveData<Resource<String>> = _registerStatus

        private val _loginStatus = MutableLiveData<Resource<String>>()
        val loginStatus:LiveData<Resource<String>> = _loginStatus

        fun register(email:String,password:String,repeatedPassword:String){
                _registerStatus.postValue(Resource.loading(null))
                if (email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()){
                        _registerStatus.postValue(Resource.error("Please fill all the fields", null))
                        return
                }
                if (password != repeatedPassword){
                        _registerStatus.postValue(Resource.error("Passwords do ot match",null))
                        return
                }
                viewModelScope.launch {
                        val result = repository.registerUser(email, password)
                        _registerStatus.postValue((result))
                }

        }

        fun login(email: String,password: String){
                _loginStatus.postValue(Resource.loading(null))
                if (email.isEmpty() || password.isEmpty()){
                        _loginStatus.postValue(Resource.error("Please Fill All Fields", null))
                        return
                }
                viewModelScope.launch {
                        val result = repository.loginUser(email, password)
                        _loginStatus.postValue(result)
                }
        }


}































