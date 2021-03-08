package com.example.note.repository

import android.app.Application
import android.content.Context
import com.example.note.data.local.NoteDao
import com.example.note.data.local.entities.LocallyDeletedNoteId
import com.example.note.data.local.entities.Note
import com.example.note.data.remote.NoteApi
import com.example.note.data.remote.requests.AccountRequest
import com.example.note.data.remote.requests.AddOwnerRequest
import com.example.note.data.remote.requests.DeleteNoteRequest
import com.example.note.other.Resource
import com.example.note.other.checkForInternetConnection
import com.example.note.other.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import kotlin.Exception

class NoteRepository @Inject constructor(
    private val context: Application,
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
) {

    fun getAllNotes():kotlinx.coroutines.flow.Flow<Resource<List<Note>>> {
        return networkBoundResource(
            query = {
            noteDao.getAllNotes()
            },
            fetch = {
            syncNotes()
                curNotesResponse
            },
            saveFetchResult = { response ->
                response?.body()?.let {
                    insertNotes(it.onEach { note ->
                        note.isSynced = true})
                }
            },
         shouldFetch = {
            checkForInternetConnection(context)
         }

        )
    }
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





    fun observerNoteById(noteID: String) = noteDao.observeNoteById(noteID)

    suspend fun getNoteById(noteId: String) = noteDao.getNoteById(noteId)




    suspend fun insertNote(note:Note) {
            val response = try {
            noteApi.addNote(note)
            } catch (e: Exception){null}
        if (response != null && response.isSuccessful){
            noteDao.insertNote(note.apply { isSynced = true })
        }   else{
            noteDao.insertNote(note)
        }
        }


    suspend fun insertNotes(notes:List<Note>){
        notes.forEach{insertNote(it)}
    }
    suspend fun addOwnerToNote(owner: String, noteID: String) = withContext(Dispatchers.IO) {
        try {
            val response = noteApi.addOwnerToNote(AddOwnerRequest(owner, noteID))
            if(response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch(e: Exception) {
            Resource.error("Couldn't connect to the servers. Check your internet connection", null)
        }
    }

    suspend fun deleteNote(noteId:String){
        val response = try {
            noteApi.deleteNote(DeleteNoteRequest(noteId))
        }catch (e:Exception){
            null
        }
        noteDao.deleteNoteById(noteId)
        if (response == null || !response.isSuccessful) {
            noteDao.insertLocallyDeletedNote(LocallyDeletedNoteId(noteId))
        }else{
            deleteLocallyDeleted(noteId)
        }
    }
    suspend fun deleteLocallyDeleted(deletedNoteId: String){
        noteDao.deleteLocallyDeletedNoteById(deletedNoteId)
    }



    private var curNotesResponse: Response<List<Note>>? = null
    suspend fun syncNotes(){
        val locallyDeletedNoteIds = noteDao.getAllLocallyDeletedNoteByIds()
        locallyDeletedNoteIds.forEach { id ->
            deleteNote(id.deletedNoteId)
        }
        val unSyncedNotes = noteDao.getAllUnSyncedNotes()
        unSyncedNotes.forEach { note -> insertNote(note)}

        curNotesResponse = noteApi.getNotes()
        curNotesResponse?.body()?.let { notes ->
            noteDao.deleteAllNotes()
            insertNotes(notes.onEach { note ->
                    note.isSynced = true
            })

        }
          }

}














































