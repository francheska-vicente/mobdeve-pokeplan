package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

public class Pool {
    private static ArrayList<String> common;
    private static ArrayList<String> uncommon;
    private static ArrayList<String> rare;
    private static ArrayList<String> superrare;

    public Pool() {
        common = new ArrayList<>();
        uncommon = new ArrayList<>();
        rare = new ArrayList<>();
        superrare = new ArrayList<>();
    }

    public static String generateCommonPokemon() {
        /* TODO: populate array w/ common pokemon here */
        String pokemon = common.get((int)(Math.random() * common.size() + 1 - 2));
        return "Common";
    }
    public static String generateUncommonPokemon() {
        /* TODO: populate array w/ uncommon pokemon here */
        String pokemon = uncommon.get((int)(Math.random() * uncommon.size() + 1 - 2));
        return "Uncommon";
    }
    public static String generateRarePokemon() {
        /* TODO: populate array w/ rare pokemon here */
        String pokemon = rare.get((int)(Math.random() * rare.size() + 1 - 2));
        return "Rare";
    }
    public static String generateSuperRarePokemon() {
        /* TODO: populate array w/ super rare pokemon here */
        String pokemon = superrare.get((int)(Math.random() * superrare.size() + 1 - 2));
        return "Super Rare";
    }
}
