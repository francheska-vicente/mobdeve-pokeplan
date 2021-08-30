package com.mobdeve.s11.pokeplan;

import java.util.ArrayList;

public interface FirebaseCallbackPokemon {
    void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message);
}
