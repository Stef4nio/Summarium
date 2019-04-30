package com.games440.summarium;

import javax.inject.Inject;

public class GameController{
    @Inject
    public EventManager _eventManager;
    @Inject
    public GameModel _gameModel;


    public GameController()
    {
        DaggerContainer.getDaggerBinder().inject(this);
        _eventManager.Subscribe(EventType.Click,new EventListener(){
            @Override
            public void HandleEvent(int param) {
                _gameModel.HandleInput(param);
                SoundManager.getSoundManager().PlaySound(SoundType.Click);
            }
        });
        _eventManager.Subscribe(EventType.RestartNeeded,new EventListener(){
            @Override
            public void HandleEvent() {
                _gameModel.RestartModel();
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