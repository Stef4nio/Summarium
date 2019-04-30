package com.games440.summarium;

public interface IGameModelReadonly {

    IModelCellReadonly[][] GetGameFieldModel();
    int GetAim();
    boolean isFirstRun();
}