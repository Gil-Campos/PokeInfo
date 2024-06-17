package com.example.pokeinfo.data.model.remote.pokemon_info

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String
)
