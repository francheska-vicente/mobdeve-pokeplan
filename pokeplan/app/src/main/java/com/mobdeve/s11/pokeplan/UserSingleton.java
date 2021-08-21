package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;
import java.util.Arrays;

public class UserSingleton {
    private static UserSingleton user;

    private UserDetails userDetails;
    private ArrayList<Task> tasks;
    private ArrayList<UserPokemon> userPokemonPC;
    private ArrayList<UserPokemon> userPokemonParty;
    private Boolean[] userPokedex;

    private int rarecandy;
    private int supercandy;

    public static UserSingleton getUser() {
        //instantiate a new CustomerLab if we didn't instantiate one yet
        if (user == null) {
            user = new UserSingleton();
        }
        return user;
    }

    private UserSingleton(){
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

    // user details
    public UserDetails getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(UserDetails details) {
        this.userDetails = details;
    }

    // tasks
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    // pokemons
    public void addPokemon(int dexnum) {
        UserPokemon pkmn = new UserPokemon(new Pokedex().getPokemon(dexnum));
        userPokedex[dexnum-1] = true;
        if (userPokemonParty.size() < 6) {
            userPokemonParty.add(pkmn);
        }
        else {
            userPokemonPC.add(pkmn);
        }
    }

    // party pokemon
    public ArrayList<UserPokemon> getUserPokemonParty() {
        return userPokemonParty;
    }
    public void setUserPokemonParty(ArrayList<UserPokemon> party) {
        this.userPokemonParty = party;
    }
    public UserPokemon getPokemonInParty(String id) {
        for(int j = 0; j < userPokemonParty.size(); j++)
            if(userPokemonParty.get(j).getPokemonID().equals(id))
                return userPokemonParty.get(j);

        return null;
    }
    public void movePokemonToPC(UserPokemon pkmn) {
        for(int j = 0; j < userPokemonParty.size(); j++)
            if(userPokemonParty.get(j).equals(pkmn))
                userPokemonParty.remove(j);

        userPokemonPC.add(pkmn);
    }

    // pc pokemon
    public ArrayList<UserPokemon> getUserPokemonPC() {
        return userPokemonPC;
    }
    public void setUserPokemonPC(ArrayList<UserPokemon> pc) {
        this.userPokemonPC = pc;
    }
    public UserPokemon getPokemonInPC(String id) {
        for(int j = 0; j < userPokemonPC.size(); j++)
            if(userPokemonPC.get(j).getPokemonID().equals(id))
                return userPokemonPC.get(j);

        return null;
    }
    public void movePokemonToParty(UserPokemon pkmn) {
        for(int j = 0; j < userPokemonPC.size(); j++)
            if(userPokemonPC.get(j).equals(pkmn))
                userPokemonPC.remove(j);

        userPokemonParty.add(pkmn);
    }

    // pokedex
    public Boolean[] getUserPokedex() {
        return userPokedex;
    }
    public void setUserPokedex(Boolean[] pokedex) {
        this.userPokedex = pokedex;
    }

    // items
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
