package com.mobdeve.s11.pokeplan.data;

import java.util.ArrayList;

public class FaqsDataHelper {

    private final ArrayList<String> questionsList;
    private final ArrayList<String> answersList;

    public FaqsDataHelper () {
        questionsList = new ArrayList<>();
        answersList = new ArrayList<>();

        questionsList.add("1. What is the Focus Timer?");
        answersList.add("The focus timer is a timer that would help you avoid the temptation of using your device when you are trying to be productive. In using the focus timer, you would be encouraged not to touch, move or use your device during its duration as there is a Pokémon awaiting you at the end of it.");

        questionsList.add("2. What is Deep Focus Mode?");
        answersList.add("The deep focus mode is an additional feature of the focus timer. In this mode, the user may not touch or move the phone, as doing either of the two would cause the user’s egg to crack. However, choosing this mode would also increase the chances of hatching a rare Pokémon.");

        questionsList.add("3. Why did my egg turn into a sunny-side-up?");
        answersList.add("In the Normal Mode, this might be because you exited from the application, your device’s screen turned off, or you stopped the timer. Additionally, in the Deep Focus Mode, you might have touched or moved your phone.");

        questionsList.add("4. How do I hatch rarer Pokémon from the Focus Timer?");
        answersList.add("The chances of a rare Pokémon increases as the length of your timer becomes longer. ");

        questionsList.add("5. What is the Pokémon Party?");
        answersList.add("The Pokémon Party is a group of Pokémon that is visible on the Home Screen. These Pokémon can be interacted with, or more specifically, can be fed Rare Candies and Super Candies.");

        questionsList.add("6. How many Pokémon can I have in my Pokémon Party?");
        answersList.add("Like the original Pokémon games, six Pokémon can be stored at your party. Newly hatched Pokémon will automatically be sent to your PC.");

        questionsList.add("7. What is the Pokémon PC?");
        answersList.add("The Pokémon PC acts as a storage system for all your other Pokémon.");

        questionsList.add("8. What are Rare Candies and Super Candies?");
        answersList.add("In the absence of the traditional Pokémon battling system, Rare Candies and Super Candies are used to level up and evolve your Pokémon respectively. Ten (10) Rare Candies are used to level up a Pokémon, while only one (1) Super Candy is needed for evolution. Pokémon can only evolve at specific levels, each different per species.");

        questionsList.add("9. How do I get Rare Candies and Super Candies?");
        answersList.add("You can get these candies by finishing your tasks. The type and amount of candies you can get are based on the priority of your task. The higher your priority level, the more candies that you can get. It also increases your chances of getting Super Candies.");

        questionsList.add("10. What level does <pokémon> evolve at? How about Pokémon that evolve through happiness/trades?");
        answersList.add("Because of the new Candy system, Pokémon Evolution levels have been revised accordingly. The exact levels needed for evolution can be seen in the Pokedex under the specific Pokémon.");
    }

    public ArrayList <String> getQuestions () {
        return questionsList;
    }

    public ArrayList <String> getAnswers () {
        return answersList;
    }
}
