package dev.ronnie.allplayers.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.ronnie.allplayers.data.cache.daos.PlayersDao
import dev.ronnie.allplayers.data.cache.daos.RemoteKeyDao
import dev.ronnie.allplayers.models.Player
import dev.ronnie.allplayers.models.RemoteKey

@Database(entities = [Player::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playersDao(): PlayersDao

    abstract fun remoteKeyDao(): RemoteKeyDao

}