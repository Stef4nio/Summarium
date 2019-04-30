package com.games440.summarium;

public interface IModelCellReadonly {
    int GetValue();
    ViewCellState GetState();
    int GetId();
    boolean isChanged();
    boolean isCleared();
    boolean isNew();
    int GetOffset();
}
