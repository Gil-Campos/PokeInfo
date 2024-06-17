package com.example.pokeinfo.di.modules

import android.content.Context
import androidx.room.Room
import com.example.pokeinfo.data.local.PokemonDAO
import com.example.pokeinfo.data.local.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PokemonDatabase {
        return Room.databaseBuilder(
            appContext,
            PokemonDatabase::class.java,
            "pokemon_database"
        ).build()
    }

    @Provides
    fun providesPokemonDao(pokemonDatabase: PokemonDatabase): PokemonDAO {
        return pokemonDatabase.pokemonDao()
    }
}