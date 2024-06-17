package com.example.pokeinfo.data.repository

import com.example.pokeinfo.data.mappers.pokemonResponseHomeMapper
import com.example.pokeinfo.data.remote.PokeApi
import com.example.pokeinfo.domain.model.PokemonHomeInfo
import com.example.pokeinfo.domain.repository.PokemonHomeRepository
import com.example.pokeinfo.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class PokemonHomeRepositoryImpl @Inject constructor(private val pokeApi: PokeApi) :
    PokemonHomeRepository {

    override suspend fun getPokemonList(offset: Int, limit: Int): Flow<Resource<PokemonHomeInfo>> {
        return channelFlow {
            send(Resource.Loading)
            try {

                val pokemonListResponse = pokeApi.getPaginatedPokemons(offset, limit)

                val pokemonImages = mutableListOf<String>()

                if (pokemonListResponse.isSuccessful) {
                    pokemonListResponse.body()?.results?.forEach {
                        val pokemonInfoResponse = pokeApi.getPokemonImage(it.name)

                        if (pokemonInfoResponse.isSuccessful) {
                            pokemonImages.add(pokemonInfoResponse.body()?.sprites?.frontDefault.orEmpty())
                        } else {
                            send(
                                Resource.Error(message = pokemonListResponse.errorBody().toString())
                            )
                        }
                    }

                    send(
                        Resource.Success(
                            pokemonResponseHomeMapper(
                                pokemonListResponse.body()?.results?.map { it.name }.orEmpty(),
                                pokemonImages
                            )
                        )
                    )

                } else {
                    send(
                        Resource.Error(message = pokemonListResponse.errorBody().toString())
                    )
                }

            } catch (e: Throwable) {
                send(Resource.Error(message = "An error occurred: ${e.message}"))
            }
        }
    }

}