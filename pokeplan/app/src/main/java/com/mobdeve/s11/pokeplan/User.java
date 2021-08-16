package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

public class User {
    private ArrayList<UserPokemon> userPokemonList;
    private ArrayList<UserPokemon> userPokemonParty;
    private boolean[] userPokedex;

    public User(UserPokemon starter) {
        userPokemonList = new ArrayList<>();
        userPokemonParty = new ArrayList<>(6);
        userPokedex = new boolean[150];
    }

    public void addPokemon() {
        UserPokemon pokemon = new UserPokemon(new Pokedex().getPokemon(0));
        userPokemonList.add(pokemon);
        userPokemonParty.add(pokemon);
    }
}
