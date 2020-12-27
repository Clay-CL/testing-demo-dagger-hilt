package com.clay.dagger2testingdemo.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.clay.dagger2testingdemo.MainCoroutineRule
import com.clay.dagger2testingdemo.getOrAwaitValueTest
import com.clay.dagger2testingdemo.other.Resource
import com.clay.dagger2testingdemo.repositories.FakeWordRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        viewModel = MainViewModel(FakeWordRepository())
    }

    @Test
    fun `insert empty word with meaning returns error`() {
        viewModel.insertWord("", "meaning")
        val insertStatus = viewModel.insertWord.getOrAwaitValueTest()
        assertThat(insertStatus.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert word with empty meaning returns error`() {
        viewModel.insertWord("word", "")
        val insertStatus = viewModel.insertWord.getOrAwaitValueTest()
        assertThat(insertStatus.getContentIfNotHandled()).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `insert word with no empty fields returns success`() = mainCoroutineRule.runBlockingTest {
        mainCoroutineRule.pauseDispatcher()

        viewModel.insertWord("word", "meaning")

        var insertStatus = viewModel.insertWord.getOrAwaitValueTest()
        assertThat(insertStatus.getContentIfNotHandled()).isInstanceOf(Resource.Loading::class.java)

        mainCoroutineRule.resumeDispatcher()

        insertStatus = viewModel.insertWord.getOrAwaitValueTest()
        assertThat(insertStatus.getContentIfNotHandled()).isInstanceOf(Resource.Success::class.java)
    }

}