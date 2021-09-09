package com.mobdeve.s11.pokeplan.data;

import com.mobdeve.s11.pokeplan.models.Pokemon;

import java.util.ArrayList;

public class PokedexDataHelper {
    private final ArrayList<Pokemon> pokemonList;
    private final ArrayList<String> pokedexInfoList;

    public PokedexDataHelper () {
        pokemonList = new ArrayList<>();
        pokedexInfoList = new ArrayList<>();
    }

    /**
     * Manually fills the Pokedex with Pokemon.
     * @return the list of all Pokemon
     */
    public ArrayList<Pokemon> fillPokedex() {
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

        return pokemonList;
    }

    /**
     * Manually fills the Pokedex with Pokedex Entries.
     * @return the list of all Pokemon Entries
     */
    public ArrayList<String> fillPokedexInfo () {
        pokedexInfoList.add("Bulbasaur can be seen napping in bright sunlight. There is a seed on its back. By soaking up the sun's rays, the seed grows progressively larger.");
        pokedexInfoList.add("There is a bud on this Pokémon's back. To support its weight, Ivysaur's legs and trunk grow thick and strong. If it starts spending more time lying in the sunlight, it's a sign that the bud will bloom into a large flower soon.");
        pokedexInfoList.add("There is a large flower on Venusaur's back. The flower is said to take on vivid colors if it gets plenty of nutrition and sunlight. The flower's aroma soothes the emotions of people.");

        pokedexInfoList.add("The flame that burns at the tip of its tail is an indication of its emotions. The flame wavers when Charmander is enjoying itself. If the Pokémon becomes enraged, the flame burns fiercely.");
        pokedexInfoList.add("Charmeleon mercilessly destroys its foes using its sharp claws. If it encounters a strong foe, it turns aggressive. In this excited state, the flame at the tip of its tail flares with a bluish white color.");
        pokedexInfoList.add("Charizard flies around the sky in search of powerful opponents. It breathes fire of such great heat that it melts anything. However, it never turns its fiery breath on any opponent weaker than itself.");

        pokedexInfoList.add("Squirtle's shell is not merely used for protection. The shell's rounded shape and the grooves on its surface help minimize resistance in water, enabling this Pokémon to swim at high speeds.");
        pokedexInfoList.add("Its tail is large and covered with a rich, thick fur. The tail becomes increasingly deeper in color as Wartortle ages. The scratches on its shell are evidence of this Pokémon's toughness as a battler.");
        pokedexInfoList.add("Blastoise has water spouts that protrude from its shell. The water spouts are very accurate. They can shoot bullets of water with enough accuracy to strike empty cans from a distance of over 160 feet.");

        pokedexInfoList.add("In battle, Chikorita waves its leaf around to keep the foe at bay. However, a sweet fragrance also wafts from the leaf, becalming the battling Pokémon and creating a cozy, friendly atmosphere all around.");
        pokedexInfoList.add("Bayleef’s neck is ringed by curled-up leaves. Inside each tubular leaf is a small shoot of a tree. The fragrance of this shoot makes people peppy.");
        pokedexInfoList.add("The fragrance of Meganium’s flower soothes and calms emotions. In battle, this Pokémon gives off more of its becalming scent to blunt the foe’s fighting spirit.");

        pokedexInfoList.add("Cyndaquil protects itself by flaring up the flames on its back. The flames are vigorous if the Pokémon is angry. However, if it is tired, the flames splutter fitfully with incomplete combustion.");
        pokedexInfoList.add("Quilava keeps its foes at bay with the intensity of its flames and gusts of superheated air. This Pokémon applies its outstanding nimbleness to dodge attacks even while scorching the foe with flames.");
        pokedexInfoList.add("Typhlosion obscures itself behind a shimmering heat haze that it creates using its intensely hot flames. This Pokémon creates blazing explosive blasts that burn everything to cinders.");

        pokedexInfoList.add("Despite the smallness of its body, Totodile’s jaws are very powerful. While the Pokémon may think it is just playfully nipping, its bite has enough power to cause serious injury.");
        pokedexInfoList.add("Once Croconaw has clamped its jaws on its foe, it will absolutely not let go. Because the tips of its fangs are forked back like barbed fishhooks, they become impossible to remove when they have sunk in.");
        pokedexInfoList.add("Feraligatr intimidates its foes by opening its huge mouth. In battle, it will kick the ground hard with its thick and powerful hind legs to charge at the foe at an incredible speed.");

        pokedexInfoList.add("Treecko is cool, calm, and collected—it never panics under any situation. If a bigger foe were to glare at this Pokémon, it would glare right back without conceding an inch of ground.");
        pokedexInfoList.add("This Pokémon adeptly flies from branch to branch in trees. In a forest, no Pokémon can ever hope to catch a fleeing Grovyle however fast they may be.");
        pokedexInfoList.add("Sceptile has seeds growing on its back. They are said to be bursting with nutrients that revitalize trees. This Pokémon raises the trees in a forest with loving care.");

        pokedexInfoList.add("Torchic has a place inside its body where it keeps its flame. Give it a hug—it will be glowing with warmth. This Pokémon is covered all over by a fluffy coat of down.");
        pokedexInfoList.add("Combusken battles with the intensely hot flames it spews from its beak and with outstandingly destructive kicks. This Pokémon’s cry is very loud and distracting.");
        pokedexInfoList.add("Blaziken has incredibly strong legs—it can easily clear a 30-story building in one leap. This Pokémon’s blazing punches leave its foes scorched and blackened.");

        pokedexInfoList.add("In water, Mudkip breathes using the gills on its cheeks. If it is faced with a tight situation in battle, this Pokémon will unleash its amazing power—it can crush rocks bigger than itself.");
        pokedexInfoList.add("Marshtomp is much faster at traveling through mud than it is at swimming. This Pokémon’s hindquarters exhibit obvious development, giving it the ability to walk on just its hind legs.");
        pokedexInfoList.add("Swampert predicts storms by sensing subtle differences in the sounds of waves and tidal winds with its fins. If a storm is approaching, it piles up boulders to protect itself.");

        pokedexInfoList.add("It undertakes photosynthesis with its body, making oxygen. The leaf on its head wilts if it is thirsty.");
        pokedexInfoList.add("It lives along water in forests. In the daytime, it leaves the forest to sunbathe its treed shell.");
        pokedexInfoList.add("Small Pokémon occasionally gather on its unmoving back to begin building their nests.");

        pokedexInfoList.add("The gas made in its belly burns from its rear end. The fire burns weakly when it feels sick.");
        pokedexInfoList.add("It skillfully controls the intensity of the fire on its tail to keep its foes at an ideal distance.");
        pokedexInfoList.add("It tosses its enemies around with agility. It uses all its limbs to fight in its own unique style.");

        pokedexInfoList.add("Because it is very proud, it hates accepting food from people. Its thick down guards it from cold.");
        pokedexInfoList.add("It lives a solitary life. Its wings deliver wicked blows that can snap even the thickest of trees.");
        pokedexInfoList.add("The three horns that extend from its beak attest to its power. The leader has the biggest horns.");

        pokedexInfoList.add("They photosynthesize by bathing their tails in sunlight. When they are not feeling well, their tails droop.");
        pokedexInfoList.add("It moves along the ground as if sliding. Its swift movements befuddle its foes, and it then attacks with a vine whip.");
        pokedexInfoList.add("It can stop its opponents’ movements with just a glare. It takes in solar energy and boosts it internally.");

        pokedexInfoList.add("It can deftly dodge its foe’s attacks while shooting fireballs from its nose. It roasts berries before it eats them.");
        pokedexInfoList.add("The more it eats, the more fuel it has to make the fire in its stomach stronger. This fills it with even more power.");
        pokedexInfoList.add("It can throw a fire punch by setting its fists on fire with its fiery chin. It cares deeply about its friends.");

        pokedexInfoList.add("The scalchop on its stomach isn’t just used for battle—it can be used to break open hard berries as well.");
        pokedexInfoList.add("As a result of strict training, each Dewott learns different forms for using the scalchops.");
        pokedexInfoList.add("One swing of the sword incorporated in its armor can fell an opponent. A simple glare from one of them quiets everybody.");

        pokedexInfoList.add("When Sentret sleeps, it does so while another stands guard. The sentry wakes the others at the first sign of danger. When this Pokémon becomes separated from its pack, it becomes incapable of sleep due to fear.");
        pokedexInfoList.add("Furret has a very slim build. When under attack, it can slickly squirm through narrow spaces and get away. In spite of its short limbs, this Pokémon is very nimble and fleet.");

        pokedexInfoList.add("Zigzagoon restlessly wanders everywhere at all times. This Pokémon does so because it is very curious. It becomes interested in anything that it happens to see.");
        pokedexInfoList.add("When hunting, Linoone will make a beeline straight for the prey at a full run. While this Pokémon is capable of topping 60 mph, it has to come to a screeching halt before it can turn.");

        pokedexInfoList.add("Pidgey has an extremely sharp sense of direction. It is capable of unerringly returning home to its nest, however far it may be removed from its familiar surroundings.");
        pokedexInfoList.add("Pidgeotto claims a large area as its own territory. This Pokémon flies around, patrolling its living space. If its territory is violated, it shows no mercy in thoroughly punishing the foe with its sharp claws.");
        pokedexInfoList.add("This Pokémon has a dazzling plumage of beautifully glossy feathers. Many Trainers are captivated by the striking beauty of the feathers on its head, compelling them to choose Pidgeot as their Pokémon.");

        pokedexInfoList.add("Hoothoot has an internal organ that senses and tracks the earth’s rotation. Using this special organ, this Pokémon begins hooting at precisely the same time every day.");
        pokedexInfoList.add("Noctowl never fails at catching prey in darkness. This Pokémon owes its success to its superior vision that allows it to see in minimal light, and to its soft, supple wings that make no sound in flight.");

        pokedexInfoList.add("The web spun by Spinarak can be considered its second nervous system. It is said that this Pokémon can determine what kind of prey is touching its web just by the tiny vibrations it feels through the web’s strands.");
        pokedexInfoList.add("Ariados’s feet are tipped with tiny hooked claws that enable it to scuttle on ceilings and vertical walls. This Pokémon constricts the foe with thin and strong silk webbing.");

        pokedexInfoList.add("The long hair around its face provides an amazing radar that lets it sense subtle changes in its surroundings.");
        pokedexInfoList.add("It has black, cape-like fur that is very hard and decreases the amount of damage it receives.");
        pokedexInfoList.add("Being wrapped in its long fur is so comfortable that a person would be fine even overnight on a wintry mountain.");

        pokedexInfoList.add("When Pichu plays with others, it may short out electricity with another Pichu, creating a shower of sparks. In that event, this Pokémon will begin crying, startled by the flash of sparks.");
        pokedexInfoList.add("This Pokémon has electricity-storing pouches on its cheeks. These appear to become electrically charged during the night while Pikachu sleeps. It occasionally discharges electricity when it is dozy after waking up.");
        pokedexInfoList.add("This Pokémon exudes a weak electrical charge from all over its body that makes it take on a slight glow in darkness. Raichu plants its tail in the ground to discharge electricity.");

        pokedexInfoList.add("Azurill’s tail is large and bouncy. It is packed full of the nutrients this Pokémon needs to grow. Azurill can be seen bouncing and playing on its big, rubbery tail.");
        pokedexInfoList.add("When fishing for food at the edge of a fast-running stream, Marill wraps its tail around the trunk of a tree. This Pokémon’s tail is flexible and configured to stretch.");
        pokedexInfoList.add("Azumarill’s long ears are indispensable sensors. By focusing its hearing, this Pokémon can identify what kinds of prey are around, even in rough and fast-running rivers.");

        pokedexInfoList.add("Plusle always acts as a cheerleader for its partners. Whenever a teammate puts out a good effort in battle, this Pokémon shorts out its body to create the crackling noises of sparks to show its joy.");
        pokedexInfoList.add("Minun loves to cheer on its partner in battle. It gives off sparks from its body while it is doing so. If its partner is in trouble, this Pokémon gives off increasing amounts of sparks.");
        pokedexInfoList.add("It makes fur balls that crackle with static electricity. It stores them with berries in tree holes.");
        pokedexInfoList.add("The energy made in its cheeks’ electric pouches is stored inside its membrane and released while it is gliding.");

        pokedexInfoList.add("As its energy, Togepi uses the positive emotions of compassion and pleasure exuded by people and Pokémon. This Pokémon stores up feelings of happiness inside its shell, then shares them with others.");
        pokedexInfoList.add("Togetic is said to be a Pokémon that brings good fortune. When the Pokémon spots someone who is pure of heart, it is said to appear and share its happiness with that person.");
        pokedexInfoList.add("As everyone knows, it visits peaceful regions, bringing them gifts of kindness and sweet blessings.");

        pokedexInfoList.add("It carefully carries a round, white rock that it thinks is an egg. It’s bothered by how curly its hair looks.");
        pokedexInfoList.add("Chansey lays nutritionally excellent eggs on an everyday basis. The eggs are so delicious, they are easily and eagerly devoured by even those people who have lost their appetite.");
        pokedexInfoList.add("Blissey senses sadness with its fluffy coat of fur. If it does so, this Pokémon will rush over to a sad person, no matter how far away, to share a Lucky Egg that brings a smile to any face.");

        pokedexInfoList.add("Mareep’s fluffy coat of wool rubs together and builds a static charge. The more static electricity is charged, the more brightly the lightbulb at the tip of its tail glows.");
        pokedexInfoList.add("Flaaffy’s wool quality changes so that it can generate a high amount of static electricity with a small amount of wool. The bare and slick parts of its hide are shielded against electricity.");
        pokedexInfoList.add("Ampharos gives off so much light that it can be seen even from space. People in the old days used the light of this Pokémon to send signals back and forth with others far away.");

        pokedexInfoList.add("Chinchou lets loose positive and negative electrical charges from its two antennas to make its prey faint. This Pokémon flashes its electric lights to exchange signals with others.");
        pokedexInfoList.add("Lanturn is known to emit light. If you peer down into the dark sea from a ship at night, you can sometimes see this Pokémon’s light rising from the depths where it swims. It gives the sea an appearance of a starlit night.");

        pokedexInfoList.add("Aron has a body of steel. With one all-out charge, this Pokémon can demolish even a heavy dump truck. The destroyed dump truck then becomes a handy meal for the Pokémon.");
        pokedexInfoList.add("Lairon feeds on iron contained in rocks and water. It makes its nest on mountains where iron ore is buried. As a result, the Pokémon often clashes with humans mining the iron ore.");
        pokedexInfoList.add("Aggron is protective of its environment. If its mountain is ravaged by a landslide or a fire, this Pokémon will haul topsoil to the area, plant trees, and beautifully restore its own territory.");

        pokedexInfoList.add("They attach themselves to large-bodied Pokémon and absorb static electricity, which they store in an electric pouch.");
        pokedexInfoList.add("They employ an electrically charged web to trap their prey. While it is immobilized by shock, they leisurely consume it.");

        pokedexInfoList.add("Inside Vulpix’s body burns a flame that never goes out. During the daytime, when the temperatures rise, this Pokémon releases flames from its mouth to prevent its body from growing too hot.");
        pokedexInfoList.add("Legend has it that Ninetales came into being when nine wizards possessing sacred powers merged into one. This Pokémon is highly intelligent—it can understand human speech.");

        pokedexInfoList.add("Growlithe has a superb sense of smell. Once it smells anything, this Pokémon won’t forget the scent, no matter what. It uses its advanced olfactory sense to determine the emotions of other living things.");
        pokedexInfoList.add("Arcanine is known for its high speed. It is said to be capable of running over 6,200 miles in a single day and night. The fire that blazes wildly within this Pokémon’s body is its source of power.");

        pokedexInfoList.add("Zubat avoids sunlight because exposure causes it to become unhealthy. During the daytime, it stays in caves or under the eaves of old houses, sleeping while hanging upside down.");
        pokedexInfoList.add("Golbat bites down on prey with its four fangs and drinks the victim’s blood. It becomes active on inky dark moonless nights, flying around to attack people and Pokémon.");
        pokedexInfoList.add("If this Pokémon is flying by fluttering only a pair of wings on either the forelegs or hind legs, it’s proof that Crobat has been flying a long distance. It switches the wings it uses if it is tired.");

        pokedexInfoList.add("Its habitat is dark forests and caves. It emits ultrasonic waves from its nose to learn about its surroundings.");
        pokedexInfoList.add("It shakes its tail vigorously when it emits ultrasonic waves strong enough to reduce concrete to rubble.");

        pokedexInfoList.add("Swablu has light and fluffy wings that are like cottony clouds. This Pokémon is not frightened of people. It lands on the heads of people and sits there like a cotton-fluff hat.");
        pokedexInfoList.add("Altaria dances and wheels through the sky among billowing, cotton-like clouds. By singing melodies in its crystal-clear voice, this Pokémon makes its listeners experience dreamy wonderment.");

        pokedexInfoList.add("When it senses danger, it perks up its ears. On cold nights, it sleeps with its head tucked into its fur.");
        pokedexInfoList.add("The ears appear to be delicate. If they are touched roughly, it kicks with its graceful legs.");

        pokedexInfoList.add("It uses the shapes of auras, which change according to emotion, to communicate with others.");
        pokedexInfoList.add("By reading the auras of all things, it can tell how others are feeling from over half a mile away.");

        pokedexInfoList.add("Absol has the ability to foretell the coming of natural disasters. It lives in a harsh, rugged mountain environment. This Pokémon very rarely ventures down from the mountains.");

        pokedexInfoList.add("Murkrow was feared and loathed as the alleged bearer of ill fortune. This Pokémon shows strong interest in anything that sparkles or glitters. It will even try to steal rings from women.");
        pokedexInfoList.add("If one utters a deep cry, many Murkrow gather quickly. For this, it is called “Summoner of Night.”");

        pokedexInfoList.add("Misdreavus frightens people with a creepy, sobbing cry. The Pokémon apparently uses its red spheres to absorb the fearful feelings of foes and turn them into nutrition.");
        pokedexInfoList.add("It chants incantations. While they usually torment targets, some chants bring happiness.");

        pokedexInfoList.add("The energy that burns inside it enables it to move, but no one has yet been able to identify this energy.");
        pokedexInfoList.add("It is said that Golurk were ordered to protect people and Pokémon by the ancient people who made them.");

        pokedexInfoList.add("Gastly is largely composed of gaseous matter. When exposed to a strong wind, the gaseous body quickly dwindles away. Groups of this Pokémon cluster under the eaves of houses to escape the ravages of wind.");
        pokedexInfoList.add("Haunter is a dangerous Pokémon. If one beckons you while floating in darkness, you must never approach it. This Pokémon will try to lick you with its tongue and steal your life away.");
        pokedexInfoList.add("Sometimes, on a dark night, your shadow thrown by a streetlight will suddenly and startlingly overtake you. It is actually a Gengar running past you, pretending to be your shadow.");

        pokedexInfoList.add("Duskull can pass through any wall no matter how thick it may be. Once this Pokémon chooses a target, it will doggedly pursue the intended victim until the break of dawn.");
        pokedexInfoList.add("Dusclops absorbs anything, however large the object may be. This Pokémon hypnotizes its foe by waving its hands in a macabre manner and by bringing its single eye to bear. The hypnotized foe is made to do Dusclops’s bidding.");
        pokedexInfoList.add("The antenna on its head captures radio waves from the world of spirits that command it to take people there.");

        pokedexInfoList.add("Trapinch is a patient hunter. It digs an inescapable pit in a desert and waits for its prey to come tumbling down. This Pokémon can go a whole week without access to any water.");
        pokedexInfoList.add("Vibrava’s wings have not yet completed the process of growing. Rather than flying long distances, they are more useful for generating ultrasonic waves by vibrating.");
        pokedexInfoList.add("Flygon whips up a sandstorm by flapping its wings. The wings create a series of notes that sound like singing. Because the “singing” is the only thing that can be heard in a sandstorm, this Pokémon is said to be the desert spirit.");

        pokedexInfoList.add("It moves along below the sand’s surface, except for its nose and eyes. A dark membrane shields its eyes from the sun.");
        pokedexInfoList.add("The special membrane covering its eyes can sense the heat of objects, so it can see its surroundings even in darkness.");
        pokedexInfoList.add("They never allow prey to escape. Their jaws are so powerful, they can crush the body of an automobile.");

        pokedexInfoList.add("They mark their territory by leaving gashes in trees with their tusks. If a tusk breaks, a new one grows in quickly.");
        pokedexInfoList.add("A broken tusk will not grow back, so it diligently sharpens its tusks on river rocks after the end of a battle.");
        pokedexInfoList.add("They are kind but can be relentless when defending territory. They challenge foes with tusks that can cut steel.");

        pokedexInfoList.add("Dratini continually molts and sloughs off its old skin. It does so because the life energy within its body steadily builds to reach uncontrollable levels.");
        pokedexInfoList.add("Dragonair stores an enormous amount of energy inside its body. It is said to alter weather conditions in its vicinity by discharging energy from the crystals on its neck and tail.");
        pokedexInfoList.add("Dragonite is capable of circling the globe in just 16 hours. It is a kindhearted Pokémon that leads lost and foundering ships in a storm to the safety of land.");

        pokedexInfoList.add("Larvitar is born deep under the ground. To come up to the surface, this Pokémon must eat its way through the soil above. Until it does so, Larvitar cannot see its parents.");
        pokedexInfoList.add("Pupitar creates a gas inside its body that it compresses and forcefully ejects to propel itself like a jet. The body is very durable—it avoids damage even if it hits solid steel.");
        pokedexInfoList.add("Tyranitar is so overwhelmingly powerful, it can bring down a whole mountain to make its nest. This Pokémon wanders about in mountains seeking new opponents to fight.");

        pokedexInfoList.add("Bagon harbors a never-ending dream of one day soaring high among the clouds. As if trying to dispel its frustration over its inability to fly, this Pokémon slams its hard head against huge rocks and shatters them into pebbles.");
        pokedexInfoList.add("Covering Shelgon’s body are outgrowths much like bones. The shell is very hard and bounces off enemy attacks. When awaiting evolution, this Pokémon hides away in a cavern.");
        pokedexInfoList.add("By evolving into Salamence, this Pokémon finally realizes its long-held dream of growing wings. To express its joy, it flies and wheels all over the sky while spouting flames from its mouth.");

        pokedexInfoList.add("Gible attacks anything that moves, and it drags whatever it catches into the crevice that is its lair. Despite the big mouth, Gible’s stomach is small.");
        pokedexInfoList.add("As it digs to expand its nest, it habitually digs up gems that it then hoards in its nest.");
        pokedexInfoList.add("Garchomp makes its home in volcanic mountains. It flies through the sky as fast as a jet airplane, hunting down as much prey as it can.");

        pokedexInfoList.add("Lacking sight, it’s unaware of its surroundings, so it bumps into things and eats anything that moves.");
        pokedexInfoList.add("While hunting for prey, Zweilous wanders its territory, its two heads often bickering over which way to go.");
        pokedexInfoList.add("There are a slew of stories about villages that were destroyed by Hydreigon. It bites anything that moves.");

        pokedexInfoList.add("Larvesta’s body is warm all over. It spouts fire from the tips of its horns to intimidate predators and scare prey.");
        pokedexInfoList.add("Volcarona scatters burning scales. Some say it does this to start fires. Others say it’s trying to rescue those that suffer in the cold.");

        pokedexInfoList.add("Mew is said to possess the genetic composition of all Pokémon. It is capable of making itself invisible at will, so it entirely avoids notice even if it approaches people.");
        pokedexInfoList.add("This Pokémon came from the future by crossing over time. It is thought that so long as Celebi appears, a bright and shining future awaits us.");
        pokedexInfoList.add("A legend states that Jirachi will make true any wish that is written on notes attached to its head when it awakens. If this Pokémon senses danger, it will fight without awakening.");
        pokedexInfoList.add("When Uxie flew, people gained the ability to solve problems. It was the birth of knowledge.");
        pokedexInfoList.add("When Mesprit flew, people learned the joy and sadness of living. It was the birth of emotions.");
        pokedexInfoList.add("When Azelf flew, people gained the determination to do things. It was the birth of willpower.");
        pokedexInfoList.add("It drifts in warm seas. It always returns to where it was born, no matter how far it may have drifted.");
        pokedexInfoList.add("It starts its life with a wondrous power that permits it to bond with any kind of Pokémon.");
        pokedexInfoList.add("It can dissolve toxins in the air to instantly transform ruined land into a lush field of flowers.");
        pokedexInfoList.add("This Pokémon brings victory. It is said that Trainers with Victini always win, regardless of the type of encounter.");
        pokedexInfoList.add("It crosses the world, running over the surfaces of oceans and rivers. It appears at scenic waterfronts.");

        return pokedexInfoList;
    }
}
