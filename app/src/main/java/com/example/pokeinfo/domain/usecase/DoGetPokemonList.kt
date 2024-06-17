package com.example.pokeinfo.domain.usecase

import com.example.pokeinfo.domain.repository.PokemonHomeRepository
import javax.inject.Inject

class DoGetPokemonList @Inject constructor(private val repository: PokemonHomeRepository) {

    suspend fun run(offset: Int, limit: Int) = repository.getPokemonList(offset, limit)
}