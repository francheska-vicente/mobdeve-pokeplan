package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

public class Pokedex {
    private static Pokedex pokedex;
    private ArrayList<Pokemon> pokemonList;

    private Pokedex() {
        pokemonList = new ArrayList<>();
        fillPokedex();
    }

    public static Pokedex getPokedex() {
        if (pokedex == null)
            pokedex = new Pokedex();
        return pokedex;
    }

    public Pokemon getPokemon(int dexNum) {
        return pokemonList.get(dexNum-1);
    }

    public Pokemon getPokemon(String name) {
        for(int j=0; j<pokemonList.size(); j++) {
            if (pokemonList.get(j).getSpecies().equals(name))
                return pokemonList.get(j);
        }
        return null;
    }

    public ArrayList<Pokemon> getAllPokemon() {
        return pokemonList;
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

        // Azurill Line
        pokemonList.add(new Pokemon("Azurill",63,"Common",
                13,"Marill","Normal", "Fairy"));
        pokemonList.add(new Pokemon("Marill",64,"",
                18,"Azumarill", "Water", "Fairy"));
        pokemonList.add(new Pokemon("Azumarill",65,"",
                -1,"","Water", "Fairy"));

        pokemonList.add(new Pokemon("Plusle",66,"Common",
                "Electric"));
        pokemonList.add(new Pokemon("Minun",67,"Common",
                "Electric"));
        pokemonList.add(new Pokemon("Pachirisu",68,"Common",
                "Electric"));
        pokemonList.add(new Pokemon("Emolga",69,"Common",
                "Electric", "Flying"));

        // Togepi Line
        pokemonList.add(new Pokemon("Togepi",70,"Common",
                25,"Togetic","Fairy"));
        pokemonList.add(new Pokemon("Togetic",71,"",
                35,"Togekiss", "Fairy", "Flying"));
        pokemonList.add(new Pokemon("Togekiss",72,"",
                -1,"", "Fairy", "Flying"));

        // Happiny Line
        pokemonList.add(new Pokemon("Happiny",73,"Common",
                20,"Chansey","Normal"));
        pokemonList.add(new Pokemon("Chansey",74,"",
                30,"Blissey", "Normal"));
        pokemonList.add(new Pokemon("Blissey",75,"",
                -1,"", "Normal"));

        // Mareep Line
        pokemonList.add(new Pokemon("Mareep",76,"Common",
                15,"Flaaffy","Electric"));
        pokemonList.add(new Pokemon("Flaaffy",77,"",
                30,"Ampharos", "Electric"));
        pokemonList.add(new Pokemon("Ampharos",78,"",
                -1,"", "Electric"));

        // Chinchou Line
        pokemonList.add(new Pokemon("Chinchou",79,"Common",
                27,"Lanturn", "Water", "Electric"));
        pokemonList.add(new Pokemon("Lanturn",80,"",
                -1,"", "Water", "Electric"));

        // Aron Line
        pokemonList.add(new Pokemon("Aron",81,"Uncommon",
                32,"Lairon", "Steel", "Rock"));
        pokemonList.add(new Pokemon("Lairon",82,"",
                42,"Aggron", "Steel", "Rock"));
        pokemonList.add(new Pokemon("Aggron",83,"",
                -1,"", "Steel", "Rock"));

        // Joltik Line
        pokemonList.add(new Pokemon("Joltik",84,"Common",
                36,"Galvantula", "Bug", "Electric"));
        pokemonList.add(new Pokemon("Galvantula",85,"",
                -1,"", "Bug", "Electric"));

        // Vulpix Line
        pokemonList.add(new Pokemon("Vulpix",86,"Common",
                30,"Ninetales", "Fire"));
        pokemonList.add(new Pokemon("Ninetales",87,"",
                -1,"", "Fire"));

        // Growlithe Line
        pokemonList.add(new Pokemon("Growlithe",88,"Common",
                30,"Arcanine", "Fire"));
        pokemonList.add(new Pokemon("Arcanine",89,"",
                -1,"", "Fire"));

        // Zubat Line
        pokemonList.add(new Pokemon("Zubat",90,"Common",
                22,"Golbat", "Poison", "Flying"));
        pokemonList.add(new Pokemon("Golbat",91,"",
                30,"Crobat", "Poison", "Flying"));
        pokemonList.add(new Pokemon("Crobat",92,"",
                -1,"", "Poison", "Flying"));

        // Woobat Line
        pokemonList.add(new Pokemon("Woobat",93,"Common",
                25,"Swoobat", "Psychic", "Flying"));
        pokemonList.add(new Pokemon("Swoobat",94,"",
                -1,"", "Psychic", "Flying"));

        // Swablu Line
        pokemonList.add(new Pokemon("Swablu",95,"Common",
                35,"Altaria", "Normal", "Flying"));
        pokemonList.add(new Pokemon("Altaria",96,"",
                -1,"", "Dragon", "Flying"));

        // Buneary Line
        pokemonList.add(new Pokemon("Buneary",97,"Uncommon",
                25,"Lopunny", "Normal"));
        pokemonList.add(new Pokemon("Lopunny",98,"",
                -1,"", "Normal"));

        // Riolu Line
        pokemonList.add(new Pokemon("Riolu",99,"Uncommon",
                25,"Lucario", "Fighting"));
        pokemonList.add(new Pokemon("Lucario",100,"",
                -1,"", "Fighting", "Steel"));

        pokemonList.add(new Pokemon("Absol",101,"Rare",
                -1,"", "Dark"));

        // Murkrow Line
        pokemonList.add(new Pokemon("Murkrow",102,"Uncommon",
                30,"Honchkrow", "Dark", "Flying"));
        pokemonList.add(new Pokemon("Honchkrow",103,"",
                -1,"", "Dark", "Flying"));

        // Misdreavus Line
        pokemonList.add(new Pokemon("Misdreavus",104,"Uncommon",
                30,"Mismagius", "Ghost"));
        pokemonList.add(new Pokemon("Mismagius",105,"",
                -1,"", "Ghost"));

        // Golett Line
        pokemonList.add(new Pokemon("Golett",106,"Uncommon",
                43,"Golurk", "Ground", "Ghost"));
        pokemonList.add(new Pokemon("Golurk",107,"",
                -1,"", "Ground", "Ghost"));

        // Gastly Line
        pokemonList.add(new Pokemon("Gastly",108,"Uncommon",
                25,"Haunter", "Ghost", "Poison"));
        pokemonList.add(new Pokemon("Haunter",109,"",
                35,"Gengar", "Ghost", "Poison"));
        pokemonList.add(new Pokemon("Gengar",110,"",
                -1,"", "Ghost", "Poison"));

        // Duskull Line
        pokemonList.add(new Pokemon("Duskull",111,"Uncommon",
                37,"Dusclops", "Ghost"));
        pokemonList.add(new Pokemon("Dusclops",112,"",
                45,"Dusknoir", "Ghost"));
        pokemonList.add(new Pokemon("Dusknoir",113,"",
                -1,"", "Ghost"));

        // Trapinch Line
        pokemonList.add(new Pokemon("Trapinch",114,"Uncommon",
                35,"Vibrava", "Ground"));
        pokemonList.add(new Pokemon("Vibrava",115,"",
                45,"Flygon", "Ground", "Dragon"));
        pokemonList.add(new Pokemon("Flygon",116,"",
                -1,"", "Ground", "Dragon"));

        // Sandile Line
        pokemonList.add(new Pokemon("Sandile",117,"Uncommon",
                29,"Krokorok", "Ground", "Dark"));
        pokemonList.add(new Pokemon("Krokorok",118,"",
                40,"Krookodile", "Ground", "Dark"));
        pokemonList.add(new Pokemon("Krookodile",119,"",
                -1,"", "Ground", "Dark"));

        // Axew Line
        pokemonList.add(new Pokemon("Axew",120,"Rare",
                38,"Fraxure", "Dragon"));
        pokemonList.add(new Pokemon("Fraxure",121,"",
                48,"Haxorus", "Dragon"));
        pokemonList.add(new Pokemon("Haxorus",122,"",
                -1,"", "Dragon"));

        // Dratini Line
        pokemonList.add(new Pokemon("Dratini",123,"Rare",
                30,"Dragonair","Dragon"));
        pokemonList.add(new Pokemon("Dragonair",124,"",
                55,"Dragonite","Dragon"));
        pokemonList.add(new Pokemon("Dragonite",125,"",
                -1,"","Dragon", "Flying"));

        // Larvitar Line
        pokemonList.add(new Pokemon("Larvitar",126,"Rare",
                30,"Pupitar","Rock","Ground"));
        pokemonList.add(new Pokemon("Pupitar",127,"",
                55,"Tyranitar","Rock","Ground"));
        pokemonList.add(new Pokemon("Tyranitar",128,"",
                -1,"","Rock","Dark"));

        // Bagon Line
        pokemonList.add(new Pokemon("Bagon",129,"Rare",
                30,"Shelgon","Dragon"));
        pokemonList.add(new Pokemon("Shelgon",130,"",
                55,"Salamence","Dragon"));
        pokemonList.add(new Pokemon("Salamence",131,"",
                -1,"","Dragon","Flying"));

        // Gible Line
        pokemonList.add(new Pokemon("Gible",132,"Rare",
                24,"Gabite","Dragon", "Ground"));
        pokemonList.add(new Pokemon("Gabite",133,"",
                48,"Garchomp","Dragon", "Ground"));
        pokemonList.add(new Pokemon("Garchomp",134,"",
                -1,"","Dragon", "Ground"));

        // Deino Line
        pokemonList.add(new Pokemon("Deino",135,"Rare",
                50,"Zweilous","Dark", "Dragon"));
        pokemonList.add(new Pokemon("Zweilous",136,"",
                64,"Hydreigon","Dark", "Dragon"));
        pokemonList.add(new Pokemon("Hydreigon",137,"",
                -1,"","Dark", "Dragon"));

        // Larvesta Line
        pokemonList.add(new Pokemon("Larvesta",138,"Rare",
                59,"Volcarona","Bug", "Fire"));
        pokemonList.add(new Pokemon("Volcarona",139,"",
                -1,"","Bug", "Fire"));

        // Mew 
        pokemonList.add(new Pokemon("Mew",140,"Super Rare",
                "Psychic"));
        // Celebi
        pokemonList.add(new Pokemon("Celebi",141,"Super Rare",
                "Psychic","Grass"));
        // Jirachi
        pokemonList.add(new Pokemon("Jirachi",142,"Super Rare",
                "Steel","Psychic"));

        // Uxie
        pokemonList.add(new Pokemon("Uxie",143,"Super Rare",
                "Psychic"));

        // Mesprit
        pokemonList.add(new Pokemon("Mesprit",144,"Super Rare",
                "Psychic"));

        // Azelf
        pokemonList.add(new Pokemon("Azelf",145,"Super Rare",
                "Psychic"));

        // Phione
        pokemonList.add(new Pokemon("Phione",146,"Rare",
                "Water"));

        // Manaphy
        pokemonList.add(new Pokemon("Manaphy",147,"Super Rare",
                "Water"));

        // Shaymin
        pokemonList.add(new Pokemon("Shaymin",148,"Super Rare",
                "Grass"));

        // Victini
        pokemonList.add(new Pokemon("Victini",149,"Super Rare",
                "Psychic","Fire"));

        // Keldeo
        pokemonList.add(new Pokemon("Keldeo",150,"Super Rare",
                "Water","Fighting"));
    }
}
