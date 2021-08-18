package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

public class User {
    private ArrayList<UserPokemon> userPokemonPC;
    private ArrayList<UserPokemon> userPokemonParty;
    private boolean[] userPokedex;

    private String fullName;
    private String email;
    private String userName;

    public User(){

    }

    public User(String fullName, String email, String userName, UserPokemon starter) {
        userPokemonPC = new ArrayList<>();
        userPokemonParty = new ArrayList<>(6);
        userPokedex = new boolean[150];

        this.fullName = fullName;
        this.email = email;
        this.userName = userName;

        addPokemon();
    }

    public void addPokemon() {
        UserPokemon pokemon = new UserPokemon(new Pokedex().getPokemon(0));
        userPokemonPC.add(pokemon);
        userPokemonParty.add(pokemon);
    }

    public ArrayList<UserPokemon> getUserPokemonParty() {
        ArrayList<UserPokemon> data = new ArrayList<>();
        data.add(new UserPokemon(new Pokedex().getPokemon(1)));
        data.add(new UserPokemon(new Pokedex().getPokemon(2)));
        data.add(new UserPokemon(new Pokedex().getPokemon(3)));
        return data;

        //return userPokemonParty;
    }
}
