package com.example.pokeinfo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeinfo.domain.model.PokemonHomeInfo
import com.example.pokeinfo.domain.model.PokemonList
import com.example.pokeinfo.domain.usecase.DoGetPokemonList
import com.example.pokeinfo.domain.usecase.DoInsertPokemonList
import com.example.pokeinfo.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val doGetPokemonList: DoGetPokemonList,
    private val doInsertPokemonList: DoInsertPokemonList
) : ViewModel() {

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var _pokemonList = MutableStateFlow<List<PokemonList>?>(null)
    val pokemonList: StateFlow<List<PokemonList>?> get() = _pokemonList

    private var _errorIsActive = MutableLiveData(false)
    val errorIsActive: LiveData<Boolean> get() = _errorIsActive

    private var limit = 15

    fun getPokemons() {
        _errorIsActive.value = false

        if (_pokemonList.value?.size == 15) {
            limit -= 5
        }


        viewModelScope.launch {
            doGetPokemonList.run(_pokemonList.value?.size ?: 0, limit).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _isLoading.value = false
                        _errorIsActive.value = true
                    }

                    is Resource.Loading -> {
                        _isLoading.value = true
                    }

                    is Resource.Success -> {
                        _isLoading.value = false
                        _pokemonList.value = resource.data.pokemonList
                        insertPokemons(resource.data)
                    }
                }
            }
        }
    }

    private fun insertPokemons(pokemonList: PokemonHomeInfo) {
        viewModelScope.launch {
            doInsertPokemonList.run(pokemonList)
        }
    }
}