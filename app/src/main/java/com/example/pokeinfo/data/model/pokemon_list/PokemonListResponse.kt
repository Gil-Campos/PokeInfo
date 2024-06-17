package com.example.pokeinfo.data.model.pokemon_list

import androidx.annotation.Keep

@Keep
data class PokemonListResponse(
    val results: List<Pokemon>
)
