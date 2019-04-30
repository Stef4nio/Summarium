package com.games440.summarium;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EventManagerModule {
    private EventManager _eventManager = new EventManager();

    @Provides
    @Singleton
    public EventManager provideEventManager()
    {
        return _eventManager;
    }
}
