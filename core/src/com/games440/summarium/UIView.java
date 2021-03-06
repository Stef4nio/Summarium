package com.games440.summarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.inject.Inject;

public class UIView {
    private Skin _uiSkin;
    private UIDialog _menuDialog;
    private UIDialog _helpDialog;
    private UIDialog _confirmationPopup;
    private UIDialog _winDialog;
    private UIDialog _tutorialDialog;
    private UIDialog _negativeTutorialDialog;
    private ImageButton _yesConfirmationPopupButton;
    private ImageButton _noConfirmationPopupButton;
    private ImageButton _yesWinDialogButton;
    private ImageButton _noWinDialogButton;
    private ImageButton _playButton;
    private ImageButton _restartButton;
    private ImageButton _modeButton;
    private ImageButton _quitButton;
    private ImageButton _menuButton;
    private ImageButton _helpButton;
    private WinParticleEffect _winEffect;
    private Stage _gameStage;
    private boolean isWon = false;
    @Inject
    ModeSelectWindow _modeSelectWindow;
    @Inject
    public EventManager _eventManager;
    @Inject
    public StateManager _stateManager;
    @Inject
    public IGameModelReadonly _gameModel;


    public UIView(Stage gameStage)
    {
        _gameStage = gameStage;
        _winEffect = new WinParticleEffect();
        _uiSkin = new Skin(Gdx.files.internal("UISkin.json"));
        _menuDialog = new UIDialog("", _uiSkin,"window_menu");
        _helpDialog = new UIDialog("",_uiSkin,"window_help");
        _confirmationPopup = new UIDialog("",_uiSkin,"window_confirmationPopup");
        _winDialog = new UIDialog("",_uiSkin,"window_winDialog");
        _tutorialDialog = new UIDialog("",_uiSkin,"window_tutorial");
        _negativeTutorialDialog = new UIDialog("",_uiSkin,"window_tutorialNegative");
        _playButton = new ImageButton(_uiSkin,"button_play");
        _modeButton = new ImageButton(_uiSkin,"button_mode");
        _restartButton = new ImageButton(_uiSkin,"button_restart");
        _quitButton = new ImageButton(_uiSkin,"button_exit");
        _menuButton = new ImageButton(_uiSkin,"button_menu");
        _helpButton = new ImageButton(_uiSkin,"button_help");
        _yesConfirmationPopupButton = new ImageButton(_uiSkin,"button_yes");
        _noConfirmationPopupButton = new ImageButton(_uiSkin,"button_no");
        _yesWinDialogButton = new ImageButton(_uiSkin,"button_yes");
        _noWinDialogButton = new ImageButton(_uiSkin,"button_no");
        DaggerContainer.getDaggerBinder().inject(this);
        _confirmationPopup.setPosition((GameConfig.SCREEN_WIDTH-_confirmationPopup.getBackground().getMinWidth())/2,(GameConfig.SCREEN_HEIGHT-_confirmationPopup.getBackground().getMinHeight())/2);
        _playButton.padBottom(50f);
        _modeButton.padBottom(50);
        _restartButton.padBottom(50f);
        _menuDialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!_gameModel.isShowTutorial())
                {
                    _menuDialog.hide();
                }
            }
        });
        _tutorialDialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _tutorialDialog.hide();
                startGame();
            }
        });
        _negativeTutorialDialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _negativeTutorialDialog.hide();
                startGame();
            }
        });
        _playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(_gameModel.isShowTutorial()) {
                    _menuDialog.hide(null);
                }else
                {
                    _menuDialog.hide();
                }
                if(_gameModel.isShowTutorial())
                {
                    startGame();
                }
            }
        });
        _restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _menuDialog.hide(null);
                if(_gameModel.isShowTutorial())
                {
                    startGame();
                }
                else {
                    _confirmationPopup.show(_gameStage, null);
                }
                playUIClickSound();
            }
        });
        _yesConfirmationPopupButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _confirmationPopup.hide(null);
                _eventManager.Dispatch(EventType.RestartNeeded);
            }
        });
        _noConfirmationPopupButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _confirmationPopup.hide(null);
                _menuDialog.show(_gameStage,null);
                playUIClickSound();
            }
        });
        _yesWinDialogButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _winDialog.hide(null);
                _winEffect.stop();
                _eventManager.Dispatch(EventType.RestartNeeded);
            }
        });
        _noWinDialogButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _winDialog.hide(null);
                _winEffect.stop();
                playUIClickSound();
            }
        });
        _modeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _menuDialog.hide(null);
                _modeSelectWindow.Show(_gameStage,false);
                playUIClickSound();
            }
        });

        _quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
                Gdx.app.exit();
            }
        });
        Table menuDialogLayout = new Table();
        menuDialogLayout.add(_playButton);
        menuDialogLayout.row();
        menuDialogLayout.add(_modeButton);
        menuDialogLayout.row();
        menuDialogLayout.add(_restartButton);
        menuDialogLayout.row();
        menuDialogLayout.add(_quitButton);
        menuDialogLayout.setFillParent(true);
        _menuDialog.addActor(menuDialogLayout);
        _confirmationPopup.setPosition((GameConfig.SCREEN_WIDTH - _confirmationPopup.getBackground().getMinWidth()) / 2, (GameConfig.SCREEN_HEIGHT - _confirmationPopup.getBackground().getMinHeight()) / 2);
        Table confirmationPopupLayout = new Table();
        confirmationPopupLayout.row().padTop(300f);
        confirmationPopupLayout.add(_yesConfirmationPopupButton).padRight(50f);
        confirmationPopupLayout.add(_noConfirmationPopupButton).padLeft(50f);
        confirmationPopupLayout.setFillParent(true);
        _confirmationPopup.addActor(confirmationPopupLayout);
        Table winDialogLayout = new Table();
        winDialogLayout.row().padTop(300f);
        winDialogLayout.add(_yesWinDialogButton).padRight(50f);
        winDialogLayout.add(_noWinDialogButton).padLeft(50f);
        winDialogLayout.setFillParent(true);
        _winDialog.addActor(winDialogLayout);
        _helpDialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _helpDialog.hide();
            }
        });
        float scale = (gameStage.getWidth()-30)/_helpDialog.getBackground().getMinWidth();
        _helpDialog.getBackground().setMinHeight(scale*_helpDialog.getBackground().getMinHeight());
        _helpDialog.getBackground().setMinWidth(scale*_helpDialog.getBackground().getMinWidth());
        _tutorialDialog.setPosition((GameConfig.SCREEN_WIDTH - _tutorialDialog.getBackground().getMinWidth()) / 2, (GameConfig.SCREEN_HEIGHT - _tutorialDialog.getBackground().getMinHeight()) / 2);
        _negativeTutorialDialog.setPosition((GameConfig.SCREEN_WIDTH - _negativeTutorialDialog.getBackground().getMinWidth()) / 2, (GameConfig.SCREEN_HEIGHT - _negativeTutorialDialog.getBackground().getMinHeight()) / 2);
        InitUIView();
    }


    private void playUIClickSound()
    {
        SoundManager.getSoundManager().PlaySound(SoundType.UIClicked);
    }

    public void SetMenuVisibility(boolean visibility)
    {
        if(visibility) {
            Color color = _menuDialog.getColor();
            color.a = 0;
            _menuDialog.setColor(color);
            _menuDialog.show(_gameStage);
        }else
        {
            _menuDialog.hide();
        }

    }

    public void handleBackButtonClick()
    {
        if(!_gameModel.isShowTutorial()) {

            if (_helpDialog.isVisible()) {

                _helpDialog.hide();
                return;
            }
            if (_modeSelectWindow.isVisible()) {
                _modeSelectWindow.Hide(false);
                return;
            }
            if(_confirmationPopup.isVisible())
            {
                _confirmationPopup.hide(null);
                _menuDialog.show(_gameStage,null);
                return;
            }
            if (_winDialog.isVisible())
            {
                _winEffect.stop();
                _winDialog.hide();
                return;
            }

            if (_menuDialog.isVisible()) {
                _menuDialog.hide();
            }else
            {
                _menuDialog.show(_gameStage);
            }
        }
    }

    private void startGame()
    {
        _eventManager.Dispatch(EventType.RestartNeeded);
    }

    public void showTutorial()
    {
        if(_gameModel.getGameMode() == GameMode.PlusGameMode) {
            _tutorialDialog.show(_gameStage, null);
        }else
        {
            _negativeTutorialDialog.show(_gameStage,null);
        }
    }

    private void InitUIView()
    {
        float stageWidth = _gameStage.getWidth();
        float stageHeight = _gameStage.getHeight();
        _menuButton.setPosition(5,stageHeight-_menuButton.getHeight()-5);
        _menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(_stateManager.getGameState() == GameState.Idle) {
                    Color color = _menuDialog.getColor();
                    color.a = 0;
                    _menuDialog.setColor(color);
                    _menuDialog.show(_gameStage);
                }
                playUIClickSound();
            }
        });
        _helpButton.setPosition(stageWidth-_helpButton.getWidth()-5,stageHeight-_helpButton.getHeight()-5);
        _helpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(_stateManager.getGameState() == GameState.Idle) {
                    Color color = _helpDialog.getColor();
                    color.a = 0;
                    _helpDialog.setColor(color);
                    _helpDialog.show(_gameStage);
                }
                playUIClickSound();
            }
        });
        _eventManager.Subscribe(EventType.PlayerWon,new EventListener(){
            @Override
            public void HandleEvent() {
                isWon = true;
            }
        });
        _eventManager.Subscribe(EventType.GameStateChanged, new GameStateChangeListener(){
            @Override
            public void HandleEvent(GameState gameState) {
                if(gameState == GameState.Idle && isWon)
                {
                    _winEffect.start();
                    SoundManager.getSoundManager().PlaySound(SoundType.WinSound);
                    _winDialog.toFront();
                    Color color = _winDialog.getColor();
                    color.a = 0;
                    _winDialog.setColor(color);
                    _winDialog.show(_gameStage);
                    isWon = false;
                }
            }
        });
        _gameStage.addActor(_helpButton);
        _gameStage.addActor(_menuButton);
    }

    public void drawEffects(Batch batch, float delta)
    {
        _winEffect.Draw(batch,delta);
    }
}
