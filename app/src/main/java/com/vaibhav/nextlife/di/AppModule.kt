package com.vaibhav.nextlife.di

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesAuth() = Firebase.auth

    @Provides
    @Singleton
    fun providesFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun providesStorage() = Firebase.storage

    @Provides
    @Singleton
    fun providesPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences("NextLife", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesApplicationContext(@ApplicationContext context: ApplicationContext) = context
}