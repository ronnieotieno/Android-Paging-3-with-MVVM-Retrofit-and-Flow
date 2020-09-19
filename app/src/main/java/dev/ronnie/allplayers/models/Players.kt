package dev.ronnie.allplayers.models

import dev.ronnie.allplayers.models.Data
import dev.ronnie.allplayers.models.Meta

data class Players(
    val `data`: List<Data>,
    val meta: Meta
)