package com.mobdeve.s11.pokeplan;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;

@IgnoreExtraProperties
public class UserDetails {
    private String fullName;
    private String email;
    private String userName;
    private int starterdexnum;
    private CustomDate birthday;

    private ArrayList<Boolean> userPokedex;

    private int rarecandy;
    private int supercandy;

    private int hatchedpkmncount;
    private int completedtaskscount;

    /**
     * Class constructor (for database use)
     */
    public UserDetails() {}

    /**
     * Class constructor for creating a new user
     * @param fullName the full name entered by the user
     * @param email the email entered by the user
     * @param userName the username entered by the user
     * @param dexnum the pokedex number of the user's starter pokemon
     * @param birthday the date of birth of the user
     */
    public UserDetails(String fullName, String email, String userName, int dexnum, CustomDate birthday) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;
        this.starterdexnum = dexnum;
        this.birthday = birthday;

        // initializes the user's Pokedex as an ArrayList
        Boolean[] userPokedexBool = new Boolean[150];
        Arrays.fill(userPokedexBool, Boolean.FALSE);
        userPokedex = new ArrayList<>(Arrays.asList(userPokedexBool));

        // sets initial candy amounts to 5
        this.rarecandy = 5;
        this.supercandy = 5;

        // initializes counters to 0
        this.hatchedpkmncount = 0;
        this.completedtaskscount = 0;
    }


    /* functions for user's details */
    /**
     * @return the full name of the user
     */
    public String getFullName () {
        return this.fullName;
    }

    /**
     * @return the email of the user
     */
    public String getEmail () {
        return this.email;
    }

    /**
     * @return the username of the user
     */
    public String getUserName () {
        return this.userName;
    }

    /**
     * @return the birthday of the user
     */
    public CustomDate getBirthday () {
        return this.birthday;
    }

    /**
     * @return the pokedex number of the user's starter pokemon
     */
    public int getStarterDexNum() {
        return starterdexnum;
    }

    /**
     * @param name the full name of the user
     */
    public void setFullName (String name) {
        this.fullName = name;
    }

    /**
     * @param email the email of the user
     */
    public void setEmail (String email) {
        this.email = email;
    }

    /**
     * @param username the username of the user
     */
    public void setUserName (String username) {
        this.userName = username;
    }

    /**
     * @param birthday the birthday of the user (for database use)
     */
    public void setBirthday (CustomDate birthday) {
        this.birthday = birthday;
    }

    /**
     * @param starterdexnum the pokedex number of the user's starter pokemon (for database use)
     */
    public void setStarterDexNum(int starterdexnum) {
         this.starterdexnum = starterdexnum;
    }


    /* functions for user's pokedex */
    /**
     * @return the pokedex of the user
     */
    public ArrayList<Boolean> getUserPokedex() {
        return userPokedex;
    }

    /**
     * @param pokedex the pokedex of the user (for database use)
     */
    public void setUserPokedex(ArrayList<Boolean> pokedex) {
        this.userPokedex = pokedex;
    }

    /**
     * Marks a specific pokemon as caught
     * @param dexnum the pokedex number of the pokemon caught
     */
    public void setCaught(int dexnum) {
        this.userPokedex.set(dexnum-1, true);
    }

    /**
     * Counts the number of pokemon caught by the user
     * @return  the number of pokemon marked as caught
     */
    public int getNumCaught() {
        int ctr = 0;
        for(int j = 0; j < userPokedex.size(); j++)
            if(userPokedex.get(j))
                ctr++;
        return ctr;
    }

    /**
     * Counts the number of pokemon not yet caught by the user
     * @return  the number of pokemon marked as uncaught
     */
    public int getNumNotCaught() {
        int ctr = 0;
        for(int j = 0; j < userPokedex.size(); j++)
            if(!userPokedex.get(j))
                ctr++;
        return ctr;
    }

    /* functions for user's inventory */

    /**
     * @return the number of rare candies the user owns
     */
    public int getRareCandy() {
        return rarecandy;
    }

    /**
     * Adds rare candies to the user's inventory
     * @param candy the number of candies to add, should be a positive value
     */
    public void addRareCandy(int candy) {
        this.rarecandy += candy;
    }

    /**
     * Removes rare candies from the user's inventory
     * @param candy the number of candies to subtract, should be a positive value
     */
    public void subtractRareCandy(int candy) {
        this.rarecandy -= candy;
    }

    /**
     * @param rarecandy the number of rare candies the user has (for database use)
     */
    public void setRareCandy (int rarecandy) {
        this.rarecandy = rarecandy;
    }

    /**
     * @return the number of super candies the user owns
     */
    public int getSuperCandy() {
        return supercandy;
    }

    /**
     * Adds super candies to the user's inventory
     * @param candy the number of candies to add, should be a positive value
     */
    public void addSuperCandy(int candy) {
        this.supercandy += candy;
    }

    /**
     * Removes super candies from the user's inventory
     * @param candy the number of candies to subtract, should be a positive value
     */
    public void subtractSuperCandy(int candy) {
        this.supercandy -= candy;
    }

    /**
     * @param supercandy the number of super candies the user has (for database use)
     */
    public void setSuperCandy (int supercandy) {
        this.supercandy = supercandy;
    }


    /* functions for user's statistics */
    /**
     * @return the total number of pokemon the user has hatched
     */
    public int getHatchedPkmnCount() {
        return hatchedpkmncount;
    }

    /**
     * Increments the number of pokemon the user hatched
     * @param hatched number of pokemon the user has hatched
     */
    public void addHatchedPkmn(int hatched) {
        this.hatchedpkmncount += hatched;
    }

    /**
     * @param num number of pokemon the user has hatched (for database use)
     */
    public void setHatchedPkmnCount (int num) {
        this.hatchedpkmncount = num;
    }

    /**
     * @return the total number of tasks the user has completed
     */
    public int getCompletedTaskCount() {
        return completedtaskscount;
    }

    /**
     * Increments the number of tasks the user finished
     * @param completed number of tasks the user has finished
     */
    public void addCompletedTask(int completed) {
        this.completedtaskscount += completed;
    }

    /**
     * @param num number of tasks the user finished (for database use)
     */
    public void setFinishedTaskCount (int num) {
        this.completedtaskscount = num;
    }
}
