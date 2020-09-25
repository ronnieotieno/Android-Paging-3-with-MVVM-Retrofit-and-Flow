package dev.ronnie.allplayers.viewmodels


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.ronnie.allplayers.data.PlayersRepository
import dev.ronnie.allplayers.models.Player
import kotlinx.coroutines.flow.Flow

class MainViewModel @ViewModelInject constructor(
    private val repository: PlayersRepository
) : ViewModel() {

    private var currentResult: Flow<PagingData<Player>>? = null

    fun searchPlayers(): Flow<PagingData<Player>> {
        val newResult: Flow<PagingData<Player>> =
            repository.getPlayers().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    /**
     * Same thing but with Livedata
     */
    private var currentResultLiveData: LiveData<PagingData<Player>>? = null

    fun searchPlayersLiveData(): LiveData<PagingData<Player>> {
        val newResultLiveData: LiveData<PagingData<Player>> =
            repository.getPlayersLiveData().cachedIn(viewModelScope)
        currentResultLiveData = newResultLiveData
        return newResultLiveData
    }


}