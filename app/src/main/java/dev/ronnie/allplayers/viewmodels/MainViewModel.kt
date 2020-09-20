package dev.ronnie.allplayers.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.ronnie.allplayers.data.PlayersRepository
import dev.ronnie.allplayers.models.Player
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: PlayersRepository) : ViewModel() {

    private var currentResult: Flow<PagingData<Player>>? = null

    fun searchPlayers(): Flow<PagingData<Player>> {
        val newResult: Flow<PagingData<Player>> =
            repository.getPlayers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}