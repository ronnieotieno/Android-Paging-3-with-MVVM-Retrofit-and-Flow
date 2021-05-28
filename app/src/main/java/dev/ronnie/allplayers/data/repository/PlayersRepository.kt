package dev.ronnie.allplayers.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import dev.ronnie.allplayers.api.PlayersApi
import dev.ronnie.allplayers.data.datasource.PlayersDataSource
import dev.ronnie.allplayers.data.db.AppDataBase
import dev.ronnie.allplayers.data.remotediator.PlayersRemoteMediator
import dev.ronnie.allplayers.models.Player
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayersRepository @Inject constructor(
    private val playersApi: PlayersApi,
    private val db: AppDataBase
) {


    private val pagingSourceFactory = { db.playersDao.getPlayers() }

    /**
     * for caching
     */
    @ExperimentalPagingApi
    fun getPlayers(): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PlayersRemoteMediator(
                playersApi,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow //can also return livedata
    }
    /**
     * Use this if you dont want to cache data to room
     */
//    fun getPlayers(
//    ): Flow<PagingData<Player>> {
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
//            pagingSourceFactory = {
//                PlayersDataSource(playersApi)
//            }
//        ).flow
//    }

    /**
     * The same thing but with Livedata
     */
    fun getPlayersLiveData(

    ): LiveData<PagingData<Player>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                PlayersDataSource(playersApi)
            }
        ).liveData

    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 25
    }

}