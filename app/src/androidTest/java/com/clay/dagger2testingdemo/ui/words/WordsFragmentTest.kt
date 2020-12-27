package com.clay.dagger2testingdemo.ui.words

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.clay.dagger2testingdemo.R
import com.clay.dagger2testingdemo.adapters.WordAdapter
import com.clay.dagger2testingdemo.data.local.entities.Word
import com.clay.dagger2testingdemo.getOrAwaitValue
import com.clay.dagger2testingdemo.launchFragmentInHiltContainer
import com.clay.dagger2testingdemo.ui.MainAndroidTestFragmentFactory
import com.clay.dagger2testingdemo.ui.MainViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class WordsFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var testFragmentFactory: MainAndroidTestFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
        testFragmentFactory = MainAndroidTestFragmentFactory()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun clickOnAddWord_navigatesToAddWordFragment() = runBlockingTest {

        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<WordsFragment>(Bundle(), R.style.Theme_AppCompat) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fab_add_word)).perform(click())

        verify(navController).navigate(
            WordsFragmentDirections.actionWordsFragmentToAddWordFragment()
        )

    }

    @Test
    fun swipeWord_deleteItemInDb() = runBlockingTest {
        val word = Word("word", "meaning")
        var testViewModel: MainViewModel? = null
        launchFragmentInHiltContainer<WordsFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = this.viewModel
            viewModel?.insertWordIntoDb(word)
        }

        onView(withId(R.id.rv_words)).perform(
            RecyclerViewActions.actionOnItemAtPosition<WordAdapter.WordViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.words?.getOrAwaitValue()).isEmpty()

    }


}



















