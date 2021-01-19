package com.example.note.repository

import android.content.Context
import com.example.note.data.local.NoteDao
import com.example.note.data.remote.NoteApi
import com.example.note.data.remote.requests.AccountRequest
import com.example.note.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val context: Context,
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
) {
    suspend fun registerUser(email:String,password:String) = withContext(Dispatchers.IO){
        try {
            val response = noteApi.register(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful)
                Resource.success(response.body()?.message)
            else{
                Resource.error(response.body()?.message ?:response.message(),null)
            }
        }catch (e:Exception){
        Resource.error("Couldn't connect to the servers. Check your internet connection",null)
        }
    }

    suspend fun loginUser(email:String,password: String) = withContext(Dispatchers.IO){
            try {
                val response = noteApi.login(AccountRequest(email, password))
                if(response.isSuccessful && response.body()!!.successful)
                    Resource.success(response.body()?.message)
                else{
                    Resource.error(response?.message(),null)
                }
            }catch (e:Exception){
            Resource.error("Couldn't connect to the serves. Check your internet connection", null)
            }
    }
}