package com.games440.summarium;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StateManagerModule {
    @Provides
    @Singleton
    public StateManager provideStateManager()
    {
        return new StateManager();
    }
}
