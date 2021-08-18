package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

public class User {
    private ArrayList<UserPokemon> userPokemonList;
    private ArrayList<UserPokemon> userPokemonParty;
    private boolean[] userPokedex;

    private String fullName;
    private String email;
    private String userName;

    public User(String fullName, String email, String userName, int dexNumber) {
        userPokemonList = new ArrayList<>();
        userPokemonParty = new ArrayList<>(6);
        userPokedex = new boolean[150];

        this.fullName = fullName;
        this.email = email;
        this.userName = userName;

        addPokemon(dexNumber);
    }

    public void addPokemon(int dexNumber) {
        UserPokemon pokemon = new UserPokemon(new Pokedex().getPokemon(dexNumber));
        userPokemonList.add(pokemon);
        userPokemonParty.add(pokemon);
    }

    public ArrayList<UserPokemon> getUserPokemonParty() {
        return userPokemonParty;
    }
}
