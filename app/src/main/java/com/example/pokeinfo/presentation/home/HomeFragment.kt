package com.example.pokeinfo.presentation.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeinfo.R
import com.example.pokeinfo.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var pokemonAdapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observer()
        viewModel.getPokemons()
    }

    private fun observer() {
        getPokemons()
        showHideLoading()
    }

    private fun getPokemons() {
        viewModel.pokemonList.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                pokemonAdapter.pokemons = it
            }
        }
    }

    private fun showHideLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvPokemonList.apply {
            pokemonAdapter = PokemonListAdapter()
            adapter = pokemonAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}