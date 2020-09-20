package dev.ronnie.allplayers.models

import com.google.gson.annotations.SerializedName


data class Players(

    @SerializedName("data")
    val playersList: List<Player>,
    val meta: Meta
)