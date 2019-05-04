package com.games440.summarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.Hashtable;

public class SoundManager {
    private Sound _cellClickSound;
    private Sound _animatingFinishedSound;
    private Sound _addingFinishedSound;
    private Sound _uiClickSound;
    private Sound _playButtonClickSound;
    private Sound _winSound;
    private Sound _cellClearSound;
    private Sound _rowAppearSound;
    private Music _backgroundMusic;
    private Hashtable<SoundType,Sound> _sounds = new Hashtable<SoundType, Sound>();
    private Sound[] _fireworksSounds;
    private static SoundManager _soundManager = new SoundManager();

    private SoundManager()
    {
        _cellClickSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Cell_click.wav"));
        _animatingFinishedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/cells_tween_down.wav"));
        _addingFinishedSound = Gdx.audio.newSound(Gdx.files.internal("Audio/new_cells_Appear_stereo.wav"));
        _uiClickSound = Gdx.audio.newSound(Gdx.files.internal("Audio/UI_BUTTON_Click.wav"));
        _playButtonClickSound = Gdx.audio.newSound(Gdx.files.internal("Audio/VOICE_Girl_4yo_Lets_Play_03.wav"));
        _backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Audio/loops/Laid Back LOOP.wav"));
        _winSound = Gdx.audio.newSound(Gdx.files.internal("Audio/VOICE_Girl_4yo_You_Win_01.wav"));
        _cellClearSound = Gdx.audio.newSound(Gdx.files.internal("Audio/breaking-glass.wav"));
        _rowAppearSound = Gdx.audio.newSound(Gdx.files.internal("Audio/row_appear.wav"));
        _fireworksSounds = new Sound[4];
        for(int i = 0;i<_fireworksSounds.length;i++)
        {
            _fireworksSounds[i] = Gdx.audio.newSound(Gdx.files.internal("Audio/FIREWORKS_Rocket_Explode_RR"+(i+1)+"_mono.wav"));
        }
        _backgroundMusic.setLooping(true);
        _backgroundMusic.setVolume(0.3f);
        _backgroundMusic.play();
        _sounds.put(SoundType.Click,_cellClickSound);
        _sounds.put(SoundType.AnimationEnded, _animatingFinishedSound);
        _sounds.put(SoundType.AddingFinished, _addingFinishedSound);
        _sounds.put(SoundType.UIClicked,_uiClickSound);
        _sounds.put(SoundType.LetsPlaySound,_playButtonClickSound);
        _sounds.put(SoundType.WinSound,_winSound);
        _sounds.put(SoundType.CellClearSound,_cellClearSound);
        _sounds.put(SoundType.RowAppearSound,_rowAppearSound);
    }

    public void PlaySound(SoundType type)
    {
        _sounds.get(type).play(0.5f);
    }

    public void PlayFireworkSound(int fireworkType)
    {
        _fireworksSounds[fireworkType].play(0.5f);
    }

    public static SoundManager getSoundManager()
    {
        return _soundManager;
    }


    public static void InitSoundManager()
    {
        _soundManager = new SoundManager();
    }

}
