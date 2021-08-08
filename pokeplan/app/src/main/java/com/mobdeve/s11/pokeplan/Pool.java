package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

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
        pokemonList = new Pokedex();
    }

    public static Pokemon generateCommonPokemon() {
        common = pokemonList.getCommonPokemonList();
        Pokemon pokemon = common.get((int)(Math.random() * common.size() + 1 - 2));
        return pokemon;
    }
    public static Pokemon generateUncommonPokemon() {
        uncommon = pokemonList.getUncommonPokemonList();
        Pokemon pokemon = uncommon.get((int)(Math.random() * uncommon.size() + 1 - 2));
        return pokemon;
    }
    public static Pokemon generateRarePokemon() {
        rare = pokemonList.getRarePokemonList();
        Pokemon pokemon = rare.get((int)(Math.random() * rare.size() + 1 - 2));
        return pokemon;
    }
    public static Pokemon generateSuperRarePokemon() {
        superrare = pokemonList.getSuperRarePokemonList();
        Pokemon pokemon = superrare.get((int)(Math.random() * superrare.size() + 1 - 2));
        return pokemon;
    }
}
