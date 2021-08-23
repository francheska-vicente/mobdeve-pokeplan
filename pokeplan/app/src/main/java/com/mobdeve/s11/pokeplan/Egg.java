package com.mobdeve.s11.pokeplan;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Egg {
    private Timer timer;

    private ArrayList<String> raritypool;
    private final String C = "Common";
    private final String UC = "Uncommon";
    private final String R = "Rare";
    private final String SR = "Super Rare";

    // rates of C, UC, R, SR respectively
    private final int[] first = {50, 30, 15, 5};
    private final int[] second = {30, 40, 20, 10};
    private final int[] third = {10, 50, 25, 15};
    private final int[] fourth = {5, 35, 40, 20};
    private final int[] fifth = {0, 30, 45, 25};
    private final int[] sixth = {0, 10, 50, 40};

    private Pool pkmnpool;

    public Egg(Timer timer) {
        this.timer = timer;
        this.pkmnpool = new Pool();
        this.raritypool = new ArrayList<>(100);
    }

    public Pokemon generatePokemon() {
        // convert hh:mm:ss to minutes
        int minutes = (int) TimeUnit.HOURS.toMinutes(timer.getHours()) + timer.getMins();
        if (timer.getSecs() > 0)    minutes++;

        // fix rates based on time
        fixRates(minutes);

        // get rarity from pool
        String rarity = raritypool.get(new Random().nextInt(100));

        // get random pokemon of the pulled rarity
        Pokemon pokemon;
        switch (rarity) {
            case SR: pokemon = pkmnpool.generateSuperRarePokemon(); break;
            case R: pokemon = pkmnpool.generateRarePokemon(); break;
            case UC: pokemon = pkmnpool.generateUncommonPokemon(); break;
            case C: default: pokemon = pkmnpool.generateCommonPokemon();
        }

        return pokemon;
    }

    public void populateRarityPool(int[] rates) {
        for(int j=0; j<rates[0]; j++)
            raritypool.add(C);
        for(int j=0; j<rates[1]; j++)
            raritypool.add(UC);
        for(int j=0; j<rates[2]; j++)
            raritypool.add(R);
        for(int j=0; j<rates[3]; j++)
            raritypool.add(SR);
    }

    public void fixRates(int minutes) {
        if (minutes >= 5 && minutes < 20) {
            populateRarityPool(first);
        }
        else if (minutes >= 20 && minutes < 40) {
            populateRarityPool(second);
        }
        else if (minutes >= 40 && minutes < 60) {
            populateRarityPool(third);
        }
        else if (minutes >= 60 && minutes < 120) {
            populateRarityPool(fourth);
        }
        else if (minutes >= 120 && minutes < 240) {
            populateRarityPool(fifth);
        }
        else {
            populateRarityPool(sixth);
        }
    }
}