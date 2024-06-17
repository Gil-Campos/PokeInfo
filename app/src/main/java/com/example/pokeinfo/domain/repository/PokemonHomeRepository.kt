package com.example.pokeinfo.domain.repository

import com.example.pokeinfo.domain.model.PokemonHomeInfo
import com.example.pokeinfo.domain.model.PokemonList
import com.example.pokeinfo.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonHomeRepository {
    suspend fun getPokemonList(offset: Int, limit: Int): Flow<Resource<PokemonHomeInfo>>
}