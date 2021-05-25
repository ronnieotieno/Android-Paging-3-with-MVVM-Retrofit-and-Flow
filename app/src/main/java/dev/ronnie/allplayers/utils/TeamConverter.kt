package dev.ronnie.allplayers.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.ronnie.allplayers.models.Team
import java.lang.reflect.Type

class TeamConverter {

    private val gSon = Gson()

    private val type: Type = object : TypeToken<Team?>() {}.type

    @TypeConverter
    fun from(team: Team?): String? {
        if (team == null) {
            return null
        }

        return gSon.toJson(team, type)
    }

    @TypeConverter
    fun to(teamString: String?): Team? {
        if (teamString == null) {
            return null
        }
        return gSon.fromJson(teamString, type)
    }
}