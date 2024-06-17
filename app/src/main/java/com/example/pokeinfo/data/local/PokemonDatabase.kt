package com.example.pokeinfo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Pokemon::class], version = 1)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDAO
}