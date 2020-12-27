package com.clay.dagger2testingdemo.repositories

import androidx.lifecycle.LiveData
import com.clay.dagger2testingdemo.data.local.entities.Word

interface WordRepository {

    suspend fun insertWord(word: Word)

    suspend fun deleteWord(word: Word)

    fun observeAllWords(): LiveData<List<Word>>

}