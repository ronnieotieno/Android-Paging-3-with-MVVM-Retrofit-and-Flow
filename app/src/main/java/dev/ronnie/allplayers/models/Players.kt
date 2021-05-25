package dev.ronnie.allplayers.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Players(
    @SerializedName("data")
    val playersList: List<Player>,
    val meta: Meta
) : Parcelable