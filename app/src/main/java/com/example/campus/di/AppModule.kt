package com.example.campus.di

import com.example.campus.data.repo.EventRepository
import com.example.campus.data.repo.EventRepositoryImp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun providesFireStoreInstance():FirebaseFirestore{
        return FirebaseFirestore.getInstance()

    }
    @Provides
    @Singleton
    fun providesEventRepository(database:FirebaseFirestore):EventRepository{
        return EventRepositoryImp(database)

    }
}