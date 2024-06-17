package com.example.pokeinfo.di.modules

import com.example.pokeinfo.data.local.PokemonDAO
import com.example.pokeinfo.data.remote.PokeApi
import com.example.pokeinfo.data.repository.PokemonHomeRepositoryImpl
import com.example.pokeinfo.domain.repository.PokemonHomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Singleton
    @Provides
    fun providesHomeRepository(pokeApi: PokeApi, pokemonDAO: PokemonDAO): PokemonHomeRepository {
        return PokemonHomeRepositoryImpl(pokeApi, pokemonDAO)
    }
}