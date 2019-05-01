package com.games440.summarium;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import java.util.LinkedList;
import java.util.List;

public class Particle {
    private boolean _isStarted;
    private List<ParticleEffect> _effects;
    private FileHandle _effectFile;
    private FileHandle _imageLocation;
    private OnParticleCompleteCallback _callback;
    private SoundType _soundType;

    public Particle() {
        _effects = new LinkedList<ParticleEffect>();
        _isStarted = false;
    }

    public void load(FileHandle effectFile, FileHandle imageLocation)
    {
        _effectFile = effectFile;
        _imageLocation = imageLocation;
    }

    public void Dispose()
    {
        for (ParticleEffect effect:_effects)
        {
            effect.dispose();
        }
    }

    public boolean start() {
        if(!_effects.isEmpty()) {
            _isStarted = true;
            if(_soundType!=null) {
                SoundManager.getSoundManager().PlaySound(SoundType.CellClearSound);
            }
            for (ParticleEffect particle : _effects) {
                particle.setDuration((int) particle.getEmitters().get(0).duration);
                particle.start();
            }
            return true;
        }
        else {
            return false;
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
                if(_callback!=null) {
                    _callback.run();
                }
            }
            spriteBatch.end();
        }
    }

    public void addSound(SoundType soundType)
    {
        _soundType = soundType;
    }

    public void addOnCompleteListener(OnParticleCompleteCallback callback)
    {
        _callback = callback;
    }

    public void addEmitterToPoint(float pointX, float pointY)
    {
        ParticleEffect particle = new ParticleEffect();
        particle.load(_effectFile,_imageLocation);
        particle.setPosition(pointX,pointY);
        _effects.add(particle);
    }

}
