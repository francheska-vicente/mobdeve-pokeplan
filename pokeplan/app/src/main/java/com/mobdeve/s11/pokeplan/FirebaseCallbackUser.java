package com.mobdeve.s11.pokeplan;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public interface FirebaseCallbackUser {
    void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message);
}
