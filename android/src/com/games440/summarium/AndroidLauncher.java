package com.games440.summarium;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.games440.summarium.GameView;


public class AndroidLauncher extends AndroidApplication {

	private GameView _gameView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		_gameView = new GameView();
		initialize(_gameView, config);
		//_gameView.create();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
