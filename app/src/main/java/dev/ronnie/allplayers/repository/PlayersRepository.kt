package dev.ronnie.allplayers.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import dev.ronnie.allplayers.data.api.PlayersApi
import dev.ronnie.allplayers.data.cache.AppDatabase
import dev.ronnie.allplayers.models.Player
import dev.ronnie.allplayers.paging.PlayersRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayersRepository @Inject constructor(
    private val playersApi: PlayersApi,
    private val appDatabase: AppDatabase
) {

    @ExperimentalPagingApi
    fun getPlayers(
    ): Flow<PagingData<Player>> {
        val playersPagingConfig = PagingConfig(
            enablePlaceholders = false,
            pageSize = PAGE_SIZE,
            maxSize = PAGE_SIZE + (PAGE_SIZE * 4)
        )
        val playersRemoteMediator = PlayersRemoteMediator(playersApi, appDatabase)
        val playersPagingSource = { appDatabase.playersDao().getPlayers() }

        return Pager(
            config = playersPagingConfig,
            remoteMediator = playersRemoteMediator,
            pagingSourceFactory = playersPagingSource
        ).flow
    }

    /**
     * The same thing but with Livedata
     */
    @ExperimentalPagingApi
    fun getPlayersLiveData(): LiveData<PagingData<Player>> {
        val playersPagingConfig = PagingConfig(
            enablePlaceholders = false,
            pageSize = PAGE_SIZE,
            maxSize = PAGE_SIZE + (PAGE_SIZE * 4)
        )
        val playersRemoteMediator = PlayersRemoteMediator(playersApi, appDatabase)
        val playersPagingSource = { appDatabase.playersDao().getPlayers() }

        return Pager(
            config = playersPagingConfig,
            remoteMediator = playersRemoteMediator,
            pagingSourceFactory = playersPagingSource
        ).liveData

    }

    companion object {
        private const val PAGE_SIZE = 25
    }

}