package dev.ronnie.allplayers.api

import dev.ronnie.allplayers.models.Players
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayersApi {

    @GET("players")
    suspend fun getPlayers(
        @Query("per_page") per_page: Int?,
        @Query("page") page: Int?,
    ): Players

}