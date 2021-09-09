package com.mobdeve.s11.pokeplan.data;

import com.mobdeve.s11.pokeplan.models.UserDetails;

public interface FirebaseCallbackUser {
    void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message);
}
