package com.mobdeve.s11.pokeplan.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UserPokemon {
    private String userPokemonID;

    private Pokemon details;
    private String nickname;
    private String nature;
    private int level;
    private int fedCandy;
    private Date dMetDate;
    private boolean inParty;

    /**
     * Class constructor (for database use)
     */
    public UserPokemon() {}

    /**
     * Class constructor for generating a User's Pokemon
     * @param details the species-specific details of a Pokemon
     * @param inParty if true, the pokemon is currently in the user's party
     *                else, the pokemon is in the PC
     */
    public UserPokemon(Pokemon details, boolean inParty) {
        this.details = details;
        this.inParty = inParty;

        // default/initial nickname is the pokemon's species
        this.nickname = details.getSpecies();

        // nature is randomly generated
        this.nature = this.generateNature();

        // pokemon level initialized to 1, candy initialized to 0
        this.level = 1;
        this.fedCandy = 0;

        // met date is time of UserPokemon object creation
        this.dMetDate = new java.util.Date();
    }

    /**
     * Feeds candy to a pokemon. Every ten candies, the Pokemon's level will increase by 1
     * until it reaches Level 100.
     * @return  true, if pokemon leveled up
     *          false, otherwise
     */
    public boolean feedCandy() {
        fedCandy++;
        if (fedCandy%10 == 0 && level < 100) {
            levelUpPokemon();
            return true;
        }
        return false;
    }

    /**
     * Increases a Pokemon's level by 1.
     */
    public void levelUpPokemon() {
        if (level < 100) level++;
    }

    /**
     * Evolves a Pokemon if it can still evolve and has met the required level.
     */
    public void evolvePokemon() {
        if (!this.details.getEvolvesTo().isEmpty() && this.level >= this.details.getEvolveLvl()) {
            if (nickname.equals(details.getSpecies()))
                nickname = this.details.getEvolvesTo();
            this.details = Pokedex.getPokedex().getPokemon(this.details.getEvolvesTo());
        }
    }

    /**
     * @return the ID of the UserPokemon
     */
    public String getUserPokemonID() {
        return this.userPokemonID;
    }

    /**
     * @return the species-specific details of a UserPokemon
     */
    public Pokemon getDetails () {
        return this.details;
    }

    /**
     * @return the nickname of the UserPokemon
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * @return the nature of the UserPokemon
     */
    public String getNature() {
        return this.nature;
    }

    /**
     * @return the level of the UserPokemon
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * @return the total candies fed to the UserPokemon
     */
    public int getFedCandy() {
        return this.fedCandy;
    }

    /**
     * @return the date when the UserPokemon was received in "MMMM dd, yyyy" format
     */
    public String getMetDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        return formatter.format(dMetDate);
    }

    /**
     * @return the date when the UserPokemon was received (for database use)
     */
    public Date getDMetDate () {
        return this.dMetDate;
    }

    /**
     * @return  true, if pokemon is currently in the party
     *          false, if pokemon is in the PC
     */
    public boolean isInParty () {
        return this.inParty;
    }

    /**
     * @param id the ID of the UserPokemon (for database use)
     */
    public void setUserPokemonID(String id) {
        this.userPokemonID = id;
    }

    /**
     * @param nickname the nickname of the UserPokemon
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @param pokemon the species-specific details of the UserPokemon (for database use)
     */
    public void setDetails (Pokemon pokemon) {
        this.details = pokemon;
    }

    /**
     * @param nature the nature of the UserPokemon (for database use)
     */
    public void setNature(String nature) {
        this.nature = nature;
    }

    /**
     * @param level the level of the UserPokemon (for database use)
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @param fedcandy the total amount of candies fed to the UserPokemon (for database use)
     */
    public void setFedCandy(int fedcandy) {
        this.fedCandy = fedcandy;
    }

    /**
     * @param date the date when the UserPokemon was received (for database use)
     */
    public void setDMetDate (Date date) {
        this.dMetDate = date;
    }

    /**
     * @param inParty true, if pokemon is in the party (for database use)
     *                false, if Pokmeon is in the PC
     */
    public void setInParty(boolean inParty) {
        this.inParty = inParty;
    }

    /**
     * @return the percentage of candies eaten relative to when the pokemon last leveled up
     */
    public int getPercentToNextLevel() {
        return fedCandy % 10 * 10;
    }

    /**
     * Generates a random nature from the list of natures
     * @return the randomly generated nature
     */
    public String generateNature () {
        String[] NATURES = {
                "Hardy", "Lonely", "Brave", "Adamant", "Naughty", "Bold", "Docile",
                "Relaxed", "Impish", "Lax", "Timid", "Hasty", "Serious", "Jolly",
                "Naive", "Modest", "Mild", "Quiet", "Bashful", "Rash", "Calm",
                "Gentle", "Sassy", "Careful", "Quirky"
        };

        return NATURES[new Random().nextInt(NATURES.length)];
    }

    /**
     * @return the string representation of the UserPokemon
     */
    @Override
    public String toString () {
        return this.userPokemonID;
    }
}
