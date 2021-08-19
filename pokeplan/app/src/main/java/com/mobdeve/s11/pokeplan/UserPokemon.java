package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class UserPokemon {
    private final String[] NATURES = {
            "Hardy", "Lonely", "Brave", "Adamant", "Naughty", "Bold", "Docile",
            "Relaxed", "Impish", "Lax", "Timid", "Hasty", "Serious", "Jolly",
            "Naive", "Modest", "Mild", "Quiet", "Bashful", "Rash", "Calm",
            "Gentle", "Sassy", "Careful", "Quirky"
    };
    private Pokemon details;
    private String nickname;
    private String nature;
    private String pokemonID;
    private Date metDate;
    private int level;
    private int fedcandy;

    public UserPokemon(Pokemon details) {
        this.details = details;
        this.nickname = details.getSpecies();
        this.nature = NATURES[new Random().nextInt(NATURES.length)];
        this.metDate = new java.util.Date();
        this.pokemonID = details.getSpecies() + metDate;
        this.level = 1;
        this.fedcandy = 0;
    }

    public void feedCandy() {
        fedcandy++;
        if (fedcandy%10 == 0)
            levelUpPokemon();
    }

    public void levelUpPokemon() {
        level++;
    }

    public void evolvePokemon() {

    }

    public Pokemon getPokemonDetails() {
        return details;
    }

    public String getNickname() {
        return nickname;
    }

    public String getNature() {
        return nature;
    }

    public Date getMetDate() {
        return metDate;
    }

    public String getPokemonID() {
        return pokemonID;
    }

    public int getLevel() {
        return level;
    }

    public int getPercentToNextLevel() {
        return fedcandy % 10 * 10;
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

    public String toString () {
        return this.pokemonID;
    }
}
