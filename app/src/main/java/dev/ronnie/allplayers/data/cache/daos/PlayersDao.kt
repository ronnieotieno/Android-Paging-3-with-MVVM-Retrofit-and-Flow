package dev.ronnie.allplayers.data.cache.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ronnie.allplayers.models.Player

@Dao
interface PlayersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayers(players: List<Player>)

    @Query("SELECT * FROM Players_Table")
    fun getPlayers(): PagingSource<Int, Player>

    @Query("DELETE FROM Players_Table")
    suspend fun deletePlayers()

}