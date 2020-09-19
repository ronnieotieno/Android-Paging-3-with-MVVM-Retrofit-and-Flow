package dev.ronnie.allplayers.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.ronnie.allplayers.data.PlayersRepository
import dev.ronnie.allplayers.models.Data
import kotlinx.coroutines.flow.Flow

class MainViewModel(val repository: PlayersRepository) : ViewModel() {

    private var currentResult: Flow<PagingData<Data>>? = null

    fun searchPlayers(

    ): Flow<PagingData<Data>> {

        val newResult: Flow<PagingData<Data>> =
            repository.getPlayers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}