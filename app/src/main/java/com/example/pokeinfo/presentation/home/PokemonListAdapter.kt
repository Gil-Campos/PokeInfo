package com.example.pokeinfo.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokeinfo.databinding.ItemPokeListBinding
import com.example.pokeinfo.domain.model.PokemonList

class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    private val pokemonList: MutableList<PokemonList> = mutableListOf()

    inner class ViewHolder(val binding: ItemPokeListBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<PokemonList>() {
        override fun areItemsTheSame(oldItem: PokemonList, newItem: PokemonList): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PokemonList, newItem: PokemonList): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var pokemons: List<PokemonList>
        get() = differ.currentList
        set(value) {
            pokemonList.addAll(value) // Append new data
            differ.submitList(pokemonList.toList()) // Submit updated list

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPokeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val pokemon = pokemons[position]
            tvPokemonName.text = pokemon.name
            ivPokemon.load(pokemon.imageUrl)
        }
    }
}