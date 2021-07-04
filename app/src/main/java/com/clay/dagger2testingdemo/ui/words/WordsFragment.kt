package com.clay.dagger2testingdemo.ui.words

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clay.dagger2testingdemo.R
import com.clay.dagger2testingdemo.adapters.WordAdapter
import com.clay.dagger2testingdemo.databinding.FragmentWordsBinding
import com.clay.dagger2testingdemo.other.Resource
import com.clay.dagger2testingdemo.ui.BaseFragment
import com.clay.dagger2testingdemo.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordsFragment constructor(
    var viewModel: MainViewModel? = null
) : BaseFragment(R.layout.fragment_words) {

    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!


    private lateinit var wordsAdapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordsBinding.bind(view)
        setupUI()
        setupEventListeners()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel?.words?.observe(viewLifecycleOwner) {
            with(binding) {
                containerEmptyWords.root.isVisible = it.isEmpty()
                rvWords.isVisible = it.isNotEmpty()
            }
            wordsAdapter.words = it
        }
        viewModel?.insertWord?.observe(viewLifecycleOwner) {
            it?.let { event ->
                when (event.peekContent()) {
                    is Resource.Success -> {
                        event.getContentIfNotHandled()?.let {
                            showSnackBar(binding.root, "Success. Inserted word")
                        }
                    }
                    is Resource.Error -> {
                        showSnackBar(
                            binding.root,
                            event.getContentIfNotHandled()?.message ?: "An unknown error"
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
        wordsAdapter = WordAdapter()
        binding.apply {
            rvWords.apply {
                adapter = wordsAdapter
                layoutManager = LinearLayoutManager(requireContext())
                ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
            }
        }
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val word = wordsAdapter.words[position]
            viewModel?.deleteWord(word)
            Snackbar.make(binding.fabAddWord, "Deleted word", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel?.insertWordIntoDb(word)
                }
            }.show()
        }
    }

    private fun setupEventListeners() {
        binding.apply {
            fabAddWord.setOnClickListener {
                findNavController().navigate(
                    WordsFragmentDirections.actionWordsFragmentToAddWordFragment()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}