package com.example.pokeinfo.data.mappers

import com.example.pokeinfo.domain.model.PokemonHomeInfo
import com.example.pokeinfo.domain.model.PokemonList

fun pokemonResponseHomeMapper(name: List<String>, images: List<String>): PokemonHomeInfo {
    val pokemonHomeList = mutableListOf<PokemonList>()

    name.forEachIndexed { index: Int, s: String ->
        pokemonHomeList.add(PokemonList(s, images[index]))
    }

    return PokemonHomeInfo(pokemonHomeList)
}