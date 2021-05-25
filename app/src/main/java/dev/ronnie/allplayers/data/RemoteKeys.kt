package dev.ronnie.allplayers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val prevKey: Int?,
    val nextKey: Int?
)