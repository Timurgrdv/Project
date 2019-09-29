package com.timgapps.tds_game.utils;

import com.boontaran.DataManager;

public class Data {

    private DataManager manager;

    private static final String PREFERENCE_NAME = "crazyCat_data"; // создаем константу с именем файла, в котором будем хранить прогресс
    private static final String PROGRESS_KEY = "crazyCat_data"; // создаем константу с именем ключа, в котором будет храниться значение прогресса
    private static final String SCORE_KEY = "crazyCat_score";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String COINS_COUNT_KEY = "crazyCat_coins_count";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String ARMOR_LEVEL_KEY = "crazyCat_armor_level";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String DAMAGE_LEVEL_KEY = "crazyCat_damage_level";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String ARTFIRE_COUNT_KEY = "crazyCat_artfire_count";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String CANNONBALL_COUNT_KEY = "crazyCat_cannonball_count";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String STONES_COUNT_KEY = "crazyCat_stones_count";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String FIRESTONES_COUNT_KEY = "crazyCat_firestones_count";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String BOOSTER_NUM = "crazyCat_booster_num";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String FIREBOOSTER = "crazyCat_fireBooster";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег
    private static final String HEALTHBOOSTER = "crazyCat_healthBooster";     // создаем константу с именеи ключа, в котором будет храниться значение количества денег

    private static final String IS_SOUND_ON = "isSoundOn";           //

    private static final String ROVER_NUM_KEY = "moon_rover_rover_num"; // создаем константу с именем ключа, в котором будет храниться номер ровера

    public Data() { // конструктор, который инициализ. DataManager  для создания файла хранения
        manager = new DataManager(PREFERENCE_NAME);
    }
}