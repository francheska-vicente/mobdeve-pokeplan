package com.mobdeve.s11.pokeplan;

public class UserPokemon {
    private Pokemon details;
    private String nickname;
    private String pokemonID;
    private int level;
    private int fedcandy;

    public UserPokemon(Pokemon details) {
        this.details = details;
        this.nickname = details.getSpecies();
        this.pokemonID = details.getSpecies() + new java.util.Date();
        this.level = 1;
        this.fedcandy = 0;
    }

    public void feedCandy() {

    }

    public void levelUpPokemon() {

    }

    public void evolvePokemon() {

    }

    public Pokemon getPokemonDetails() {
        return details;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPokemonID() {
        return pokemonID;
    }

    public int getLevel() {
        return level;
    }

    public int getFedCandy() {
        return fedcandy;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPokemonID(String id) {
        this.pokemonID = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setFedCandy(int fedcandy) {
        this.fedcandy = fedcandy;
    }
}
