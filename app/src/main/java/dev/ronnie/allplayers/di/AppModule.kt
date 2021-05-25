package dev.ronnie.allplayers.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ronnie.allplayers.api.PlayersApi
import dev.ronnie.allplayers.data.db.AppDataBase
import dev.ronnie.allplayers.data.dao.PlayersDao
import dev.ronnie.allplayers.data.dao.RemoteKeysDao
import dev.ronnie.allplayers.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


    @Provides
    @Singleton
    fun providesDB(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.invoke(context)
    }

    @Singleton
    @Provides
    fun providesKeysDao(appDataBase: AppDataBase): RemoteKeysDao = appDataBase.remoteKeysDao

    @Singleton
    @Provides
    fun providesDao(appDataBase: AppDataBase): PlayersDao = appDataBase.playersDao

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providePlayersApi(retrofit: Retrofit): PlayersApi = retrofit.create(PlayersApi::class.java)
}
