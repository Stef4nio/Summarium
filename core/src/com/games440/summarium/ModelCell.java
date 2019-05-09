package com.games440.summarium;

public class ModelCell implements IModelCellReadonly {
    private int _value;
    private int _id;
    private int _offset;
    private ViewCellState _state;
    private boolean isChanged;
    private boolean isCleared;
    private boolean isNew;
    public int selectionValue;

    public ModelCell(int value, int id) {
        _value = value;
        selectionValue = 0;
        _id = id;
        _state = ViewCellState.Idle;
        isChanged = true;
        isCleared = false;
    }

    public boolean isChanged() {
        boolean tempChanged = isChanged;
        isChanged = false;
        return tempChanged;
    }


    public void increaseOffsetBy(int offset)
    {
        _offset+=offset;
        isChanged = true;
    }

    @Override
    public int GetValue() {
        return _value;
    }

    public void SetState(ViewCellState state) {
       _state = state;
       selectionValue = state==ViewCellState.Selected?1:0;
       isChanged = true;
    }

    public void setNewValue(int value) {
        this._value = value;
        isChanged = true;
        isNew = true;
    }

    public void setValue(int value)
    {
        this._value = value;
    }

    public void makeNotNew()
    {
        isNew = false;
    }

    public void Clear()
    {
        isCleared = true;
        isChanged = false;
        _state = ViewCellState.Idle;
        selectionValue = 0;
        _value = 0;
    }

    public void Refresh(int newValue)
    {
        _value = newValue;
        isNew = false;
        _state = ViewCellState.Idle;
        selectionValue = 0;
        isChanged = true;
        isCleared = false;
    }

    public boolean isCleared()
    {
        return isCleared;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void clearOffset()
    {
        _offset = 0;
    }

    @Override
    public int GetOffset() {
        return _offset;
    }

    @Override
    public boolean equals(Object o) {
        return ((ModelCell)o).GetId() == _id;
    }

    @Override
    public int GetId() {
        return _id;
    }

    @Override
    public ViewCellState GetState() {
        return _state;
    }
}
