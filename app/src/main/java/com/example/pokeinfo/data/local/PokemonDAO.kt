package com.example.pokeinfo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(user: List<Pokemon>)

    @Query("SELECT * FROM pokemon_table")
    fun getPokemons(): List<Pokemon>
}