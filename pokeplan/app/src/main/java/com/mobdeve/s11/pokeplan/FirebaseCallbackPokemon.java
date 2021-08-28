package com.mobdeve.s11.pokeplan;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public interface FirebaseCallbackPokemon {
    void onCallbackPokemon(ArrayList<UserPokemon> list);
}
