package com.example.pokeinfo.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonDatabaseTest {

    private lateinit var database: PokemonDatabase
    private lateinit var pokemonDao: PokemonDAO

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PokemonDatabase::class.java)
            .allowMainThreadQueries().build()
        pokemonDao = database.pokemonDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testInsertAndRetrievePokemon() = runBlocking {
        val pokemon = listOf(
            Pokemon("Bulbasaur", "https//:/pokemon.com/bulbasaur"),
            Pokemon("Pikachu", "https//:/pokemon.com/pikachu")
        )

        pokemonDao.insertPokemon(pokemon)

        val retrievedPokemon = pokemonDao.getPokemons()


        assertThat(retrievedPokemon.size).isEqualTo(2)
    }
}