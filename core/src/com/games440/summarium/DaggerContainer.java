package com.games440.summarium;

public class DaggerContainer {
    private static IComponent _daggerBinder;

    static {
        _daggerBinder = DaggerIComponent.builder().build();
    }

    public static IComponent getDaggerBinder() {
        return _daggerBinder;
    }
}
