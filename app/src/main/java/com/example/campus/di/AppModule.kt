package com.example.campus.di

import com.example.campus.data.repo.EventRepository
import com.example.campus.data.repo.EventRepositoryImp
import com.example.campus.data.repo.TicketRepository
import com.example.campus.data.repo.TicketRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
    fun providesEventRepository(database:FirebaseFirestore,storageReference: StorageReference):EventRepository{
        return EventRepositoryImp(database,storageReference)

    }

    @Provides
    @Singleton
    fun providesFireBaseStorageInstance():StorageReference{
        return FirebaseStorage.getInstance().getReference("app")

    }

    @Provides
    @Singleton
    fun providesFireBaseAuthInstance():FirebaseAuth{
        return FirebaseAuth.getInstance()

    }

    @Provides
    @Singleton
    fun providesTicketRepository(database:FirebaseFirestore,auth: FirebaseAuth):TicketRepository{
        return TicketRepositoryImp(database,auth)

    }
}