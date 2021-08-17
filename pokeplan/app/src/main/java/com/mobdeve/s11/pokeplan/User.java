package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

public class User {
    private ArrayList<UserPokemon> userPokemonList;
    private ArrayList<UserPokemon> userPokemonParty;
    private boolean[] userPokedex;

    private String fullName;
    private String email;
    private String userName;
    private String password;

    public User(String fullName, String email, String password, String userName, UserPokemon starter) {
        userPokemonList = new ArrayList<>();
        userPokemonParty = new ArrayList<>(6);
        userPokedex = new boolean[150];

        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.userName = userName;

        addPokemon();
    }

    public void addPokemon() {
        UserPokemon pokemon = new UserPokemon(new Pokedex().getPokemon(0));
        userPokemonList.add(pokemon);
        userPokemonParty.add(pokemon);
    }

    public ArrayList<UserPokemon> getUserPokemonParty() {
        return userPokemonParty;
    }
}
