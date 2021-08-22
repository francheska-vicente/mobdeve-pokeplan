package com.mobdeve.s11.pokeplan;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDetails {
    private String fullName;
    private String email;
    private String userName;

    private int rarecandy;
    private int supercandy;

    private int hatchedpkmncount;
    private int finishedtaskscount;

    public UserDetails(String fullName, String email, String userName) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;

        this.rarecandy = 5;
        this.supercandy = 5;

        this.hatchedpkmncount = 0;
        this.finishedtaskscount = 0;
    }
    public UserDetails () {

    }

    public String getName () {
        return this.fullName;
    }
    public String getEmail () {
        return this.email;
    }
    public String getUserName () {
        return this.userName;
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

    // stats
    public int getHatchedPkmnCount() {
        return hatchedpkmncount;
    }
    public void addHatchedPkmn() {
        this.hatchedpkmncount++;
    }
    public int getFinishedTaskCount() {
        return finishedtaskscount;
    }
    public void addFinishedTask() {
        this.finishedtaskscount++;
    }

    public void setFullName (String name) {
        this.fullName = name;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void setUserName (String username) {
        this.userName = username;
    }

    public void setRareCandy (int rarecandy) {
        this.rarecandy = rarecandy;
    }

    public void setSuperCandy (int supercandy) {
        this.supercandy = supercandy;
    }

    public void setHatchedPkmnCount (int num) {
        this.hatchedpkmncount = num;
    }

    public void setFinishedTaskCount (int num) {
        this.finishedtaskscount = num;
    }
}
