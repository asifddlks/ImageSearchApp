package com.asifddlks.imagesearchapp

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.asifddlks.imagesearchapp.databinding.HomeFragmentBinding
import com.asifddlks.imagesearchapp.model.ImageModel
import com.asifddlks.imagesearchapp.viewadapter.ImageAdapter
import com.asifddlks.imagesearchapp.viewadapter.ImageLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment),
    ImageAdapter.OnItemClickListener {

    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: HomeFragmentBinding? = null
    private var layoutManager: GridLayoutManager? = null
    private val binding get() = _binding!!
    var selectedItem = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = HomeFragmentBinding.bind(view)

        val adapter = ImageAdapter(this)
        layoutManager = GridLayoutManager(requireContext(), 2)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = null

            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = ImageLoadStateAdapter { adapter.retry() },
                footer = ImageLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener { adapter.retry() }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(photo: ImageModel) {
        //val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        //findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_home, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val optionItem = menu.findItem(R.id.action_option)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        optionItem.setOnMenuItemClickListener {

            val spanCountList = arrayOf("2", "3", "4")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select number of columns")
            builder.setSingleChoiceItems(
                spanCountList,
                selectedItem
            ) { dialogInterface: DialogInterface, item: Int ->
                selectedItem = item
            }
            builder.setPositiveButton(R.string.set) { dialogInterface: DialogInterface, p1: Int ->
                layoutManager =
                    GridLayoutManager(requireContext(), spanCountList[selectedItem].toInt())
                binding.recyclerView.layoutManager = layoutManager
                binding.recyclerView.recycledViewPool.clear()
                dialogInterface.dismiss()
            }
            /*builder.setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, p1: Int ->
                dialogInterface.dismiss()
            }*/
            builder.create()
            builder.show();

            true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}