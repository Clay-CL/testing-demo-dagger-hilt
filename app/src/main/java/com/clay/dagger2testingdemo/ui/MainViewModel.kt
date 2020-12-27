package com.clay.dagger2testingdemo.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clay.dagger2testingdemo.data.local.entities.Word
import com.clay.dagger2testingdemo.other.Event
import com.clay.dagger2testingdemo.repositories.WordRepository
import com.clay.dagger2testingdemo.other.Resource
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val wordRepository: WordRepository
): ViewModel() {

    val words = wordRepository.observeAllWords()

    private val _insertWord = MutableLiveData<Event<Resource<Word>>>()
    val insertWord: LiveData<Event<Resource<Word>>> = _insertWord

    fun insertWordIntoDb(word: Word) = viewModelScope.launch {
        wordRepository.insertWord(word)
        _insertWord.value = Event(Resource.Success(word))
    }

    fun insertWord(word: String, meaning: String) {
        _insertWord.value = Event(Resource.Loading())
        if(word.isEmpty() || meaning .isEmpty()) {
            _insertWord.value = Event(Resource.Error("Please fill out all fields"))
            return
        }
        insertWordIntoDb(Word(word, meaning))
    }

    fun deleteWord(word: Word) = viewModelScope.launch {
        wordRepository.deleteWord(word)
    }


}