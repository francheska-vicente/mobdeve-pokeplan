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

    public int getLevel() {
        return level;
    }

    public int getFedcandy() {
        return fedcandy;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setFedcandy(int fedcandy) {
        this.fedcandy = fedcandy;
    }
}
