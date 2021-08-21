package com.mobdeve.s11.pokeplan;

public class Pokemon {
    private String species;
    private int dexNum;
    private String rarity;
    private int evolveLvl;
    private String evolvesTo;
    private String type1;
    private String type2;

    /**
     *  full constructor
     */
    public Pokemon (String species, int dexnum, String rarity, int evolve_lvl,
                    String evolves_to, String type_1, String type_2) {
        this.species = species;
        this.dexNum = dexnum;
        this.rarity = rarity;
        this.evolveLvl = evolve_lvl;
        this.evolvesTo = evolves_to;
        this.type1 = type_1;
        this.type2 = type_2;
    }

    /**
     *  no evolve lvl + evolves to, has type 2
     */
    public Pokemon (String species, int dexnum, String rarity,
                    String type_1, String type_2) {
        this.species = species;
        this.dexNum = dexnum;
        this.rarity = rarity;
        this.type1 = type_1;
        this.type2 = type_2;
        this.evolveLvl = -1;
        this.evolvesTo = "";
    }

    /**
     *  no evolve lvl + evolves to, no type 2
     */
    public Pokemon (String species, int dexnum, String rarity,
                    String type_1) {
        this.species = species;
        this.dexNum = dexnum;
        this.rarity = rarity;
        this.type1 = type_1;
        this.evolveLvl = -1;
        this.type2 = "";
        this.evolvesTo = "";
    }

    /**
     *  has evolve lvl + evolves to, no type 2
     */
    public Pokemon (String species, int dexnum, String rarity, int evolve_lvl,
                    String evolves_to, String type_1) {
        this.species = species;
        this.dexNum = dexnum;
        this.evolveLvl = evolve_lvl;
        this.evolvesTo = evolves_to;
        this.rarity = rarity;
        this.type1 = type_1;
    }

    public Pokemon () {

    }

    public String getSpecies() {
        return species;
    }

    public int getDexNum() {
        return dexNum;
    }

    public String getRarity() {
        return rarity;
    }

    public int getEvolveLvl() {
        return evolveLvl;
    }

    public String getEvolvesTo() {
        return evolvesTo;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public void setSpecies (String species) {
        this.species = species;
    }

    public void setDexNum (int num) {
        this.dexNum = num;
    }

    public void setRarity (String rare) {
        this.rarity = rare;
    }

    public void setEvolveLvl (int evolve) {
        this.evolveLvl = evolve;
    }

    public void setEvolvesTo (String pokemon) {
        this.evolvesTo = pokemon;
    }

    public void setType1 (String type) {
        this.type1 = type;
    }

    public void setType2 (String type) {
        this.type2 = type;
    }
}
