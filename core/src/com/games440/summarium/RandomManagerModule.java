package com.games440.summarium;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RandomManagerModule {
    @Provides
    @Singleton
    public RandomManager provideRandomManager()
    {
        return new RandomManager();
    }
}
