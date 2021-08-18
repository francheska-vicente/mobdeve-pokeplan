package com.mobdeve.s11.pokeplan;

import android.util.Log;

import java.util.ArrayList;

public class Pokedex {
    private ArrayList<Pokemon> pokemonList;

    public Pokedex() {
        pokemonList = new ArrayList<>();
        fillPokedex();
    }

    public Pokemon getPokemon(int dexNum) {
        return pokemonList.get(dexNum-1);
    }

    public ArrayList<Pokemon> getCommonPokemonList() {
        ArrayList<Pokemon> common = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Common"))
                common.add(pokemonList.get(j));
        }

        return common;
    }

    public ArrayList<Pokemon> getUncommonPokemonList() {
        ArrayList<Pokemon> uncommon = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Uncommon"))
                uncommon.add(pokemonList.get(j));
        }

        return uncommon;
    }

    public ArrayList<Pokemon> getRarePokemonList() {
        ArrayList<Pokemon> rare = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Rare"))
                rare.add(pokemonList.get(j));
        }

        return rare;
    }

    public ArrayList<Pokemon> getSuperRarePokemonList() {
        ArrayList<Pokemon> superrare = new ArrayList<>();

        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getRarity().equals("Super Rare"))
                superrare.add(pokemonList.get(j));
        }

        return superrare;
    }

    private void fillPokedex() {
        // Bulbasaur Line
        pokemonList.add(new Pokemon("Bulbasaur",1,"Uncommon",
                16,"Ivysaur","Grass","Poison"));
        pokemonList.add(new Pokemon("Ivysaur",2,"",
                32,"Venusaur","Grass","Poison"));
        pokemonList.add(new Pokemon("Venusaur",3,"",
                -1,"","Grass","Poison"));

        // Charmander Line
        pokemonList.add(new Pokemon("Charmander",4,"Uncommon",
                16,"Charmeleon","Fire"));
        pokemonList.add(new Pokemon("Charmeleon",5,"",
                36,"Charizard","Fire"));
        pokemonList.add(new Pokemon("Charizard",6,"",
                -1,"","Fire", "Flying"));

        // Squirtle Line
        pokemonList.add(new Pokemon("Squirtle",7,"Uncommon",
                16,"Wartortle","Water"));
        pokemonList.add(new Pokemon("Wartortle",8,"",
                36,"Blastoise","Water"));
        pokemonList.add(new Pokemon("Blastoise",9,"",
                -1,"","Water"));

        // Chikorita Line
        pokemonList.add(new Pokemon("Chikorita",10,"Uncommon",
                16,"Bayleaf","Grass"));
        pokemonList.add(new Pokemon("Bayleaf",11,"",
                32,"Meganium","Grass"));
        pokemonList.add(new Pokemon("Meganium",12,"",
                -1,"","Grass"));

        // Cyndaquil Line
        pokemonList.add(new Pokemon("Cyndaquil",13,"Uncommon",
                14,"Quilava","Fire"));
        pokemonList.add(new Pokemon("Quilava",14,"",
                36,"Typhlosion","Fire"));
        pokemonList.add(new Pokemon("Typhlosion",15,"",
                -1,"","Fire"));

        // Totodile Line
        pokemonList.add(new Pokemon("Totodile",16,"Uncommon",
                18,"Croconaw","Water"));
        pokemonList.add(new Pokemon("Croconaw",17,"",
                30,"Feraligatr","Water"));
        pokemonList.add(new Pokemon("Feraligatr",18,"",
                -1,"","Water"));
                
        // Treecko Line
        pokemonList.add(new Pokemon("Treecko",19,"Uncommon",
                16,"Grovyle","Grass"));
        pokemonList.add(new Pokemon("Grovyle",20,"",
                36,"Sceptile","Grass"));
        pokemonList.add(new Pokemon("Sceptile",21,"",
                -1,"","Grass"));
                
        // Torchic Line
        pokemonList.add(new Pokemon("Torchic",22,"Uncommon",
                16,"Combusken","Fire"));
        pokemonList.add(new Pokemon("Combusken",23,"",
                36,"Blaziken","Fire", "Fighting"));
        pokemonList.add(new Pokemon("Blaziken",24,"",
                -1,"","Fire", "Fighting"));

        // Mudkip Line
        pokemonList.add(new Pokemon("Mudkip",25,"Uncommon",
                16,"Marshtomp","Water"));
        pokemonList.add(new Pokemon("Marshtomp",26,"",
                36,"Swampert","Water"));
        pokemonList.add(new Pokemon("Swampert",27,"",
                -1,"","Water"));

        // Turtwig Line
        pokemonList.add(new Pokemon("Turtwig",28,"Uncommon",
                18,"Grotle","Grass"));
        pokemonList.add(new Pokemon("Grotle",29,"",
                32,"Torterra","Grass"));
        pokemonList.add(new Pokemon("Torterra",30,"",
                -1,"","Grass", "Ground"));

        // Chimchar Line
        pokemonList.add(new Pokemon("Chimchar", 31, "Uncommon",
                14, "Monferno", "Fire"));
        pokemonList.add(new Pokemon("Monferno", 32,"",
                36,"Infernape","Fire", "Fighting"));
        pokemonList.add(new Pokemon("Infernape", 33,"",
                -1,"","Fire", "Fighting"));

        // Piplup Line
        pokemonList.add(new Pokemon("Piplup", 34, "Uncommon",
                16, "Prinplup", "Water"));
        pokemonList.add(new Pokemon("Prinplup", 35,"",
                36,"Empoleon","Water"));
        pokemonList.add(new Pokemon("Empoleon", 36,"",
                -1,"","Water", "Steel"));

        // Snivy Line
        pokemonList.add(new Pokemon("Snivy", 37, "Uncommon",
                16, "Servine", "Grass"));
        pokemonList.add(new Pokemon("Servine", 38,"",
                36,"Serperior","Grass"));
        pokemonList.add(new Pokemon("Serperior", 39,"",
                -1,"","Grass"));

        // Tepig Line
        pokemonList.add(new Pokemon("Tepig", 40, "Uncommon",
                17, "Pignite", "Fire"));
        pokemonList.add(new Pokemon("Pignite", 41,"",
                36,"Emboar","Fire", "Fighting"));
        pokemonList.add(new Pokemon("Emboar", 42,"",
                -1,"","Fire", "Fighting"));

        // Oshawott Line
        pokemonList.add(new Pokemon("Oshawott", 43, "Uncommon",
                17, "Dewott", "Water"));
        pokemonList.add(new Pokemon("Dewott", 44,"",
                36,"Samurott","Water"));
        pokemonList.add(new Pokemon("Samurott", 45,"",
                -1,"","Water"));

        // Sentret Line
        pokemonList.add(new Pokemon("Sentret", 46, "Common",
                15, "Furret", "Normal"));
        pokemonList.add(new Pokemon("Furret", 47,"",
                -1,"","Normal"));

        // Zigzagoon Line
        pokemonList.add(new Pokemon("Zigzagoon",48,"Common",
                20,"Linoone","Normal"));
        pokemonList.add(new Pokemon("Linoone",49,"",
                -1,"","Normal"));
        
        // Pidgey Line
        pokemonList.add(new Pokemon("Pidgey",50,"Common",
                18,"Pidgeotto","Normal","Flying"));
        pokemonList.add(new Pokemon("Pidgeotto",51,"",
                36,"Pidgeot","Normal","Flying"));
        pokemonList.add(new Pokemon("Pidgeot",52,"",
                -1,"","Normal","Flying"));

        // Hoothoot Line
        pokemonList.add(new Pokemon("Hoothoot",53,"Common",
                20,"Noctowl","Normal","Flying"));
        pokemonList.add(new Pokemon("Noctowl",54,"",
                -1,"","Normal","Flying"));

        // Spinarak Line
        pokemonList.add(new Pokemon("Spinarak",55,"Common",
                22,"Ariados","Bug","Poison"));
        pokemonList.add(new Pokemon("Ariados",56,"",
                -1,"","Bug","Poison"));

        // Lillipup Line
        pokemonList.add(new Pokemon("Lillipup",57,"Common",
                16,"Herdier","Normal"));
        pokemonList.add(new Pokemon("Herdier",58,"",
                32,"Stoutland","Normal"));
        pokemonList.add(new Pokemon("Stoutland",59,"",
                -1,"","Normal"));

        // Pichu Line
        pokemonList.add(new Pokemon("Pichu",60,"Common",
                20,"Pikachu","Electric"));
        pokemonList.add(new Pokemon("Pikachu",61,"",
                30,"Raichu","Electric"));
        pokemonList.add(new Pokemon("Raichu",62,"",
                -1,"","Electric"));

        // Dratini Line
        pokemonList.add(new Pokemon("Dratini",123,"Rare",
                30,"Dragonair","Dragon"));

        // Larvitar Line
        pokemonList.add(new Pokemon("Larvitar",126,"Rare",
                30,"Pupitar","Rock","Ground"));

        // Bagon Line
        pokemonList.add(new Pokemon("Bagon",129,"Rare",
                30,"Shelgon","Dragon"));

        // Mew
        pokemonList.add(new Pokemon("Mew",140,"Super Rare",
                "Psychic"));
        // Celebi
        pokemonList.add(new Pokemon("Celebi",141,"Super Rare",
                "Psychic","Grass"));
        // Jirachi
        pokemonList.add(new Pokemon("Jirachi",142,"Super Rare",
                "Steel","Psychic"));
    }
}
