package com.mobdeve.s11.pokeplan;

import android.util.Log;

import java.util.ArrayList;

public class Pokedex {
    private ArrayList<Pokemon> pokemonList;

    public Pokedex() {
        pokemonList = new ArrayList<>();
        pokemonList.add(new Pokemon("Bulbasaur",1,"Uncommon",
                16,"Ivysaur","Grass","Poison"));
        pokemonList.add(new Pokemon("Charmander",4,"Uncommon",
                16,"Charmeleon","Fire"));
        pokemonList.add(new Pokemon("Squirtle",7,"Uncommon",
                16,"Wartortle","Water"));

        pokemonList.add(new Pokemon("Zigzagoon",48,"Common",
                20,"Linoone","Normal"));
        pokemonList.add(new Pokemon("Pidgey",50,"Common",
                18,"Pidgeotto","Normal","Flying"));
        pokemonList.add(new Pokemon("Lillipup",57,"Common",
                16,"Herdier","Normal"));

        pokemonList.add(new Pokemon("Dratini",123,"Rare",
                30,"Dragonair","Dragon"));
        pokemonList.add(new Pokemon("Larvitar",126,"Rare",
                30,"Pupitar","Rock","Ground"));
        pokemonList.add(new Pokemon("Bagon",129,"Rare",
                30,"Shelgon","Dragon"));

        pokemonList.add(new Pokemon("Mew",140,"Super Rare",
                "Psychic"));
        pokemonList.add(new Pokemon("Celebi",141,"Super Rare",
                "Psychic","Grass"));
        pokemonList.add(new Pokemon("Jirachi",142,"Super Rare",
                "Steel","Psychic"));
    }

    public ArrayList<Pokemon> getCommonPokemonList() {
        ArrayList<Pokemon> common = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Common"))
                common.add(pokemonList.get(j));
        }

        return common;
    }

    public ArrayList<Pokemon> getUncommonPokemonList() {
        ArrayList<Pokemon> uncommon = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Uncommon"))
                uncommon.add(pokemonList.get(j));
        }

        return uncommon;
    }

    public ArrayList<Pokemon> getRarePokemonList() {
        ArrayList<Pokemon> rare = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Rare"))
                rare.add(pokemonList.get(j));
        }

        return rare;
    }

    public ArrayList<Pokemon> getSuperRarePokemonList() {
        ArrayList<Pokemon> superrare = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Super Rare"))
                superrare.add(pokemonList.get(j));
        }

        return superrare;
    }
}
