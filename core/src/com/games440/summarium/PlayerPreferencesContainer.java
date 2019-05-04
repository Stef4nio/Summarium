package com.games440.summarium;


import com.badlogic.gdx.Preferences;

public class PlayerPreferencesContainer {
    private static Preferences _playerPrefs;
    private static boolean isInitialized = false;
    private static boolean isTutorialHasToBeShown = true;

    public static Preferences getPlayerPreferences()
    {
        if(isInitialized)
        {
            return _playerPrefs;
        }
        return null;
    }

    public static boolean isTutorialHasToBeShown()
    {
        return isTutorialHasToBeShown;
    }

    public static void disableTutorial()
    {
        isTutorialHasToBeShown = false;
    }

    public static void Initialize(Preferences playerPrefs)
    {
        _playerPrefs = playerPrefs;
        isInitialized = true;
    }
}
