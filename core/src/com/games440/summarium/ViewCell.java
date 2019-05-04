package com.games440.summarium;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.inject.Inject;

public class ViewCell{
    private Image border;
    private Image aimImage;
    private Image stateImage;
    private boolean isPreviouslyCleared = false;
    private boolean isCurrentlyCleared = false;
    private IModelCellReadonly _modelCell;
    private float _borderWidth;
    private float _borderHeight;
    private float _stateImageWidth;
    private float _stateImageHeight;
    @Inject
    EventManager _eventManager;


    public ViewCell(IModelCellReadonly modelCell) {
        this._modelCell = modelCell;
        ViewCellState state = _modelCell.GetState();
        int value = _modelCell.GetValue();
        DaggerContainer.getDaggerBinder().inject(this);
        aimImage = ImageSourceConfig.getImageSourceConfig().getNumber(value);
        stateImage = ImageSourceConfig.getImageSourceConfig().getCellBackgroundByState(state,_modelCell.isCleared());
        border = ImageSourceConfig.getImageSourceConfig().getCellBorder();
        border.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _eventManager.Dispatch(EventType.Click,_modelCell.GetId());
            }
        });
        _borderWidth = border.getDrawable().getMinWidth()*border.getScaleX();
        _borderHeight = border.getDrawable().getMinHeight()*border.getScaleY();
        _stateImageWidth = stateImage.getDrawable().getMinWidth()*stateImage.getScaleX();
        _stateImageHeight = stateImage.getDrawable().getMinHeight()*stateImage.getScaleY();
    }

    public void Animate()
    {
        SequenceAction sequence = new SequenceAction();
        MoveByAction moveAction = new MoveByAction();
        float yOffset = -_modelCell.GetOffset() * (border.getDrawable().getMinHeight() + GameConfig.CELL_Y_PADDING);
        moveAction.setAmountY(yOffset);
        moveAction.setDuration(0.4f);
        sequence.addAction(moveAction);
        sequence.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                _eventManager.Dispatch(EventType.MotionEnded);
                return true;
            }
        });
        aimImage.toFront();
        aimImage.addAction(sequence);
    }

    public void FirstTimeAppear(float delay,boolean isLastInRow, boolean isLast, int modelX, int modelY)
    {
        SequenceAction sequence = new SequenceAction();
        DelayAction delayAction = new DelayAction(delay);
        sequence.addAction(delayAction);
        aimImage.setScale(0);
        ParallelAction parallelAction = new ParallelAction();
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(getCenterX(),getCenterY());
        moveToAction.setReverse(true);
        moveToAction.setDuration(0.3f);
        ScaleToAction scaleAction = new ScaleToAction();
        scaleAction.setScale(1);
        scaleAction.setDuration(0.3f);
        parallelAction.addAction(moveToAction);
        parallelAction.addAction(scaleAction);
        sequence.addAction(parallelAction);
        if(isLast)
        {
            sequence.addAction(new Action() {
                @Override
                public boolean act(float delta) {
                    SoundManager.getSoundManager().PlaySound(SoundType.LetsPlaySound);
                    return true;
                }
            });
        }
        else if(isLastInRow)
        {
            sequence.addAction(new Action() {
                @Override
                public boolean act(float delta) {
                    SoundManager.getSoundManager().PlaySound(SoundType.RowAppearSound);
                    return true;
                }
            });
        }
        aimImage.addAction(sequence);

        sequence = new SequenceAction();
        delayAction = new DelayAction(delay);
        sequence.addAction(delayAction);
        stateImage.setScale(0);
        parallelAction = new ParallelAction();
        moveToAction = new MoveToAction();
        moveToAction.setPosition(getCenterX(),getCenterY());
        moveToAction.setReverse(true);
        moveToAction.setDuration(0.3f);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1);
        scaleAction.setDuration(0.3f);
        parallelAction.addAction(moveToAction);
        parallelAction.addAction(scaleAction);
        sequence.addAction(parallelAction);
        stateImage.addAction(sequence);

        sequence = new SequenceAction();
        delayAction = new DelayAction(delay);
        sequence.addAction(delayAction);
        border.setScale(0);
        parallelAction = new ParallelAction();
        moveToAction = new MoveToAction();
        moveToAction.setPosition(getCenterX(),getCenterY());
        moveToAction.setReverse(true);
        moveToAction.setDuration(0.3f);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1);
        scaleAction.setDuration(0.3f);
        parallelAction.addAction(moveToAction);
        parallelAction.addAction(scaleAction);
        sequence.addAction(parallelAction);
        border.addAction(sequence);
    }

    public boolean hasOffset()
    {
        return _modelCell.GetOffset()!=0;
    }

    public int getId()
    {
        return _modelCell.GetId();
    }


    public void Draw(Stage gameStage,float modelX, float modelY, boolean drawNumber)
    {
        int value = 0;
        ViewCellState state = _modelCell.GetState();
        if(drawNumber)
        {
            value = _modelCell.GetValue();
        }
        aimImage.remove();
        stateImage.remove();
        border.remove();
        if(drawNumber)
        {
            aimImage = ImageSourceConfig.getImageSourceConfig().getNumber(value);
        }
        isPreviouslyCleared = isCurrentlyCleared;
        isCurrentlyCleared = _modelCell.isCleared();
        stateImage = ImageSourceConfig.getImageSourceConfig().getCellBackgroundByState(state,_modelCell.isCleared());
        border.setPosition(modelX*(_borderWidth+GameConfig.CELL_X_PADDING)+ GameConfig.TABLE_X_OFFSET,
                modelY*(_borderHeight+GameConfig.CELL_Y_PADDING)+GameConfig.TABLE_Y_OFFSET);
        stateImage.setPosition(modelX*(_borderWidth+GameConfig.CELL_X_PADDING)+ GameConfig.TABLE_X_OFFSET +(_borderWidth-_stateImageWidth)/2,
                modelY*(_borderHeight+GameConfig.CELL_Y_PADDING)+GameConfig.TABLE_Y_OFFSET+(_borderHeight-_stateImageHeight)/2);
        aimImage.setPosition(stateImage.getX(),stateImage.getY());
        gameStage.addActor(stateImage);
        if(drawNumber)
        {
            gameStage.addActor(aimImage);
            if(_modelCell.isNew())
            {
                Color tempColor = aimImage.getColor();
                tempColor.a = 0;
                aimImage.setColor(tempColor);
                aimImage.addAction(Actions.fadeIn(0.5f));
            }
        }
        gameStage.addActor(border);
    }

    public boolean isChanged()
    {
        return _modelCell.isChanged();
    }

    public float getCenterX()
    {
        //return (modelX*(_borderWidth+GameConfig.CELL_X_PADDING)+GameConfig.TABLE_X_OFFSET+_borderWidth/2f);
        return stateImage.getX()+_stateImageWidth/2;
    }

    public boolean isPreviouslyCleared()
    {
        return isPreviouslyCleared;
    }

    public float getCenterY()
    {
        //return (modelY*(_borderHeight*border.getScaleY()+GameConfig.CELL_Y_PADDING)+GameConfig.TABLE_Y_OFFSET+_borderHeight/2f);
        return stateImage.getY()+_stateImageHeight/2;
    }
}
