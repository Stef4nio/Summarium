package com.games440.summarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import javax.inject.Inject;

public class GameController{
    @Inject
    public EventManager _eventManager;
    @Inject
    public GameModel _gameModel;


    public GameController(Preferences playerPrefs)
    {
        DaggerContainer.getDaggerBinder().inject(this);
        _eventManager.Subscribe(EventType.Click,new EventListener(){
            @Override
            public void HandleEvent(int param) {
                _gameModel.HandleInput(param);
                SoundManager.getSoundManager().PlaySound(SoundType.Click);
            }
        });
        final Preferences finalPlayerPrefs = playerPrefs;
        _eventManager.Subscribe(EventType.RestartNeeded,new EventListener(){
            @Override
            public void HandleEvent() {
                if(!finalPlayerPrefs.getBoolean(GameConfig.isFirstLaunchEverKey,true)) {
                    _gameModel.RestartModel();
                }
            }
        });
        _eventManager.Subscribe(EventType.AimChanged,new EventListener(){
            @Override
            public void HandleEvent(int param) {
                _gameModel.setAim(param);
            }
        });
        _eventManager.Subscribe(EventType.GameStateChanged, new GameStateChangeListener(){
            @Override
            public void HandleEvent(GameState gameState) {
                if(gameState == GameState.Adding)
                {
                    _gameModel.clearOffsets();
                }
                if(gameState == GameState.Idle)
                {
                    _gameModel.makeAllCellsNotNew();
                }
            }
        });
    }


}