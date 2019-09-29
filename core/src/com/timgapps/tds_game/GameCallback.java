package com.timgapps.tds_game;

public interface GameCallback { // этот интерфейс необходим для связи данных между модулями нашего приложения
    public void sendMessage(int message);
}
