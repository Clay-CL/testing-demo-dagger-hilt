package com.clay.dagger2testingdemo.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.clay.dagger2testingdemo.data.local.entities.Word
import com.clay.dagger2testingdemo.di.InMemoryDb
import com.clay.dagger2testingdemo.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class WordDaoTest {

    @Inject
    @InMemoryDb
    lateinit var database: WordDatabase

    private lateinit var dao: WordDao

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.wordDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertWord() = runBlockingTest {
        val word = Word("test", "test", id = 1L)
        dao.insertWord(word)

        val allWords = dao.observerAllWords().getOrAwaitValue()
        assertThat(allWords).contains(word)
    }

    @Test
    fun deleteWord() = runBlockingTest {
        val word = Word("test", "test", id = 1L)
        dao.insertWord(word)

        dao.deleteWord(word)

        val allWords = dao.observerAllWords().getOrAwaitValue()
        assertThat(allWords).doesNotContain(word)
    }


}