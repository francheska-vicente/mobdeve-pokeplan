package com.mobdeve.s11.pokeplan;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UserPokemon {

    private Pokemon details;
    private String nickname;
    private String nature;

    private Date dMetDate;

    private boolean inParty;

    private int level;
    private int fedCandy;

    private String userPokemonID;

    public UserPokemon () {

    }

    public UserPokemon (Pokemon pokemon, String nickname, String nature, Date metDate,
                        Boolean inParty, int level, int fedCandy, String pokemonID) {
        this.details = pokemon;
        this.nickname = nickname;
        this.nature = nature;
        this.dMetDate = metDate;
        this.inParty = inParty;
        this.level = level;
        this.fedCandy = fedCandy;
        this.userPokemonID = pokemonID;
    }

    public UserPokemon(Pokemon details, boolean inParty) {
        this.details = details;
        this.nickname = details.getSpecies();
        this.nature = this.setNature();
        this.dMetDate = new java.util.Date();
        this.level = 1;
        this.fedCandy = 0;
        this.inParty = inParty;
    }

    public void feedCandy() {
        fedCandy++;
        if (fedCandy%10 == 0)
            levelUpPokemon();
    }

    public void levelUpPokemon() {
        level++;
    }

    @Exclude
    public void evolvePokemon() {
        Pokedex pokedex = new Pokedex();
        this.details = pokedex.getPokemon(this.details.getEvolvesTo());
    }

    @Exclude
    public Pokemon getPokemonDetails() {
        return this.details;
    }

    public Pokemon getDetails () {
        return this.details;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getNature() {
        return this.nature;
    }

    public String getMetDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        return formatter.format(dMetDate);
    }

    public Date getDMetDate () {
        return this.dMetDate;
    }
    public String getUserPokemonID() {
        return this.userPokemonID;
    }

    public int getLevel() {
        return this.level;
    }

    public int getPercentToNextLevel() {
        return fedCandy % 10 * 10;
    }

    public int getFedCandy() {
        return this.fedCandy;
    }

    public boolean isInParty () {
        return this.inParty;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUserPokemonID(String id) {
        this.userPokemonID = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setFedCandy(int fedcandy) {
        this.fedCandy = fedcandy;
    }

    public void setInParty(boolean inParty) {
        this.inParty = inParty;
    }

    public String toString () {
        return this.userPokemonID;
    }

    public String setNature () {
        String[] NATURES = {
                "Hardy", "Lonely", "Brave", "Adamant", "Naughty", "Bold", "Docile",
                "Relaxed", "Impish", "Lax", "Timid", "Hasty", "Serious", "Jolly",
                "Naive", "Modest", "Mild", "Quiet", "Bashful", "Rash", "Calm",
                "Gentle", "Sassy", "Careful", "Quirky"
        };

        return NATURES[new Random().nextInt(NATURES.length)];
    }
}
