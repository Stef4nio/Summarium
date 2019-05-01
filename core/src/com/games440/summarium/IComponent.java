package com.games440.summarium;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ModelModule.class,EventManagerModule.class,StateManagerModule.class,ModeSelectWindowModule.class,RandomManagerModule.class})
public interface IComponent {
    void inject(GameView gameView);
    void inject(GameController gameController);
    void inject(ViewCell viewCell);
    void inject(ModelModule modelModule);
    void inject(UIView uiView);
    void inject(ModeSelectWindow modeSelectWindow);
    void inject(StateManager stateManager);
}
