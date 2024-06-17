package com.example.pokeinfo.domain.usecase

import com.example.pokeinfo.domain.model.PokemonHomeInfo
import com.example.pokeinfo.domain.repository.PokemonHomeRepository
import javax.inject.Inject

class DoInsertPokemonList @Inject constructor(private val repository: PokemonHomeRepository) {

    suspend fun run(pokemons: PokemonHomeInfo) {
        repository.insertPokemons(pokemons)
    }
}