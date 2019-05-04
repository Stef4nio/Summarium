package com.games440.summarium;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import javax.inject.Inject;

public class GameView extends ApplicationAdapter {
	private SpriteBatch batch;
	private Stage gameStage;
	private Image _gameFieldBackground;
	private Image _topBar;
	private Image _aimImage;
	private Particle _cellClearEffect;
	@Inject
	public IGameModelReadonly _gameModel;
	@Inject
	public EventManager _eventManager;
	@Inject
	StateManager _stateManager;
	@Inject
	ModeSelectWindow _modeSelectWindow;
	private GameController _controller;
	private ViewCell[][] _gameFieldView;
	private UIView _uiView;

    public GameView()
    {
        _gameFieldView = new ViewCell[GameConfig.CELLS_IN_VERTICAL][GameConfig.CELLS_IN_HORIZONTAL];
    }


	@Override
	public void create () {
		batch = new SpriteBatch();
		DaggerContainer.getDaggerBinder().inject(this);
		PlayerPreferencesContainer.Initialize(Gdx.app.getPreferences("SummariumPlayerPreferences"));
		_controller = new GameController(PlayerPreferencesContainer.getPlayerPreferences());
		SoundManager.InitSoundManager();
		_cellClearEffect = new Particle();
		_cellClearEffect.load(Gdx.files.internal("ParticleEffects/CellClearEffect/Particle Park Glass.p"),Gdx.files.internal("ParticleEffects/CellClearEffect/"));
		_cellClearEffect.addOnCompleteListener(new OnParticleCompleteCallback() {
			@Override
			public void run() {
				_cellClearEffect.ClearAddedEmitters();
				_stateManager.ChangeState(GameState.Moving);
			}
		});
		_cellClearEffect.addSound(SoundType.CellClearSound);
		_topBar = ImageSourceConfig.getImageSourceConfig().getTopBar();
		_aimImage = ImageSourceConfig.getImageSourceConfig().getAimNumber(_gameModel.GetAim());
		_gameFieldBackground = ImageSourceConfig.getImageSourceConfig().getFieldBackground();
		gameStage = new Stage(new ExtendViewport(GameConfig.MIN_SCREEN_WIDTH, GameConfig.MIN_SCREEN_HEIGHT));
		GameConfig.SCREEN_WIDTH = gameStage.getWidth();
		GameConfig.SCREEN_HEIGHT = gameStage.getHeight();
		gameStage.getViewport().setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		gameStage.getViewport().apply();
		_gameFieldBackground.setPosition((gameStage.getWidth()-_gameFieldBackground.getDrawable().getMinWidth())/2,10);
		GameConfig.TABLE_X_OFFSET = ((gameStage.getWidth() - _gameFieldBackground.getDrawable().getMinWidth()) / 2f) + 40f;
		gameStage.addActor(_gameFieldBackground);
		_topBar.setPosition((gameStage.getWidth()-_topBar.getDrawable().getMinWidth())/2,1550);
		_aimImage.setPosition((gameStage.getWidth()-_aimImage.getDrawable().getMinWidth()*_aimImage.getScaleX())/2,_topBar.getY()+(_topBar.getDrawable().getMinHeight()-_aimImage.getDrawable().getMinHeight()*_aimImage.getScaleY())/2);
		_uiView = new UIView(gameStage);
		//redrawField();
		gameStage.addActor(_topBar);
		gameStage.addActor(_aimImage);
		_eventManager.Subscribe(EventType.ModelChanged,new EventListener(){
			@Override
			public void HandleEvent() {
				redrawField();
			}
		});
		_eventManager.Subscribe(EventType.RestartNeeded,new EventListener(){
			@Override
			public void HandleEvent() {
				if(!PlayerPreferencesContainer.getPlayerPreferences().contains(GameConfig.isFirstLaunchEverKey)||PlayerPreferencesContainer.getPlayerPreferences().getBoolean(GameConfig.isFirstLaunchEverKey)) {
					PlayerPreferencesContainer.getPlayerPreferences().putBoolean(GameConfig.isFirstLaunchEverKey,false);
					_uiView.showTutorial();
				}
			}
		});
		_eventManager.Subscribe(EventType.AimChanged,new EventListener(){
			@Override
			public void HandleEvent(int param) {
				_aimImage.remove();
				_aimImage = ImageSourceConfig.getImageSourceConfig().getAimNumber(_gameModel.GetAim());
				_aimImage.setPosition((gameStage.getWidth()-_aimImage.getDrawable().getMinWidth()*_aimImage.getScaleX())/2,_topBar.getY()+(_topBar.getDrawable().getMinHeight()-_aimImage.getDrawable().getMinHeight()*_aimImage.getScaleY())/2);
				_aimImage.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(_stateManager.getGameState() == GameState.Idle) {
							_modeSelectWindow.Show(gameStage,false);
						}
					}
				});
				gameStage.addActor(_aimImage);
			}
		});
		_eventManager.Subscribe(EventType.ClearCell,new EventListener(){
			@Override
			public void HandleEvent(int param) {
				for(int i = 0;i<_gameFieldView.length;i++)
				{
					for(int j = 0;j<_gameFieldView[i].length;j++)
					{
						if(param == _gameFieldView[i][j].getId())
						{
							ViewCell tempCell = _gameFieldView[i][j];
							if(!tempCell.isPreviouslyCleared()) {
								_cellClearEffect.addEmitterToPoint(tempCell.getCenterX(), tempCell.getCenterY());
							}
							tempCell.Draw(gameStage,j,i,false);
							return;
						}
					}
				}
			}
		});
		_eventManager.Subscribe(EventType.MotionEnded,new EventListener(){
			@Override
			public void HandleEvent() {
				_stateManager.ChangeState(GameState.Adding);
			}
		});
		_eventManager.Subscribe(EventType.GameStateChanged,new GameStateChangeListener(){
			@Override
			public void HandleEvent(GameState gameState) {
				if(gameState == GameState.AnimatingParticles)
				{
					if(!_cellClearEffect.start()) {
						_stateManager.ChangeState(GameState.Moving);
					}
				}
				else if(gameState == GameState.Adding)
				{
					redrawField();
					_stateManager.ChangeState(GameState.Idle);
				}
				else if(gameState == GameState.Moving)
				{
					boolean isAnimating = false;
					for(int i = 0; i<_gameFieldView.length;i++)
					{
						for(int j = 0;j<_gameFieldView[i].length;j++)
						{
							if(_gameFieldView[i][j].hasOffset())
							{
								isAnimating = true;
								break;
							}
						}
						if(isAnimating)
						{
							break;
						}
					}
					if(isAnimating)
					{
						for(int i = 0; i<_gameFieldView.length;i++)
						{
							for(int j = 0;j<_gameFieldView[i].length;j++)
							{
								if(_gameFieldView[i][j].hasOffset()) {
									_gameFieldView[i][j].Animate();
								}
							}
						}
						SoundManager.getSoundManager().PlaySound(SoundType.AnimationEnded);
					}
					else
					{
						_stateManager.ChangeState(GameState.Adding);
					}
				}
			}
		});
		_aimImage.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(_stateManager.getGameState() == GameState.Idle) {
					_modeSelectWindow.Show(gameStage,true);
				}
			}
		});
		Gdx.input.setInputProcessor(gameStage);
		_uiView.SetMenuVisibility(true);
	}



	@Override
	public void render () {
		Gdx.gl.glClearColor(0.17254902f, 0.11372549f, 0.11372549f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStage.draw();
		_cellClearEffect.draw(gameStage.getBatch(),Gdx.graphics.getDeltaTime());
		_uiView.drawEffects(gameStage.getBatch(),Gdx.graphics.getDeltaTime());
		gameStage.act(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		batch.dispose();
		gameStage.dispose();
	}


	public void redrawField()
	{
		for(int i = 0; i< GameConfig.CELLS_IN_VERTICAL;i++)
		{
			for(int j = 0; j < GameConfig.CELLS_IN_HORIZONTAL;j++)
			{
				if(_gameFieldView[i][j] == null) {
					_gameFieldView[i][j] = new ViewCell(_gameModel.GetGameFieldModel()[i][j]);
				}
				if(_gameFieldView[i][j].isChanged()) {
					_gameFieldView[i][j].Draw(gameStage,j,i,true);
				}
				if(_gameModel.isFirstRun())
				{
					_gameFieldView[i][j].FirstTimeAppear(((GameConfig.CELLS_IN_VERTICAL-i)*GameConfig.CELLS_IN_HORIZONTAL+j)/30f,(j==GameConfig.CELLS_IN_HORIZONTAL-1) ,(i==0&&j==GameConfig.CELLS_IN_HORIZONTAL-1),j,i);
				}
			}
		}
	}
}
