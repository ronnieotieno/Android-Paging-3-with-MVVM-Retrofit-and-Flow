package dev.ronnie.allplayers.data

import androidx.paging.PagingSource
import dev.ronnie.allplayers.api.PlayersApi
import dev.ronnie.allplayers.models.Player
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.HttpRetryException
import javax.inject.Inject

const val STARTING_PAGE_INDEX = 1

class PlayersDataSource(private val playersApi: PlayersApi) :
    PagingSource<Int, Player>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Player> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = playersApi.getPlayers(params.loadSize, page)
            val players = response.playersList
            LoadResult.Page(
                data = players,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.meta.next_page == null) null else page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }
}