package com.games440.summarium.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.games440.summarium.GameView;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 540;
		config.height = 680;
		/*config.width = 1080;
		config.height = 1920;*/
		new LwjglApplication(new GameView(), config);
	}
}
