package dev.ronnie.allplayers.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ronnie.allplayers.data.PlayersRepository
import dev.ronnie.allplayers.models.Player
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PlayersRepository
) : ViewModel() {
    private var currentResult: Flow<PagingData<Player>>? = null

    @ExperimentalPagingApi
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