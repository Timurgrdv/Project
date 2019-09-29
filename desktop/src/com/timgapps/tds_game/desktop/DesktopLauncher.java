package com.timgapps.tds_game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.timgapps.tds_game.GameCallback;
import com.timgapps.tds_game.Tds;

public class DesktopLauncher {
	public DesktopLauncher() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Tds.V_WIDTH;
		config.height = Tds.V_HEIGHT;

		new LwjglApplication(new Tds(callback), config);
	}

	private GameCallback callback = new GameCallback() {
		@Override
		public void sendMessage(int message) {
		}
	};


	public static void main(String[] arg) {
		new DesktopLauncher();
	}
}
