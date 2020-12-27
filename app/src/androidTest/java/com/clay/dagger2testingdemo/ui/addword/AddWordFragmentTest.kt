package com.clay.dagger2testingdemo.ui.addword

import android.text.TextUtils.replace
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.clay.dagger2testingdemo.R
import com.clay.dagger2testingdemo.data.local.entities.Word
import com.clay.dagger2testingdemo.data.repositories.FakeAndroidTestWordRepository
import com.clay.dagger2testingdemo.getOrAwaitValue
import com.clay.dagger2testingdemo.launchFragmentInHiltContainer
import com.clay.dagger2testingdemo.ui.MainAndroidTestFragmentFactory
import com.clay.dagger2testingdemo.ui.MainViewModel
import com.clay.dagger2testingdemo.ui.words.WordsFragment
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


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddWordFragmentTest {

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
    fun pressBackButton_popBackStack() = runBlockingTest {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddWordFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()
        verify(navController).popBackStack()

    }

    @Test
    fun addWord_insertedIntoDb() = runBlockingTest {

        val testViewModel = MainViewModel(FakeAndroidTestWordRepository())
        launchFragmentInHiltContainer<AddWordFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            viewModel = testViewModel
        }

        onView(withId(R.id.et_new_word)).perform(replaceText("word"))
        onView(withId(R.id.et_word_meaning)).perform(replaceText("meaning"))

        onView(withId(R.id.fab_insert_word)).perform(click())

        assertThat(testViewModel.words.getOrAwaitValue()).contains(Word("word", "meaning"))


    }

}