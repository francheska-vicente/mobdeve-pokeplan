package com.mobdeve.s11.pokeplan.models;

import com.mobdeve.s11.pokeplan.data.PokedexDataHelper;

import java.util.ArrayList;

public class Pokedex {
    private static Pokedex pokedex;
    private final ArrayList<Pokemon> pokemonList;
    private final ArrayList<String> pokedexInfo;

    /**
     * Private class constructor
     */
    private Pokedex() {
        pokemonList = new PokedexDataHelper().fillPokedex();
        pokedexInfo = new PokedexDataHelper().fillPokedexInfo();
    }

    /**
     * Static method that will create a new pokedex if not yet instantiated, otherwise it will
     * call the already instatiated object
     */
    public static Pokedex getPokedex() {
        if (pokedex == null)
            pokedex = new Pokedex();
        return pokedex;
    }

    /**
     * Returns the details of a Pokemon given its Pokedex Number.
     * @param dexNum    the Pokedex number of a Pokemon
     * @return          the Pokemon with the specific Pokedex Number
     */
    public Pokemon getPokemon(int dexNum) {
        return pokemonList.get(dexNum-1);
    }

    /**
     * Returns the details of a Pokemon given its species.
     * @param species   the species of a Pokemon
     * @return          the Pokemon with the specific Pokedex Number
     */
    public Pokemon getPokemon(String species) {
        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getSpecies().equals(species))
                return pokemonList.get(j);
        }
        return null;
    }

    /**
     * Returns a list of all Pokemon.
     * @return          the ArrayList of all Pokemon
     */
    public ArrayList<Pokemon> getAllPokemon() {
        return pokemonList;
    }

    /**
     * Returns a list of Pokemon classified as Common.
     * @return          the ArrayList of all Common Pokemon
     */
    public ArrayList<Pokemon> getCommonPokemonList() {
        ArrayList<Pokemon> common = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Common"))
                common.add(pokemonList.get(j));
        }

        return common;
    }

    /**
     * Returns a list of Pokemon classified as Uncommon.
     * @return          the ArrayList of all Uncommon Pokemon
     */
    public ArrayList<Pokemon> getUncommonPokemonList() {
        ArrayList<Pokemon> uncommon = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Uncommon"))
                uncommon.add(pokemonList.get(j));
        }

        return uncommon;
    }

    /**
     * Returns a list of Pokemon classified as Rare.
     * @return          the ArrayList of all Rare Pokemon
     */
    public ArrayList<Pokemon> getRarePokemonList() {
        ArrayList<Pokemon> rare = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Rare"))
                rare.add(pokemonList.get(j));
        }

        return rare;
    }

    /**
     * Returns a list of Pokemon classified as Super Rare.
     * @return          the ArrayList of all Super Rare Pokemon
     */
    public ArrayList<Pokemon> getSuperRarePokemonList() {
        ArrayList<Pokemon> superrare = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Super Rare"))
                superrare.add(pokemonList.get(j));
        }

        return superrare;
    }

    /**
     * Returns the Pokedex Entry of a Pokemon given its Pokedex Number.
     * @param dexNum    the Pokedex number of a Pokemon
     * @return          the Pokemon Entry of a Pokemon
     */
    public String getPokemonInfo(int dexNum) {
        return pokedexInfo.get(dexNum-1);
    }

}
