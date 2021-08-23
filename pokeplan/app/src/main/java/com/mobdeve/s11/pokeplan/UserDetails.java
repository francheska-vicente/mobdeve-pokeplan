package com.mobdeve.s11.pokeplan;
import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@IgnoreExtraProperties
public class UserDetails {
    private String fullName;
    private String email;
    private String userName;

    private int starterdexnum;
    private ArrayList<Boolean> userPokedex;

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
        Boolean[] userPokedexBool = new Boolean[150];
        Arrays.fill(userPokedexBool, Boolean.FALSE);
        userPokedex = new ArrayList<Boolean>(Arrays.asList(userPokedexBool));
        Log.d("hello pare", "inside constructor");
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
    public void setFullName (String name) {
        this.fullName = name;
    }
    public void setEmail (String email) {
        this.email = email;
    }
    public void setUserName (String username) {
        this.userName = username;
    }

    // starter
    public int getStarterDexNum() {
        return starterdexnum;
    }
    public void setStarterDexNum(int starterdexnum) {
         this.starterdexnum = starterdexnum;
    }

    // pokedex
    public ArrayList<Boolean> getUserPokedex() {
        return userPokedex;
    }
    public void setUserPokedex(ArrayList<Boolean> pokedex) {
        this.userPokedex = pokedex;
    }
    public void setCaught(int dexnum) {
        this.userPokedex.set(dexnum-1, true);
    }
    public int getNumCaught() {
        int ctr = 0;
        for(int j = 0; j < userPokedex.size(); j++)
            if(userPokedex.get(j) == true)
                ctr++;
        return ctr;
    }
    public int getNumNotCaught() {
        int ctr = 0;
        for(int j = 0; j < userPokedex.size(); j++)
            if(userPokedex.get(j) == false)
                ctr++;
        return ctr;
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
    public void setRareCandy (int rarecandy) {
        this.rarecandy = rarecandy;
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
    public void setSuperCandy (int supercandy) {
        this.supercandy = supercandy;
    }

    // stats
    public int getHatchedPkmnCount() {
        return hatchedpkmncount;
    }
    public void addHatchedPkmn() {
        this.hatchedpkmncount++;
    }
    public void setHatchedPkmnCount (int num) {
        this.hatchedpkmncount = num;
    }
    public int getCompletedTaskCount() {
        return completedtaskscount;
    }
    public void addCompletedTask() {
        this.completedtaskscount++;
    }
    public void setFinishedTaskCount (int num) {
        this.completedtaskscount = num;
    }
}
