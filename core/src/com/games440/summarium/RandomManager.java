package com.games440.summarium;

import java.util.Hashtable;
import java.util.Random;

public class RandomManager {
    private Hashtable<Integer,Double[]> _probabilities;
    private Random _rnd;


    public RandomManager()
    {
        _rnd = new Random();
        _rnd.setSeed(System.nanoTime());
        _probabilities = new Hashtable<Integer, Double[]>();
        _probabilities.put(10,new Double[]{17.5d,17.5d,17.5d,17.5d,12.5d,7.5d,5d,2.5d,2.5});
        _probabilities.put(11,new Double[]{12.5d,14d,17.5d,17.5d,12.5d,10d,8.5,5d,2.5});
        _probabilities.put(12,new Double[]{10d,12.5d,20d,20d,12.5d,10d,7.5,5d,2.5});
        _probabilities.put(13,new Double[]{10d,12.5d,20d,17.5d,15d,10d,7.5,5d,2.5});
        _probabilities.put(14,new Double[]{10d,10d,17.5d,20d,17.5d,10d,7.5,5d,2.5});
        _probabilities.put(15,new Double[]{7.5,7.5,15d,15d,20d,15d,10d,5d,5d});
        _probabilities.put(16,new Double[]{5d,7.5,12.5d,15d,15d,17.5d,15d,7.5d,5d});
        _probabilities.put(17,new Double[]{5d,5d,10d,12.5d,15d,17.5d,15d,12.5d,7.5});
        _probabilities.put(18,new Double[]{5d,5d,7.5d,10d,15d,17.5d,15d,15d,10d});
        _probabilities.put(19,new Double[]{5d,5d,5d,7.5d,15d,20d,17.5d,15d,10d});
    }

    public int getRandomNumber(int aim)
    {
        double currProbability = 0;
        double randomNumber = _rnd.nextDouble();

        Double[] probability = _probabilities.get(aim);
        for(int i = 0; i<probability.length;i++)
        {
            currProbability += (probability[i]/100d);
            if(randomNumber<=currProbability)
            {
                return (i+1);
            }
        }
        return -1;
    }
}
