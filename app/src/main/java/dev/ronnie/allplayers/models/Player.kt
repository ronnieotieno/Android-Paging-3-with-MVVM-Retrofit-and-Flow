package dev.ronnie.allplayers.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dev.ronnie.allplayers.utils.TeamConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@TypeConverters(TeamConverter::class)
@Entity(tableName = "players_table")
data class Player(
    val first_name: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val last_name: String,
    val position: String,
    val team: Team,
) : Parcelable