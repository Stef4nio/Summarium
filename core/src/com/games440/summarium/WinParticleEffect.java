package com.games440.summarium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;

public class WinParticleEffect {
    private Particle _firework;
    private Random _rnd;

    public WinParticleEffect()
    {
        _rnd = new Random();
        _firework = new Particle();
        _firework.load(Gdx.files.internal("ParticleEffects/WinEffect/Particle Park Fireworks/Particle Park Fireworks.p"),Gdx.files.internal("ParticleEffects/WinEffect/Particle Park Fireworks"));
        _firework.Dispose();
    }

    public void Draw(Batch batch, float delta)
    {
        _firework.draw(batch,delta);
    }



    public void start()
    {
        int amount = 4 + _rnd.nextInt(3);
        for(int i = 0; i<amount;i++)
        {
            int dx = _rnd.nextInt(800)-400;
            int dy = _rnd.nextInt(500)-250;
            _firework.addEmitterToPoint(GameConfig.SCREEN_WIDTH/2+dx,GameConfig.SCREEN_HEIGHT/2+dy+400);
        }
        _firework.addOnCompleteListener(new OnParticleCompleteCallback() {
            @Override
            public void run() {
                int amount = 4 + _rnd.nextInt(3);
                for(int i = 0; i<amount;i++)
                {
                    int dx = _rnd.nextInt(800)-400;
                    int dy = _rnd.nextInt(100)-50;
                    _firework.addEmitterToPoint(GameConfig.SCREEN_WIDTH/2+dx,GameConfig.SCREEN_HEIGHT/2+dy+400);
                }
                _firework.start();
            }
        });
        _firework.start();
    }
}
