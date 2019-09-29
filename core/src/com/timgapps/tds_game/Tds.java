package com.timgapps.tds_game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.I18NBundle;
import com.boontaran.games.StageGame;
import com.timgapps.tds_game.levels.Level;
import com.timgapps.tds_game.levels.Scene;
import com.timgapps.tds_game.media.Media;
import com.timgapps.tds_game.utils.Data;

public class Tds extends Game {
    public static final int V_WIDTH = 1280;      // 800        //1280
    public static final int V_HEIGHT = 720;     // 480        //720
    public static SpriteBatch batch;
    private boolean loadingAssets = false; // будем присваивать true в процессе загрузки ресурсов

    private AssetManager assetManager;
    public static TextureAtlas atlas; // через переменную класса TextureAtlas мы будем работать с атласом текстур
    //    public static TextureAtlas catatlas;
    private I18NBundle bundle;   // для выбора ресурсов в зависимости от локализации использеются класс I18NBundle
    private String path_to_atlas; // в зависимости от локали в переменную path_to_atlas будет возвращаться путь к нужному нам атласу
    private GameCallback gameCallback;
    public static Media media;  // инициализируем объект класса Media, который загружает звуки
    public static Data data;
    private Level level;
    private Scene scene;

    //Box2D collision Bits
    public static final short NOTHING_BIT = 0;
    public static final short BOX_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short ENEMY_BULLET_BIT = 8;
    public static final short PLAYER_BULLET_BIT = 16;
    public static final short BULLET_BIT = 32;

    public Tds(GameCallback gameCallback) {
        this.gameCallback = gameCallback;
    }
    @Override
    public void create() {
        StageGame.setAppSize(V_WIDTH, V_HEIGHT);        //Задаём размер экрана методом setAppSize класса StageGame()
        batch = new SpriteBatch();
        Gdx.input.setCatchBackKey(true);    // метод setCatchBackKey определяет перехватывать ли кнопку <-Back на устройстве

        path_to_atlas = "images/pack.atlas";
        loadingAssets = true; // присваиваем переменной значение true;
        assetManager = new AssetManager();  //Создаем объект класса AssetManager
        assetManager.load(path_to_atlas, TextureAtlas.class);  // методом load() выполняем сначала загрузку атласа(путь к атласу, и передаем класс TextureAtlas.class)


//        assetManager.load("musics/music1.ogg", Music.class);            // загружаем файлы музыки методом load()
//        assetManager.load("sounds/fail.ogg", Sound.class);
//        assetManager.load("sounds/click.ogg", Sound.class);
//        assetManager.load("sounds/hitEnemy.ogg", Sound.class);
//        assetManager.load("sounds/coin.ogg", Sound.class);
//        assetManager.load("sounds/tintong.ogg", Sound.class);
//        assetManager.load("sounds/throw.ogg", Sound.class);

        //assetManager.finishLoading();
        media = new Media(assetManager);
        data = new Data();
    }

    @Override
    public void render() {
        // этот метод render() выполняется каждый раз, когда должна быть выполнена визуализиция
        // обновление логики игры обычно пишут в этом методе

        // проверяем загружены ли ресурсы
        if (loadingAssets) {
            if (assetManager.update()) {
                loadingAssets = false;
                onAssetsLoaded();
            }
        }
        super.render();
    }


    @Override
    public void dispose() {
        // этот метод вызывается перед остановкой игры
        // в нем неоходимо освобождать занимаемую ресурсами память
        assetManager.dispose();
        super.dispose();
    }

    private void onAssetsLoaded() {  // получаем загруженный атлас текстур и шрифт
        atlas = assetManager.get(path_to_atlas, TextureAtlas.class);

        showLevel();
    }

    private void exitApp() {
        Gdx.app.exit(); // Выход из приложения
    }

    private void showLevel() {
        level = new Level("level1");
        scene = new Scene();
        setScreen(scene);   // активируем игровой экран
//        setScreen(level);   // активируем игровой экран

    }

//    private void showMenu() {
//        menu = new MenuScene(); // создаем новый экран
//        setScreen(menu);       // активируем его методом setScreen
//
//        menu.setCallback(new StageGame.Callback() {
//            @Override
//            public void call(int code) {
//                if (code == MenuScene.ON_PLAY) {
//                    showStore(); // метод отображения экрана магизина
//                    hideMenu();
//                } else if (code == menu.ON_BACK) {
//                    exitApp(); // при нажатии кнопки BACK или ESC на экране intro выходим из приложения
//                }
//            }
//        });
//    }
//
//    private void hideMenu() {
//        menu = null;
//    }

}

