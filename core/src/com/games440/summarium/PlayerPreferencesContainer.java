package com.games440.summarium;


import com.badlogic.gdx.Preferences;

public class PlayerPreferencesContainer {
    private static Preferences _playerPrefs;
    private static boolean isInitialized = false;

    public static Preferences getPlayerPreferences()
    {
        if(isInitialized)
        {
            return _playerPrefs;
        }
        return null;
    }

    public static void Initialize(Preferences playerPrefs)
    {
        _playerPrefs = playerPrefs;
        isInitialized = true;
    }
}
