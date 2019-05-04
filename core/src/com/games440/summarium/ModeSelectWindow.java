package com.games440.summarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.inject.Inject;

public class ModeSelectWindow {
    private UIDialog _modeSelectDialog;
    private ImageButton _leftArrowButton;
    private ImageButton _rightArrowButton;
    private ImageButton _modeSelectConfirmButton;
    private Image _modeSelectBackgroundImage;
    private Stack _modeStack;
    private int _tempAim;
    private int _currAim;
    @Inject
    EventManager _eventManager;
    @Inject
    GameModel _gameModel;

    public ModeSelectWindow() {
        DaggerContainer.getDaggerBinder().inject(this);
        Skin _uiSkin = new Skin(Gdx.files.internal("UISkin.json"));
        _modeStack = new Stack();
        _currAim = _gameModel.GetAim();
        _tempAim = _currAim;
        _leftArrowButton = new ImageButton(_uiSkin, "button_left_arrow");
        _rightArrowButton = new ImageButton(_uiSkin, "button_right_arrow");
        _modeSelectConfirmButton = new ImageButton(_uiSkin, "button_ok");
        _modeSelectBackgroundImage = new Image(new Texture("circle.png"));
        _modeSelectDialog = new UIDialog("", _uiSkin, "window_mode");
        _modeSelectDialog.setPosition((GameConfig.SCREEN_WIDTH - _modeSelectDialog.getBackground().getMinWidth()) / 2, (GameConfig.SCREEN_HEIGHT - _modeSelectDialog.getBackground().getMinHeight()) / 2);
        Table modeChooseDialogLayout = new Table();
        updateModeSelectWindow();
        modeChooseDialogLayout.add(_leftArrowButton);
        modeChooseDialogLayout.add(_modeStack).padLeft(50f).padRight(50f);
        modeChooseDialogLayout.add(_rightArrowButton);
        modeChooseDialogLayout.row();
        modeChooseDialogLayout.add(_modeSelectConfirmButton).colspan(3).center().padTop(50);
        modeChooseDialogLayout.setFillParent(true);
        modeChooseDialogLayout.padTop(80);
        _modeSelectDialog.addActor(modeChooseDialogLayout);
        _modeSelectConfirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _modeSelectDialog.hide(null);
                if(_tempAim!=_currAim) {
                    _currAim = _tempAim;
                    _eventManager.Dispatch(EventType.AimChanged, _currAim);
                }else if(_gameModel.isFirstRun())
                {
                    _eventManager.Dispatch(EventType.RestartNeeded);
                }
                playUIClickSound();
            }
        });
        _leftArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _tempAim-= _tempAim-1>=GameConfig.MIN_AIM?1:0;
                updateModeSelectWindow();
                playUIClickSound();
            }
        });
        _rightArrowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _tempAim+= _tempAim+1<=GameConfig.MAX_AIM?1:0;
                updateModeSelectWindow();
                playUIClickSound();
            }
        });
    }

    private void playUIClickSound()
    {
        SoundManager.getSoundManager().PlaySound(SoundType.UIClicked);
    }

    private void updateModeSelectWindow()
    {
        Image aimImage = new Image(new Texture("Aims/"+Integer.toString(_tempAim)+".png"));
        Table aimImageOverlay = new Table();
        aimImageOverlay.add(aimImage);
        aimImageOverlay.setTransform(true);
        Table backgroundImageOverlay = new Table();
        backgroundImageOverlay.add(_modeSelectBackgroundImage).expand().center();
        _modeStack.add(backgroundImageOverlay);
        _modeStack.add(aimImageOverlay);

    }

    public void Show(Stage stage, boolean doAnimate)
    {
        _modeSelectDialog.setPosition((stage.getWidth() - _modeSelectDialog.getBackground().getMinWidth()) / 2, (stage.getHeight() - _modeSelectDialog.getBackground().getMinHeight()) / 2);
        if(doAnimate)
        {
            Color color = _modeSelectDialog.getColor();
            color.a = 0;
            _modeSelectDialog.setColor(color);
            _modeSelectDialog.show(stage);
        }
        else
        {
            _modeSelectDialog.show(stage,null);
        }
    }

    public boolean isVisible()
    {
        return _modeSelectDialog.isVisible();
    }

    public void Hide()
    {
        _modeSelectDialog.hide();
    }

    public void Hide(boolean doAnimate)
    {
        if(doAnimate)
        {
            _modeSelectDialog.hide();
        }
        else
        {
            _modeSelectDialog.hide(null);
        }
    }

}
