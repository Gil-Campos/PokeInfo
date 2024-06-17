package com.example.pokeinfo.domain.repository

import com.example.pokeinfo.data.local.Pokemon
import com.example.pokeinfo.domain.model.PokemonHomeInfo
import com.example.pokeinfo.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonHomeRepository {
    suspend fun getPokemonList(offset: Int, limit: Int): Flow<Resource<PokemonHomeInfo>>

    suspend fun insertPokemons(pokemons: PokemonHomeInfo)

}