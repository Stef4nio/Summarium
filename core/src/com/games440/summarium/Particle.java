package com.games440.summarium;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class Particle {
    private boolean _isStarted;
    private List<ParticleEffect> _effects;
    private FileHandle _effectFile;
    private FileHandle _imageLocation;
    @Inject
    StateManager _stateManager;

    public Particle() {
        _effects = new LinkedList<ParticleEffect>();
        _isStarted = false;
        DaggerContainer.getDaggerBinder().inject(this);
    }

    public void load(FileHandle effectFile, FileHandle imageLocation)
    {
        _effectFile = effectFile;
        _imageLocation = imageLocation;
    }

    public void start() {
        if(!_effects.isEmpty()) {
            _isStarted = true;
            SoundManager.getSoundManager().PlaySound(SoundType.CellClearSound);
            for (ParticleEffect particle : _effects) {
                particle.setDuration((int) particle.getEmitters().get(0).duration);
                particle.start();
            }
        }
        else {
            _stateManager.ChangeState(GameState.Moving);
        }
    }



    public void draw(Batch spriteBatch, float delta)
    {
        if(_isStarted) {
            spriteBatch.begin();
            if (!_effects.get(0).isComplete()) {
                for (ParticleEffect particle : _effects) {
                    particle.draw(spriteBatch, delta);
                }
            }
            else {
                _isStarted = false;
                _effects.clear();
                _stateManager.ChangeState(GameState.Moving);
            }
            spriteBatch.end();
        }
    }

    public void addEmitterToPoint(float pointX, float pointY)
    {
        ParticleEffect particle = new ParticleEffect();
        particle.load(_effectFile,_imageLocation);
        particle.setPosition(pointX,pointY);
        _effects.add(particle);
    }

}
