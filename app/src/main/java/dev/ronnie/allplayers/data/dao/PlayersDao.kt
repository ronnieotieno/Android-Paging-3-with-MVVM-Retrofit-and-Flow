package dev.ronnie.allplayers.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ronnie.allplayers.models.Player

@Dao
interface PlayersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiplePlayers(list: List<Player>)

    @Query("SELECT * FROM players_table")
    fun getPlayers(): PagingSource<Int, Player>

    @Query("DELETE FROM players_table")
    suspend fun clearRepos()

    @Query("SELECT COUNT(id) from players_table")
    suspend fun count(): Int

}