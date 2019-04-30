package com.games440.summarium;

import javax.inject.Inject;

public class StateManager {
    private GameState gameState;
    @Inject
    EventManager _eventManager;

    public StateManager() {
        DaggerContainer.getDaggerBinder().inject(this);
        gameState = GameState.Idle;
        _eventManager.Subscribe(EventType.RestartNeeded,new EventListener(){
            @Override
            public void HandleEvent() {
                gameState = GameState.Idle;
            }
        });
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void ChangeState(GameState state)
    {
        if(state == GameState.Adding && gameState == GameState.Idle)
        {
            return;
        }
        if(state != gameState) {

            gameState = state;
            _eventManager.Dispatch(EventType.GameStateChanged, state);
            if (state == GameState.Adding) {
                SoundManager.getSoundManager().PlaySound(SoundType.AddingFinished);
            }
        }
    }
}
