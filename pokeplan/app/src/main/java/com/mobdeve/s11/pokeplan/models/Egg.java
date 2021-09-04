package com.mobdeve.s11.pokeplan.models;

import java.util.ArrayList;
import java.util.Random;

public class Egg {
    private final Timer timer;
    private final boolean deepFocusEnabled;

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
    private final int[] seventh = {0, 0, 30, 70};

    /**
     * Class constructor to generate an Egg
     * @param timer the timer object containing the user's focus time
     * @param deepFocusEnabled if true, deep focus mode was enabled for the duration of the timer
     */
    public Egg(Timer timer, boolean deepFocusEnabled) {
        this.timer = timer;
        this.deepFocusEnabled = deepFocusEnabled;
        this.raritypool = new ArrayList<>(100);
    }

    /**
     * Class constructor to generate an Egg
     * @return the generated pokemon
     */
    public Pokemon generatePokemon() {
        // get time in minutes
        int minutes = timer.convertToMinutes();

        // fix rates based on time, enabled deep focus mode will boost rates
        if (deepFocusEnabled) setBoostedRates(minutes);
        else setNormalRates(minutes);

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
    private void populateRarityPool(int[] rates) {
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
    private void setNormalRates(int minutes) {
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
     * Sets better rates for rarities if Deep Focus Mode was enabled.
     * @param minutes   number of minutes set in the Focus Timer
     */
    private void setBoostedRates(int minutes) {
        if (minutes >= 5 && minutes < 20) {
            populateRarityPool(second);
        }
        else if (minutes >= 20 && minutes < 40) {
            populateRarityPool(third);
        }
        else if (minutes >= 40 && minutes < 60) {
            populateRarityPool(fourth);
        }
        else if (minutes >= 60 && minutes < 120) {
            populateRarityPool(fifth);
        }
        else if (minutes >= 120 && minutes < 240) {
            populateRarityPool(sixth);
        }
        else {
            populateRarityPool(seventh);
        }
    }

    /**
     * Randomly generates a Common Pokemon.
     * @return  the common Pokemon generated randomly
     */
    private Pokemon generateCommonPokemon() {
        ArrayList<Pokemon> common = Pokedex.getPokedex().getCommonPokemonList();
        return common.get(new Random().nextInt(common.size()));
    }

    /**
     * Randomly generates a Uncommon Pokemon.
     * @return  the uncommon Pokemon generated randomly
     */
    private Pokemon generateUncommonPokemon() {
        ArrayList<Pokemon> uncommon = Pokedex.getPokedex().getUncommonPokemonList();
        return uncommon.get(new Random().nextInt(uncommon.size()));
    }

    /**
     * Randomly generates a Rare Pokemon.
     * @return  the rare Pokemon generated randomly
     */
    private Pokemon generateRarePokemon() {
        ArrayList<Pokemon> rare = Pokedex.getPokedex().getRarePokemonList();
        return rare.get(new Random().nextInt(rare.size()));
    }

    /**
     * Randomly generates a Super Rare Pokemon.
     * @return  the super rare Pokemon generated randomly
     */
    private Pokemon generateSuperRarePokemon() {
        ArrayList<Pokemon> superrare = Pokedex.getPokedex().getSuperRarePokemonList();
        return superrare.get(new Random().nextInt(superrare.size()));
    }
}