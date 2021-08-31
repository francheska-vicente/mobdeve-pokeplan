package com.mobdeve.s11.pokeplan;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class Pokemon {
    private String species;
    private int dexNum;
    private String rarity;
    private int evolveLvl;
    private String evolvesTo;
    private String type1;
    private String type2;

    /**
     *  Class constructor for a pokemon that can evolve and is dual-typed.
     *  @param species the species of a pokemon
     *  @param dexnum the pokedex number of a pokemon
     *  @param rarity the rarity of a pokemon, should be one of 4 rarities or empty
     *  @param evolve_lvl the level needed for the pokemon's next evolution, should be an integer from 1-100
     *  @param evolves_to the species of the pokemon's next evolution
     *  @param type_1 the first type of the pokemon, should be one of 18 types
     *  @param type_2 the second type of the pokemon, should be one of 18 types
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
     *  Class constructor for a dual-typed pokemon that cannot evolve.
     *  @param species the species of a pokemon
     *  @param dexnum the pokedex number of a pokemon
     *  @param rarity the rarity of a pokemon, should be one of 4 rarities or empty
     *  @param type_1 the first type of the pokemon, should be one of 18 types
     *  @param type_2 the second type of the pokemon, should be one of 18 types
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
     *  Class constructor for a pokemon that cannot evolve and has only one type.
     *  @param species the species of a pokemon
     *  @param dexnum the pokedex number of a pokemon
     *  @param rarity the rarity of a pokemon, should be one of 4 rarities or empty
     *  @param type_1 the first type of the pokemon, should be one of 18 types
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
     *  Class constructor for a pokemon that can evolve but has only one type.
     *  @param species the species of a pokemon
     *  @param dexnum the pokedex number of a pokemon
     *  @param rarity the rarity of a pokemon, should be one of 4 rarities or empty
     *  @param evolve_lvl the level needed for the pokemon's next evolution, should be an integer from 1-100
     *  @param evolves_to the species of the pokemon's next evolution
     *  @param type_1 the first type of the pokemon, should be one of 18 types
     */
    public Pokemon (String species, int dexnum, String rarity, int evolve_lvl,
                    String evolves_to, String type_1) {
        this.species = species;
        this.dexNum = dexnum;
        this.evolveLvl = evolve_lvl;
        this.evolvesTo = evolves_to;
        this.rarity = rarity;
        this.type1 = type_1;
        this.type2 = "";
    }

    /**
     * Class constructor (for database use)
     */
    public Pokemon () {}

    /**
     * @return the species of the pokemon
     */
    public String getSpecies() {
        return species;
    }

    /**
     * @return the pokedex number of the pokemon
     */
    public int getDexNum() {
        return dexNum;
    }

    /**
     * @return the rarity of the pokemon
     */
    public String getRarity() {
        return rarity;
    }

    /**
     * @return the level needed for the pokemon to evolve
     */
    public int getEvolveLvl() {
        return evolveLvl;
    }

    /**
     * @return the species of the pokemon's next evolution
     */
    public String getEvolvesTo() {
        return evolvesTo;
    }

    /**
     * @return the first type of the pokemon
     */
    public String getType1() {
        return type1;
    }

    /**
     * @return the second type of the pokemon
     */
    public String getType2() {
        return type2;
    }

    /**
     * Plays the pokemon's cry or sound.
     */
    public void playPokemonCry() {
        MediaPlayer mediaPlayer = new MediaPlayer();

        // soundfiles are accessed online
        String audioUrl = "https://play.pokemonshowdown.com/audio/cries/";
        audioUrl = audioUrl +
                species.toLowerCase() + ".mp3";
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the string representation of the pokemon
     */
    @Override
    public String toString() {
        return species;
    }
}
