package com.mobdeve.s11.pokeplan;

public class Pokemon {
    private String species;
    private int dexnum;
    private String rarity;
    private int evolve_lvl;
    private String evolves_to;
    private String type_1;
    private String type_2;


    /**
     *  full constructor
     */
    public Pokemon (String species, int dexnum, String rarity, int evolve_lvl,
                    String evolves_to, String type_1, String type_2) {
        this.species = species;
        this.dexnum = dexnum;
        this.rarity = rarity;
        this.evolve_lvl = evolve_lvl;
        this.evolves_to = evolves_to;
        this.type_1 = type_1;
        this.type_2 = type_2;
    }

    /**
     *  no evolve lvl + evolves to, has type 2
     */
    public Pokemon (String species, int dexnum, String rarity,
                    String type_1, String type_2) {
        this.species = species;
        this.dexnum = dexnum;
        this.rarity = rarity;
        this.type_1 = type_1;
        this.type_2 = type_2;
    }

    /**
     *  no evolve lvl + evolves to, no type 2
     */
    public Pokemon (String species, int dexnum, String rarity,
                    String type_1) {
        this.species = species;
        this.dexnum = dexnum;
        this.rarity = rarity;
        this.type_1 = type_1;
    }

    /**
     *  has evolve lvl + evolves to, no type 2
     */
    public Pokemon (String species, int dexnum, String rarity, int evolve_lvl,
                    String evolves_to, String type_1) {
        this.species = species;
        this.dexnum = dexnum;
        this.evolve_lvl = evolve_lvl;
        this.evolves_to = evolves_to;
        this.rarity = rarity;
        this.type_1 = type_1;
    }

    public String getSpecies() {
        return species;
    }

    public int getDexNum() {
        return dexnum;
    }

    public String getRarity() {
        return rarity;
    }

    public int getEvolveLvl() {
        return evolve_lvl;
    }

    public String getEvolvesTo() {
        return evolves_to;
    }

    public String getType1() {
        return type_1;
    }

    public String getType2() {
        return type_2;
    }
}
