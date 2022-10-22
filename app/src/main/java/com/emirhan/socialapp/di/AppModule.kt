
package com.emirhan.socialapp.di

import android.content.Context
import com.emirhan.socialapp.data.network.HomeDataSourceImpl
import com.emirhan.socialapp.data.network.LoginDataSourceImpl
import com.emirhan.socialapp.data.repository.HomeRepositoryImpl
import com.emirhan.socialapp.data.repository.LoginRepositoryImpl
import com.emirhan.socialapp.domain.network.ConnectivityObserver
import com.emirhan.socialapp.domain.repository.HomeRepository
import com.emirhan.socialapp.domain.repository.LoginRepository
import com.emirhan.socialapp.domain.utils.NetworkConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    // Providing data here
    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext appContext: Context
    ): ConnectivityObserver = NetworkConnectivityObserver(appContext)


    @Provides
    @Singleton
    fun provideLoginDataSource(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ) = LoginDataSourceImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideLoginRepository(
        dataSource: LoginDataSourceImpl
    ): LoginRepository = LoginRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideHomeDataSource(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ) = HomeDataSourceImpl(firestore, storage)

    @Provides
    @Singleton
    fun provideHomeRepository(
        dataSource: HomeDataSourceImpl
    ): HomeRepository = HomeRepositoryImpl(dataSource)
}