package com.games440.summarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WinParticleEffect {
    private Particle[] _fireworks;
    private Timer[] _timers;
    private Random _rnd;

    public WinParticleEffect()
    {
        _rnd = new Random();
        _fireworks = new Particle[3];
        _timers = new Timer[_fireworks.length];
        for (int i = 0; i< _fireworks.length; i++) {
            Particle particle = new Particle();
            particle.load(Gdx.files.internal("ParticleEffects/WinEffect/Particle Park Fireworks/Particle Park Fireworks.p"), Gdx.files.internal("ParticleEffects/WinEffect/Particle Park Fireworks"));
            _fireworks[i] = particle;
        }
    }

    public void Draw(Batch batch, float delta)
    {
        for (int i = 0; i<_fireworks.length;i++) {
            _fireworks[i].draw(batch,delta);
        }
    }

    public void stop(){
        for (int i = 0; i< _fireworks.length; i++) {
            _timers[i].cancel();
            _timers[i].purge();
            _fireworks[i].Dispose();
            _fireworks[i].ClearAddedEmitters();
        }
    }

    public void start()
    {

        for(int i = 0; i< _fireworks.length; i++)
        {
            _timers[i] = new Timer();
            float x = GameConfig.SCREEN_WIDTH/2+_rnd.nextInt(800)-400;
            float y = GameConfig.SCREEN_HEIGHT/2+400+_rnd.nextInt(500)-250;
            final int finalI = i;
            _fireworks[finalI].addEmitterToPoint(x,y);
            _timers[i].scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    _fireworks[finalI].start();
                    _fireworks[finalI].setPositionToEmitter(0,GameConfig.SCREEN_WIDTH/2+_rnd.nextInt(800)-400,GameConfig.SCREEN_HEIGHT/2+400+_rnd.nextInt(500)-250);
                }
            },750-_rnd.nextInt(500),3000-_rnd.nextInt(1000));
        }
    }
}
