package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;
import java.util.Arrays;

public class UserSingleton {
    private ArrayList<UserPokemon> userPokemonPC;
    private ArrayList<UserPokemon> userPokemonParty;
    private Boolean[] userPokedex;

    private int rarecandy;
    private int supercandy;

    public UserSingleton(){
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

        rarecandy = 5;
        supercandy = 5;
    }

    public UserSingleton(String fullName, String email, String userName, UserPokemon starter) {
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

    public void addRareCandy(int candy) {
        this.rarecandy += candy;
    }

    public void subtractRareCandy(int candy) {
        this.rarecandy -= candy;
    }

    public int getSuperCandy() {
        return supercandy;
    }

    public void addSuperCandy(int candy) {
        this.supercandy += candy;
    }

    public void subtractSuperCandy(int candy) {
        this.supercandy -= candy;
    }
}
