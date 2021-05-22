package dev.ronnie.allplayers.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Players_Table")
data class Player(
    @ColumnInfo(name = "First Name")
    val first_name: String,

    @ColumnInfo(name = "Height Feet")
    val height_feet: Int,

    @ColumnInfo(name = "Height Inches")
    val height_inches: Int,

    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "Last Name")
    val last_name: String,

    @ColumnInfo(name = "Position")
    val position: String,

    @Embedded(prefix = "Team")
    val team: Team,

    @ColumnInfo(name = "Weight Pounds")
    val weight_pounds: Int
)