package com.mobdeve.s11.pokeplan;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDetails {
    private String fullName;
    private String email;
    private String userName;

    private int rarecandy;
    private int supercandy;

    public UserDetails(String fullName, String email, String userName) {
        this.fullName = fullName;
        this.email = email;
        this.userName = userName;

        this.rarecandy = 5;
        this.supercandy = 5;
    }

    public UserDetails () {
        this.fullName = "";
        this.email = "";
        this.userName = "";

        this.rarecandy = 5;
        this.supercandy = 5;
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
}
