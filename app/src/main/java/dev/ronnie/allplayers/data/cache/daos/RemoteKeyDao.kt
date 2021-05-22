package dev.ronnie.allplayers.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ronnie.allplayers.models.RemoteKey

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRemoteKeys(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM Remote_Key_Table WHERE Player_ID=:playerId")
    suspend fun getRemoteKey(playerId: Int): RemoteKey?

    @Query("DELETE FROM Players_Table")
    suspend fun deleteRemoteKeys()

}