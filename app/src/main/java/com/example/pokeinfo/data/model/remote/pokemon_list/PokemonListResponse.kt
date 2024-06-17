package com.example.pokeinfo.data.model.remote.pokemon_list

import androidx.annotation.Keep

@Keep
data class PokemonListResponse(
    val results: List<Pokemon>
)
