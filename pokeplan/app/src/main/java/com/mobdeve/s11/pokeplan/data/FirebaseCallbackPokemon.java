package com.mobdeve.s11.pokeplan.data;

import com.mobdeve.s11.pokeplan.models.UserPokemon;

import java.util.ArrayList;

public interface FirebaseCallbackPokemon {
    void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message);
}
