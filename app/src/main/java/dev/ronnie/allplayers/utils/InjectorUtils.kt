package dev.ronnie.allplayers.utils

import android.content.Context
import dev.ronnie.allplayers.data.PlayersRepository
import dev.ronnie.allplayers.viewmodels.MainViewModelFactory


object InjectorUtils {

    fun provideViewModelFactory() = MainViewModelFactory(
        getRepository()
    )

    private fun getRepository() = PlayersRepository.getInstance()

}