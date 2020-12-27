package com.clay.dagger2testingdemo.repositories

import androidx.lifecycle.LiveData
import com.clay.dagger2testingdemo.data.local.WordDao
import com.clay.dagger2testingdemo.data.local.entities.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultWordRepository @Inject constructor(
    private val wordDao: WordDao
) : WordRepository {

    override suspend fun insertWord(word: Word) = withContext(Dispatchers.IO) {
        wordDao.insertWord(word)
    }

    override suspend fun deleteWord(word: Word) = withContext(Dispatchers.IO) {
        wordDao.deleteWord(word)
    }

    override fun observeAllWords(): LiveData<List<Word>> = wordDao.observerAllWords()
}