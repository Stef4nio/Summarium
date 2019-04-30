package com.games440.summarium;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    @Inject
    EventManager eventManager;
    @Inject
    StateManager stateManager;
    @Inject
    RandomManager randomManager;
    private GameModel _gameModel;


    @Provides
    @Singleton
    public GameModel provideGameModel()
    {
        InitModel();
        return _gameModel;
    }

    private void InitModel()
    {
        if(_gameModel==null)
        {
            DaggerContainer.getDaggerBinder().inject(this);
            _gameModel = new GameModel(eventManager,stateManager,randomManager);
        }
    }

    @Provides
    @Singleton
    public IGameModelReadonly provideReadonlyGameModel()
    {
        InitModel();
        return _gameModel;
    }
}
