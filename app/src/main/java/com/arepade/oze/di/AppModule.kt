package com.arepade.oze.di

import android.app.Application
import androidx.room.Room
import com.arepade.oze.BuildConfig
import com.arepade.oze.database.BookmarkedDao
import com.arepade.oze.database.UsersDao
import com.arepade.oze.database.UsersDatabase
import com.arepade.oze.network.Github
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingLevel = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.apply { httpLoggingInterceptor.level = loggingLevel }
    }


    @Provides
    @Singleton
    fun provideGithub(
        client: OkHttpClient
    ): Github =
        Retrofit.Builder().apply {
            baseUrl("https://api.github.com/")
            client(client)
            addConverterFactory(GsonConverterFactory.create())
        }.build().create(Github::class.java)


    @Provides
    fun getClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(httpLoggingInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(90, TimeUnit.SECONDS)
        .build()


    @Provides
    fun getDatabase(application: Application): UsersDatabase = Room.databaseBuilder(
        application.applicationContext,
        UsersDatabase::class.java,
        "barcode"
    ).build()

    @Provides
    @Singleton
    fun getUsersDao(usersDatabase: UsersDatabase): UsersDao = usersDatabase.usersDao

    @Provides
    @Singleton
    fun getBookmarkDao(usersDatabase: UsersDatabase): BookmarkedDao = usersDatabase.bookmarkedDao


}