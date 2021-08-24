package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Egg {
    private final Timer timer;

    private final ArrayList<String> raritypool;
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

    public Egg(Timer timer) {
        this.timer = timer;
        this.raritypool = new ArrayList<>(100);
    }

    public Pokemon generatePokemon() {
        // convert hh:mm:ss to minutes
        int minutes = (int) TimeUnit.HOURS.toMinutes(timer.getHours()) + timer.getMins();
        if (timer.getSecs() > 0)    minutes++;

        // fix rates based on time
        setRates(minutes);

        // get rarity from pool
        String rarity = raritypool.get(new Random().nextInt(100));

        // get random pokemon of the pulled rarity
        Pokemon pokemon;
        switch (rarity) {
            case SR: pokemon = generateSuperRarePokemon(); break;
            case R: pokemon = generateRarePokemon(); break;
            case UC: pokemon = generateUncommonPokemon(); break;
            case C: default: pokemon = generateCommonPokemon();
        }

        return pokemon;
    }

    /**
     * Fills the Rarity Pool ArrayList with rarities based on the given rates.
     * @param rates     percentages of the drop chance for Common, Uncommon, Rare, and
     *                  Super Rare Pokemon respectively
     */
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

    /**
     * Sets the rates for rarities based on the number of minutes set in the Focus Timer.
     * @param minutes   number of minutes set in the Focus Timer
     */
    public void setRates(int minutes) {
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

    /**
     * Randomly generates a Common Pokemon.
     * @return  the common Pokemon generated randomly
     */
    public Pokemon generateCommonPokemon() {
        ArrayList<Pokemon> common = Pokedex.getPokedex().getCommonPokemonList();
        return common.get(new Random().nextInt(common.size()  + 1));
    }

    /**
     * Randomly generates a Uncommon Pokemon.
     * @return  the uncommon Pokemon generated randomly
     */
    public Pokemon generateUncommonPokemon() {
        ArrayList<Pokemon> uncommon = Pokedex.getPokedex().getUncommonPokemonList();
        return uncommon.get(new Random().nextInt(uncommon.size()  + 1));
    }

    /**
     * Randomly generates a Rare Pokemon.
     * @return  the rare Pokemon generated randomly
     */
    public Pokemon generateRarePokemon() {
        ArrayList<Pokemon> rare = Pokedex.getPokedex().getRarePokemonList();
        return rare.get(new Random().nextInt(rare.size() + 1));
    }

    /**
     * Randomly generates a Super Rare Pokemon.
     * @return  the super rare Pokemon generated randomly
     */
    public Pokemon generateSuperRarePokemon() {
        ArrayList<Pokemon> superrare = Pokedex.getPokedex().getSuperRarePokemonList();
        return superrare.get(new Random().nextInt(superrare.size()  + 1));
    }
}