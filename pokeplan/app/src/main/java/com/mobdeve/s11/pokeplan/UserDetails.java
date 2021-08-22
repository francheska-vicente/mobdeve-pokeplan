package com.mobdeve.s11.pokeplan;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDetails {
    private String fullName;
    private String email;
    private String userName;

    private int starterdexnum;

    private int rarecandy;
    private int supercandy;

    private int hatchedpkmncount;
    private int completedtaskscount;

    public UserDetails () {
    }
    public UserDetails(String fullName, String email, String userName, int dexnum) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;

        this.starterdexnum = dexnum;

        this.rarecandy = 5;
        this.supercandy = 5;

        this.hatchedpkmncount = 0;
        this.completedtaskscount = 0;
    }


    public String getFullName () {
        return this.fullName;
    }
    public String getEmail () {
        return this.email;
    }
    public String getUserName () {
        return this.userName;
    }

    // starter
    public int getStarterDexNum() {
        return starterdexnum;
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
    public int getCompletedTaskCount() {
        return completedtaskscount;
    }
    public void addCompletedTask() {
        this.completedtaskscount++;
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
        this.completedtaskscount = num;
    }
}
