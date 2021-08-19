package com.mobdeve.s11.pokeplan;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private String fullName;
    private String email;
    private String userName;

    private ArrayList<UserPokemon> userPokemonPC;
    private ArrayList<UserPokemon> userPokemonParty;
    private Boolean[] userPokedex;

    private int rarecandy;
    private int supercandy;

    public User(){
        // for testing purposes
        userPokemonParty = new ArrayList<>(6);
        userPokemonParty.add(new UserPokemon(new Pokedex().getPokemon(22)));
        userPokemonParty.add(new UserPokemon(new Pokedex().getPokemon(68)));
        userPokemonParty.add(new UserPokemon(new Pokedex().getPokemon(84)));
        userPokemonParty.add(new UserPokemon(new Pokedex().getPokemon(93)));
        userPokemonParty.add(new UserPokemon(new Pokedex().getPokemon(95)));
        userPokemonParty.add(new UserPokemon(new Pokedex().getPokemon(138)));

        userPokedex = new Boolean[150];
        Arrays.fill(userPokedex, Boolean.FALSE);
        userPokedex[21] = true;
        userPokedex[67] = true;
        userPokedex[83] = true;
        userPokedex[92] = true;
        userPokedex[94] = true;
        userPokedex[137] = true;
    }

    public User(String fullName, String email, String userName, UserPokemon starter) {
        // initializes user pc
        userPokemonPC = new ArrayList<>();

        // puts starter in party
        userPokemonParty = new ArrayList<>(6);
        userPokemonParty.add(starter);

        // initializes the pokedex, puts starter in pokedex
        userPokedex = new Boolean[150];
        Arrays.fill(userPokedex, Boolean.FALSE);
        userPokedex[starter.getPokemonDetails().getDexNum()-1] = true;

        rarecandy = 0;
        supercandy = 0;

        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
    }

    public void addPokemon() {
        UserPokemon pokemon = new UserPokemon(new Pokedex().getPokemon(0));
        userPokemonPC.add(pokemon);
        userPokemonParty.add(pokemon);
    }

    public ArrayList<UserPokemon> getUserPokemonParty() {
        return userPokemonParty;
    }

    public UserPokemon getPokemonInParty(String id) {
        for(int j = 0; j < userPokemonParty.size(); j++)
            if(userPokemonParty.get(j).getPokemonID().equals(id))
                return userPokemonParty.get(j);

        // temp
        return new UserPokemon(new Pokedex().getPokemon(1));
    }

    public Boolean[] getUserPokedex() {
        return userPokedex;
    }

    public int getRareCandy() {
        return rarecandy;
    }

    public void setRareCandy(int rarecandy) {
        this.rarecandy = rarecandy;
    }

    public int getSuperCandy() {
        return supercandy;
    }

    public void setSuperCandy(int supercandy) {
        this.supercandy = supercandy;
    }
}
