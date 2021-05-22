package dev.ronnie.allplayers.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.ronnie.allplayers.data.api.PlayersApi
import dev.ronnie.allplayers.data.cache.AppDatabase
import dev.ronnie.allplayers.models.Player
import dev.ronnie.allplayers.models.RemoteKey
import dev.ronnie.allplayers.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class PlayersRemoteMediator(private val playersApi: PlayersApi, private val appDatabase: AppDatabase) :
    RemoteMediator<Int, Player>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        pagingState: PagingState<Int, Player>
    ): MediatorResult {
        val pageKeyData = getPagedData(loadType, pagingState)

        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response =
                playersApi.fetchPlayers(per_page = pagingState.config.pageSize, page = page)
            val players = response.playersList
            val isEndOfList = response.playersList.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.playersDao().deletePlayers()
                    appDatabase.remoteKeyDao().deleteRemoteKeys()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.playersList.map {
                    RemoteKey(playerId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                appDatabase.remoteKeyDao().saveRemoteKeys(remoteKeys = keys)
                appDatabase.playersDao().savePlayers(players = players)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            return MediatorResult.Error(error)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getPagedData(
        loadType: LoadType,
        pagingState: PagingState<Int, Player>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(pagingState)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }

            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(pagingState)
                val nextKey = remoteKey?.nextKey
                nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKey = getFirstRemoteKey(pagingState)
                val prevKey = remoteKey?.prevKey
                prevKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(pagingState: PagingState<Int, Player>): RemoteKey? {
        return pagingState.anchorPosition?.let { position ->
            pagingState.closestItemToPosition(position)?.id?.let { playerId ->
                appDatabase.remoteKeyDao().getRemoteKey(playerId = playerId)
            }
        }
    }

    private suspend fun getLastRemoteKey(pagingState: PagingState<Int, Player>): RemoteKey? {
        return pagingState.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { player -> appDatabase.remoteKeyDao().getRemoteKey(playerId = player.id) }
    }

    private suspend fun getFirstRemoteKey(pagingState: PagingState<Int, Player>): RemoteKey? {
        return pagingState.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { player -> appDatabase.remoteKeyDao().getRemoteKey(playerId = player.id) }
    }

}