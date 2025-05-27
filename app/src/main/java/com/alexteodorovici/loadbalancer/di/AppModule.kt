package com.alexteodorovici.loadbalancer.di

import com.alexteodorovici.loadbalancer.data.repository.LoadBalancerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoadBalancerRepository(): LoadBalancerRepository {
        return LoadBalancerRepository()

    }
}