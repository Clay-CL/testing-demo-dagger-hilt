package com.clay.dagger2testingdemo.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.clay.dagger2testingdemo.data.local.entities.Word

class FakeWordRepository : WordRepository {

    private val words = mutableListOf<Word>()

    private val observableWords = MutableLiveData<List<Word>>(words)

    private fun refreshObservableWords(words: List<Word>) {
        observableWords.value = words
    }

    override suspend fun insertWord(word: Word) {
        words.add(word)
        refreshObservableWords(words)
    }

    override suspend fun deleteWord(word: Word) {
        words.remove(word)
        refreshObservableWords(words)
    }

    override fun observeAllWords(): LiveData<List<Word>> {
        return observableWords
    }
}