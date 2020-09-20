package dev.ronnie.allplayers.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.ronnie.allplayers.models.Player
import kotlinx.coroutines.flow.Flow

class PlayersRepository {

    fun getPlayers(

    ): Flow<PagingData<Player>> {

        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                PlayersDataSource()
            }
        ).flow

    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20

        // For Singleton instantiation
        @Volatile
        private var instance: PlayersRepository? = null

        fun getInstance(

        ) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: PlayersRepository()
                            .also { instance = it }
                }
    }

}