package com.example.note.data.remote
import com.example.note.data.local.entities.Note
import com.example.note.data.remote.requests.AccountRequest
import com.example.note.data.remote.requests.AddOwnerRequest
import com.example.note.data.remote.requests.DeleteNoteRequest
import com.example.note.data.remote.responses.SimpleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteApi {
    @POST("/register")
    suspend fun register(
        @Body registerRequest: AccountRequest
    ): Response<SimpleResponse>

    @POST("/login")
    suspend fun login(
    @Body loginRequest:AccountRequest
    ):Response<SimpleResponse>

    @POST("/addNote")
    suspend fun addNote(
        @Body note: Note
    ):Response<ResponseBody>

    @GET("/getNotes")
    suspend fun getNotes(): Response<List<Note>>

    @POST("/addOwner")
    suspend fun addOwnerToNote(
    @Body addOwnerRequest: AddOwnerRequest
    ):Response<SimpleResponse>

    @POST("/deleteNote")
    suspend fun deleteNote(
        @Body deleteNoteRequest: DeleteNoteRequest
    ):Response<SimpleResponse>

}