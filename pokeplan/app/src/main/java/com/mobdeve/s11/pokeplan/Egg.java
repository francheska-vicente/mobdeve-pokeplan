package com.mobdeve.s11.pokeplan;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Egg {
    private Timer timer;

    private ArrayList<String> pool;
    private final String C = "Common";
    private final String UC = "Uncommon";
    private final String R = "Rare";
    private final String SR = "Super Rare";

    // rates of C, UC, R, SR respectively
    private final int[] first = {80, 15, 4, 1};
    private final int[] second = {50, 35, 10, 5};
    private final int[] third = {30, 45, 15, 10};
    private final int[] fourth = {10, 40, 30, 20};
    private final int[] fifth = {5, 30, 40, 25};
    private final int[] sixth = {0, 20, 30, 50};

    public Egg(Timer timer) {
        this.timer = timer;
    }

    public void generatePokemon() {
        // convert hh:mm:ss to minutes
        int total = (int) TimeUnit.HOURS.toMinutes(timer.getHours()) + timer.getMins();
        if (timer.getSecs() > 0)    total++;

        // initialize pool of rarities
        pool = new ArrayList<>(100);

        // rates are based on time
        if (total >= 5 && total < 20) {
            populatePool(first);
        }
        else if (total >= 20 && total < 40) {
            populatePool(second);
        }
        else if (total >= 40 && total < 60) {
            populatePool(third);
        }
        else if (total >= 60 && total < 120) {
            populatePool(fourth);
        }
        else if (total >= 120 && total < 240) {
            populatePool(fifth);
        }
        else {
            populatePool(sixth);
        }

        // get rarity from pull
        String rarity = pool.get((int)(Math.random() * 100 + 1 - 2));

        // get random pokemon of the pulled rarity
        String pokemon = "";
        switch (rarity) {
            case C: pokemon = Pool.generateCommonPokemon(); break;
            case UC: pokemon = Pool.generateUncommonPokemon(); break;
            case R: pokemon = Pool.generateRarePokemon(); break;
            case SR: pokemon = Pool.generateSuperRarePokemon(); break;
        }
    }

    public void populatePool(int[] rates) {
        for(int j=0; j<rates[0]; j++)
            pool.add(C);
        for(int j=0; j<rates[1]; j++)
            pool.add(UC);
        for(int j=0; j<rates[2]; j++)
            pool.add(R);
        for(int j=0; j<rates[3]; j++)
            pool.add(SR);
    }

}