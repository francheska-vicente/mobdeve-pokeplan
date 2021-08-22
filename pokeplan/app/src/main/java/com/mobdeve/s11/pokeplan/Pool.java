package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;
import java.util.Random;

public class Pool {
    private static ArrayList<Pokemon> common;
    private static ArrayList<Pokemon> uncommon;
    private static ArrayList<Pokemon> rare;
    private static ArrayList<Pokemon> superrare;
    private static Pokedex pokemonList;

    public Pool() {
        common = new ArrayList<>();
        uncommon = new ArrayList<>();
        rare = new ArrayList<>();
        superrare = new ArrayList<>();
        pokemonList = Pokedex.getPokedex();
    }

    public static Pokemon generateCommonPokemon() {
        common = pokemonList.getCommonPokemonList();
        Pokemon pokemon = common.get(new Random().nextInt(common.size()  + 1));
        return pokemon;
    }
    public static Pokemon generateUncommonPokemon() {
        uncommon = pokemonList.getUncommonPokemonList();
        Pokemon pokemon = uncommon.get(new Random().nextInt(uncommon.size()  + 1));
        return pokemon;
    }
    public static Pokemon generateRarePokemon() {
        rare = pokemonList.getRarePokemonList();
        Pokemon pokemon = rare.get(new Random().nextInt(rare.size() + 1));
        return pokemon;
    }
    public static Pokemon generateSuperRarePokemon() {
        superrare = pokemonList.getSuperRarePokemonList();
        Pokemon pokemon = superrare.get(new Random().nextInt(superrare.size()  + 1));
        return pokemon;
    }
}
