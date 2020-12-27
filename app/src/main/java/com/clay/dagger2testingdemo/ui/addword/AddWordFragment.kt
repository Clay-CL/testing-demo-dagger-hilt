package com.clay.dagger2testingdemo.ui.addword

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.clay.dagger2testingdemo.R
import com.clay.dagger2testingdemo.databinding.FragmentAddWordBinding
import com.clay.dagger2testingdemo.other.Resource
import com.clay.dagger2testingdemo.ui.BaseFragment
import com.clay.dagger2testingdemo.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWordFragment : BaseFragment(R.layout.fragment_add_word) {

    private var _binding: FragmentAddWordBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddWordBinding.bind(view)
        setupUI()
        subscribeToObservers()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subscribeToObservers() {
        viewModel.insertWord.observe(viewLifecycleOwner) {
            it?.let { event ->
                when (val result = event.peekContent()) {
                    is Resource.Success -> {
                        event.getContentIfNotHandled()?.let {
                            findNavController().popBackStack()
                        }
                    }
                    is Resource.Error -> {
                        showSnackBar(
                            binding.fabInsertWord,
                            result.message ?: "An unknown error"
                        )
                    }
                    is Resource.Loading -> {
                        /*NO-OP*/
                    }
                }
            }
        }
    }


    private fun setupUI() {
        binding.apply {
            fabInsertWord.setOnClickListener {
                val word = etNewWord.text.toString()
                val meaning = etWordMeaning.text.toString()
                viewModel.insertWord(word, meaning)
            }
        }
    }

}