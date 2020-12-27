package com.clay.dagger2testingdemo.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.clay.dagger2testingdemo.ui.addword.AddWordFragment
import com.clay.dagger2testingdemo.ui.words.WordsFragment

class MainFragmentFactory: FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            WordsFragment::class.java.name -> WordsFragment()
            AddWordFragment::class.java.name -> AddWordFragment()
            else -> super.instantiate(classLoader, className)
        }
    }

}