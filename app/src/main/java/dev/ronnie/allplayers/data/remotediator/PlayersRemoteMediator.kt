 package dev.ronnie.allplayers.data.remotediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.ronnie.allplayers.api.PlayersApi
import dev.ronnie.allplayers.data.entity.RemoteKeys
import dev.ronnie.allplayers.data.db.AppDataBase
import dev.ronnie.allplayers.models.Player
import dev.ronnie.allplayers.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class PlayersRemoteMediator(
    private val service: PlayersApi,
    private val db: AppDataBase
) : RemoteMediator<Int, Player>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Player>): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                if (db.playersDao.count() > 0) return MediatorResult.Success(false)
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)

            }
            LoadType.APPEND -> {

                getKey()
            }
        }

        try {

            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getPlayers(state.config.pageSize, page)

            val playersList = apiResponse.playersList

            val endOfPaginationReached =
                apiResponse.meta.next_page == null || apiResponse.meta.current_page == apiResponse.meta.total_pages

            db.withTransaction {

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )
                db.playersDao.insertMultiplePlayers(playersList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }


}