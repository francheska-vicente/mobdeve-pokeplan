package com.mobdeve.s11.pokeplan.data;

import com.mobdeve.s11.pokeplan.models.UserTask;

import java.util.ArrayList;

public interface FirebaseCallbackTask {
    void onCallbackTask(ArrayList<UserTask> list, Boolean isSuccesful, String message);
}
