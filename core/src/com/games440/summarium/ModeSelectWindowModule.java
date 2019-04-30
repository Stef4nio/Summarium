package com.games440.summarium;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModeSelectWindowModule {
    @Provides
    @Singleton
    public ModeSelectWindow provideModeSelectWindow()
    {
        return new ModeSelectWindow();
    }
}
