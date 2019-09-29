package com.timgapps.tds_game.media;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Media { // Класс для работы с музыкой
    private AssetManager assetManager;  // объявим переменную класса AssetManager

    public Media(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void playSound(String name) {
        Sound sound = assetManager.get("sounds/" + name, Sound.class);
        sound.play();
    }

    public void playMusic(String name, boolean loop) {
        Music music = assetManager.get("musics/" + name, Music.class);
        music.setLooping(loop);  // метод setLooping устанавливает повтор воспроизведения
        music.play();
    }

    public void stopMusic(String name) {
        Music music = assetManager.get("musics/" + name, Music.class);
        music.stop();
    }

    public void addMusic(String name) { // методом addMusic будем загружать музыкальный файл
        assetManager.load("musics/" + name, Music.class);
    }

    public void removeMusic(String name) { // метод выхывает метод unload удаляет ресурс и все его зависимости,
        // если он не используется
        assetManager.unload("musics/" + name);
    }

    public boolean update() {       // возвращает true, когда assetManager загрузил все ресурсы
        return assetManager.update();
    }
}
