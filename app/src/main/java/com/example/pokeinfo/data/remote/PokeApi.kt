package com.example.pokeinfo.data.remote

import com.example.pokeinfo.data.model.pokemon_info.PokemonInfo
import com.example.pokeinfo.data.model.pokemon_list.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("api/v2/pokemon")
    suspend fun getPaginatedPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonListResponse>

    @GET("api/v2/pokemon/{name}")
    suspend fun getPokemonImage(
        @Path("name") name: String
    ): Response<PokemonInfo>
}
