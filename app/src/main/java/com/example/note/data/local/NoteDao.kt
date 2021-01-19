package com.example.note.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.note.data.local.entities.LocallyDeletedNoteId
import com.example.note.data.local.entities.Note

@Dao
interface NoteDao {


    @Query("SELECT * FROM notes")
    fun getAllNotes():List<Note>







    @Query("SELECT * FROM notes WHERE isSynced = 0")
    suspend fun getAllUnSyncedNotes():List<Note>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)




    @Query("DELETE FROM notes WHERE isSynced =1")
    suspend fun deleteAllSyncedNotes()

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    @Query("DELETE FROM notes WHERE id = :noteID")
    suspend fun deleteNoteById(noteID:String)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocallyDeletedNote(locallyDeletedNoteId: LocallyDeletedNoteId)

    @Query("SELECT * FROM locally_Deleted_NoteIds")
    suspend fun getAllLocallyDeletedNoteByIds():List<LocallyDeletedNoteId>
    @Query("DELETE FROM locally_Deleted_NoteIds WHERE deletedNoteId= :deleteNoteById")
    suspend fun deleteLocallyDeletedNoteById(deleteNoteById:String)



    @Query("SELECT* FROM notes where id =:noteID")
    fun observeNoteById(noteID: String):LiveData<Note>


    @Query("SELECT * FROM notes WHERE id =:noteID")
   suspend fun getNoteById(noteID: String):Note?












}
























