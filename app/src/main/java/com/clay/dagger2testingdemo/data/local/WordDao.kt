package com.clay.dagger2testingdemo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.clay.dagger2testingdemo.data.local.entities.Word

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)

    @Query("SELECT * FROM words")
    fun observerAllWords(): LiveData<List<Word>>

}