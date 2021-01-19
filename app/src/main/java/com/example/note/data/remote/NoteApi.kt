package com.example.note.data.remote

import com.example.note.data.remote.requests.AccountRequest
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteApi {
    @POST("/register")
    suspend fun register(
        @Body registerRequest: AccountRequest
    ):Response
}