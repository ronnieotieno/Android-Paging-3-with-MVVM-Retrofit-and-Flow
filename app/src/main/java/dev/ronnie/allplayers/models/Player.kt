package dev.ronnie.allplayers.models

data class Player(
    val first_name: String,
    val height_feet: Any,
    val height_inches: Any,
    val id: Int,
    val last_name: String,
    val position: String,
    val team: Team,
    val weight_pounds: Any
)