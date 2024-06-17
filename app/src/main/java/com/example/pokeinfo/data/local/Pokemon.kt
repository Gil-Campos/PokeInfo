package com.example.pokeinfo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey
    val name: String,
    val imageUrl: String
)