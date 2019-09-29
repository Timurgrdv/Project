package com.timgapps.tds_game;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class AndroidLauncher extends AndroidApplication {
	private RelativeLayout mainView; // макет экрана с относительной разметокой

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// создаем окно для андроид
		requestWindowFeature(Window.FEATURE_NO_TITLE);  // окно без заголовка
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // получаем окно и устанавливаем флаг "окно на весь экран"
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN); // также удаляем флаг FLAG_FORCE_NOT_FULLSCREEN

		// сооздаем разметку макета окна
		mainView = new RelativeLayout(this);
		setContentView(mainView);

		// создаем окно игры с помощью метоада initializeForView, который устанавливает пользовательский ввод, визуализацию через OpenGL  и
		// другие необходимые вещи
		View gameView = initializeForView(new Tds(gameCallback));  // здесь передаем экземпляр главного класса игры
		mainView.addView(gameView);    // передаем игровое окно главному окну

//        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//        initialize(new Tds(), config);
	}
	// инициализируем интерфейс GameCallBack() для организации взаимодействия
	private GameCallback gameCallback = new GameCallback() {
		@Override
		public void sendMessage(int message) {
		}
	};
}