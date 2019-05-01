package com.games440.summarium;

import java.util.LinkedList;
import java.util.List;

public class GameModel implements IGameModelReadonly {


    private EventManager _eventManager;
    private StateManager _stateManager;
    private RandomManager _randomManager;
    private ModelCell[][] _gameFieldModel = new ModelCell[GameConfig.CELLS_IN_VERTICAL][GameConfig.CELLS_IN_HORIZONTAL];
    private List<ModelCell> _selectedCells;
    private int _aimValue = 10;
    private boolean isWon = false;
    private boolean isFirstRun;

    @Override
    public int GetAim() {
        return _aimValue;
    }

    public void setAim(int newAim)
    {
        if(_aimValue<=GameConfig.MAX_AIM&&_aimValue>=GameConfig.MIN_AIM)
        {
            _aimValue = newAim;
            RestartModel();
        }
    }

    public GameModel(EventManager eventManager,StateManager stateManager,RandomManager randomManager)
    {
        _eventManager = eventManager;
        _stateManager = stateManager;
        _randomManager = randomManager;
        isFirstRun = true;
        _selectedCells = new LinkedList<ModelCell>();
        for(int i = 0; i<GameConfig.CELLS_IN_VERTICAL;i++)
        {
            for(int j = 0; j<GameConfig.CELLS_IN_HORIZONTAL;j++)
            {
                _gameFieldModel[i][j] = ModelCellFactory.getModelCell(_randomManager.getRandomNumber(_aimValue));
            }
        }
    }

    public void HandleInput(int id)
    {
        if(_stateManager.getGameState() == GameState.Idle) {

            ModelCell tempCell = getModelCellById(id);
            if (_selectedCells.isEmpty() || isInTheBounds(tempCell) || _selectedCells.contains(tempCell))
            {
                if (tempCell.GetState() == ViewCellState.Idle) {
                    _selectedCells.add(tempCell);
                    if (getSumOfSelectedCells() > _aimValue) {
                        deselectAllSelectedCells();
                        _selectedCells.add(tempCell);
                    }
                    tempCell.SetState(ViewCellState.Selected);
                } else if (tempCell.GetState() == ViewCellState.Selected) {
                    tempCell.SetState(ViewCellState.Idle);
                    _selectedCells.remove(tempCell);
                    for (int i = 0; i < _selectedCells.size() && _selectedCells.size() > 1; i++) {
                        if (!isInTheBounds(_selectedCells.get(i))) {
                            deselectAllSelectedCells();
                        }
                    }
                }

                if (!_selectedCells.isEmpty()) {
                    if (getSumOfSelectedCells() == _aimValue) {
                        for (ModelCell cell : _selectedCells) {
                            cell.Clear();
                            _eventManager.Dispatch(EventType.ClearCell, cell.GetId());
                        }
                        if(isWon()&&!isWon)
                        {
                            _eventManager.Dispatch(EventType.PlayerWon);
                            isWon = true;
                        }
                        CompressModel();
                        return;
                    }
                }
            }else if(!isInTheBounds(tempCell))
            {
                deselectAllSelectedCells();
                _selectedCells.add(tempCell);
                tempCell.SetState(ViewCellState.Selected);

            }
            _eventManager.Dispatch(EventType.ModelChanged);
        }
    }

    public void clearOffsets()
    {
        for(int i = 0;i<_gameFieldModel.length;i++)
        {
            for(int j = 0;j<_gameFieldModel[i].length;j++)
            {
                _gameFieldModel[i][j].clearOffset();
            }
        }
    }

    @Override
    public boolean isFirstRun()
    {
        return isFirstRun;
    }

    public void RestartModel()
    {
        isFirstRun = true;
        _selectedCells.clear();
        isWon = false;
        for(int i = 0; i<GameConfig.CELLS_IN_VERTICAL;i++)
        {
            for(int j = 0; j<GameConfig.CELLS_IN_HORIZONTAL;j++)
            {
                _gameFieldModel[i][j].Refresh(_randomManager.getRandomNumber(_aimValue));
                if(_gameFieldModel[i][j]._id != 20)
                {
                    _gameFieldModel[i][j].isCleared = true;
                }
            }
        }
        _stateManager.ChangeState(GameState.Idle);
        _eventManager.Dispatch(EventType.ModelChanged);
        isFirstRun = false;
    }

    private void CompressModel()
    {
        ModelCell[] tempColumn = new ModelCell[_gameFieldModel.length];
        for(int j = 0; j<_gameFieldModel[0].length;j++)
        {
            for(int i = 0;i<_gameFieldModel.length;i++)
            {
                tempColumn[i] = _gameFieldModel[i][j];
            }
            tempColumn = ValueCompressor(tempColumn);
            for(int i = 0; i<_gameFieldModel.length;i++)
            {
                _gameFieldModel[i][j]=tempColumn[i];
            }
        }
        deselectAllSelectedCells();
        _stateManager.ChangeState(GameState.AnimatingParticles);
    }

    public void makeAllCellsNotNew()
    {
        for (ModelCell[] a_gameFieldModel : _gameFieldModel) {
            for (int j = 0; j < a_gameFieldModel.length; j++) {
                a_gameFieldModel[j].makeNotNew();
            }
        }
    }

    private ModelCell[] ValueCompressor(ModelCell[] column)
    {
        int offset = 0;
        for (ModelCell aColumn1 : column) {
            if (aColumn1.GetValue() == 0) {
                offset++;
            } else {
                aColumn1.increaseOffsetBy(offset);
            }
        }
        int changes;
        do {
            changes = 0;
            for (int i = 0; i < column.length; i++) {
                if (column[i].GetValue() == 0 && (i + 1) < column.length && column[i+1].GetValue() != 0) {
                    column[i].setValue(column[i + 1].GetValue());
                    column[i + 1].setValue(0);
                    changes++;
                }
            }
        }while (changes!=0);
        for (ModelCell aColumn : column) {
            if (aColumn.GetValue() == 0) {
                aColumn.setNewValue(_randomManager.getRandomNumber(_aimValue));
            }
        }
        return column;
    }

    private int getSumOfSelectedCells()
    {
        int sum = 0;
        for (ModelCell cell : _selectedCells) {
            sum += cell.GetValue();
        }
        return sum;
    }

    private void deselectAllSelectedCells()
    {
        for (ModelCell cell:_selectedCells) {
            cell.SetState(ViewCellState.Idle);
        }
        _selectedCells.clear();
    }

    private boolean isInTheBounds(ModelCell cell)
    {
        List<ModelCell> modelCellsToCheck = new LinkedList<ModelCell>();
        int cellY = getModelCellY(cell);
        int cellX = getModelCellX(cell);
        if(cellY+1<GameConfig.CELLS_IN_VERTICAL) {
            modelCellsToCheck.add(_gameFieldModel[cellY+1][cellX]);
        }
        if(cellY-1>=0) {
            modelCellsToCheck.add(_gameFieldModel[cellY-1][cellX]);
        }
        if(cellX-1>=0)
        {
            modelCellsToCheck.add(_gameFieldModel[cellY][cellX-1]);
        }
        if(cellX+1<GameConfig.CELLS_IN_HORIZONTAL)
        {
            modelCellsToCheck.add(_gameFieldModel[cellY][cellX+1]);
        }
        for(ModelCell tempCell:modelCellsToCheck)
        {
            if(_selectedCells.contains(tempCell))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isWon()
    {
        for (ModelCell[] a_gameFieldModel : _gameFieldModel) {
            for (ModelCell anA_gameFieldModel : a_gameFieldModel) {
                if (!anA_gameFieldModel.isCleared()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getModelCellX(ModelCell cell)
    {
        for (ModelCell[] tempCell : _gameFieldModel) {
            for (int j = 0; j < tempCell.length; j++) {
                if (tempCell[j].equals(cell)) {
                    return j;
                }
            }
        }
        return -1;
    }

    private int getModelCellY(ModelCell cell)
    {
        for(int i = 0;i<_gameFieldModel.length;i++)
        {
            for(int j = 0;j<_gameFieldModel[i].length;j++)
            {
                if(_gameFieldModel[i][j].equals(cell))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    private ModelCell getModelCellById(int id)
    {
        for (ModelCell[] cell : _gameFieldModel) {
            for (ModelCell aCell : cell) {
                if (aCell.GetId() == id) {
                    return aCell;
                }
            }
        }
        return null;
    }

    @Override
    public IModelCellReadonly[][] GetGameFieldModel() {
        return _gameFieldModel;
    }
}
