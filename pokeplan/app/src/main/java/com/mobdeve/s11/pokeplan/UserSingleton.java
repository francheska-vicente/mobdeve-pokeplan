package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class UserSingleton {
    private static UserSingleton user;

    private UserDetails userDetails;
    private ArrayList<Task> ongoingTasks;
    private ArrayList<Task> completedTasks;
    private ArrayList<UserPokemon> userPokemonPC;
    private ArrayList<UserPokemon> userPokemonParty;
    private Boolean[] userPokedex;

    private final String userID;

    private DatabaseReference mUser;
    private DatabaseReference mTask;
    private DatabaseReference mPokemon;

    public static UserSingleton getUser() {
        if (user == null) {
            user = new UserSingleton();
        }
        return user;
    }

    private UserSingleton(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pokeplan-8930c-default-rtdb.asia-southeast1.firebasedatabase.app/");

        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.mUser = mDatabase.getReference("Users").child(this.userID);
        this.mTask = mDatabase.getReference("Tasks").child(this.userID);
        this.mPokemon = mDatabase.getReference("UserPokemon").child(this.userID);

        ongoingTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        userPokemonParty = new ArrayList<>();
        userPokemonPC = new ArrayList<>();
        userPokedex = new Boolean[150];
        Arrays.fill(userPokedex, false);

        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userDetails = dataSnapshot.getValue(UserDetails.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEBUG USER ERROR: ", Integer.toString(databaseError.getCode()));
            }
        });

        mTask.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    Task temp = ds.getValue(Task.class);
                    if(!temp.isFinished() && !ongoingTasks.contains(temp)) {
                        ongoingTasks.add(temp);
                    } else if (temp.isFinished() && completedTasks.contains(temp)) {
                        completedTasks.add(temp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("DEBUG TASKS ERROR: ", Integer.toString(error.getCode()));
            }
        });

        mPokemon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    Pokemon pokemon = ds.child("details").getValue(Pokemon.class);
                    String nickname = ds.child("nickname").getValue(String.class);
                    String nature = ds.child("nature").getValue(String.class);
                    Date metDate = ds.child("dMetDate").getValue(Date.class);
                    Boolean inParty = ds.child("inParty").getValue(Boolean.class);
                    int level = ds.child("level").getValue(int.class);
                    int fedCandy = ds.child("fedCandy").getValue(int.class);
                    String pokemonID = ds.child("userPokemonID").getValue(String.class);

                    UserPokemon temp = new UserPokemon(pokemon, nickname, nature, metDate, inParty, level, fedCandy, pokemonID);
                    userPokedex [temp.getPokemonDetails().getDexNum() - 1] = true;
                    if (temp.isInParty()) {
                        userPokemonParty.add(temp);
                    } else {
                        userPokemonPC.add(temp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.d("DEBUG POKEMON ERROR: ", Integer.toString(error.getCode()));
            }
        });
    }

    // user details
    public UserDetails getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(UserDetails details) {
        this.userDetails = details;
    }

    // tasks
    public ArrayList<Task> getOngoingTasks() {
        return this.ongoingTasks;
    }

    public ArrayList<Task> getCompletedTasks () {
        return this.completedTasks;
    }

    public void setOngoingTasks(ArrayList<Task> tasks) {
        this.ongoingTasks = tasks;
    }

    public void setCompletedTasks(ArrayList<Task> tasks) {
        this.completedTasks = tasks;
    }

    public String addOngoingTask(Task taskCreated) {
        String key = mTask.push().getKey();
        taskCreated.setTaskID(key);

        final boolean[] checker = new boolean[1];
        final String[] error = {"no error"};
        mTask.child(key).setValue(taskCreated).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()) {
                    checker[0] = true;
                } else {
                    checker[0] = false;
                    error[0] = task.getException().toString();
                }
            }
        });

        return error[0];
    }

    public void moveToCompletedTask (Task task) {
        this.ongoingTasks.remove(task);
        this.completedTasks.add(task);
    }

    // pokemons
    public boolean addPokemon(Pokemon details) {
        userPokedex[details.getDexNum()-1] = true;

        UserPokemon userPokemon;
        String key = mPokemon.push().getKey();

        if (userPokemonParty.size() < 6) {
            userPokemon = new UserPokemon(details, true);
            userPokemon.setUserPokemonID(key);
            userPokemonParty.add(userPokemon);
        }
        else {
            userPokemon = new UserPokemon(details, false);
            userPokemon.setUserPokemonID(key);
            userPokemonPC.add(userPokemon);
        }

        final boolean[] checker = new boolean[1];

        DatabaseReference temp = mPokemon.child(key);

        temp.setValue(userPokemon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                if (task.isSuccessful()) {
                    checker[0] = true;
                } else {
                    checker[0] = false;
                }
            }
        });

        return checker[0];
    }

    // party pokemon
    public ArrayList<UserPokemon> getUserPokemonParty() {
        return userPokemonParty;
    }
    public void setUserPokemonParty(ArrayList<UserPokemon> party) {
        this.userPokemonParty = party;
    }
    public UserPokemon getPokemonInParty(String id) {
        for(int j = 0; j < userPokemonParty.size(); j++)
            if(userPokemonParty.get(j).getUserPokemonID().equals(id))
                return userPokemonParty.get(j);

        return null;
    }
    public void movePokemonToPC(UserPokemon pkmn) {
        for(int j = 0; j < userPokemonParty.size(); j++)
            if(userPokemonParty.get(j).equals(pkmn))
                userPokemonParty.remove(j);

        userPokemonPC.add(pkmn);
    }

    // pc pokemon
    public ArrayList<UserPokemon> getUserPokemonPC() {
        return userPokemonPC;
    }
    public void setUserPokemonPC(ArrayList<UserPokemon> pc) {
        this.userPokemonPC = pc;
    }
    public UserPokemon getPokemonInPC(String id) {
        for(int j = 0; j < userPokemonPC.size(); j++)
            if(userPokemonPC.get(j).getUserPokemonID().equals(id))
                return userPokemonPC.get(j);

        return null;
    }
    public void movePokemonToParty(UserPokemon pkmn) {
        for(int j = 0; j < userPokemonPC.size(); j++)
            if(userPokemonPC.get(j).equals(pkmn))
                userPokemonPC.remove(j);

        userPokemonParty.add(pkmn);
    }

    // pokedex
    public Boolean[] getUserPokedex() {
        return userPokedex;
    }
    public void setUserPokedex(Boolean[] pokedex) {
        this.userPokedex = pokedex;
    }
    public int getNumCaught() {
        int ctr = 0;
        for(int j = 0; j < userPokedex.length; j++)
            if(userPokedex[j] == true)
                ctr++;
        return ctr;
    }
    public int getNumNotCaught() {
        int ctr = 0;
        for(int j = 0; j < userPokedex.length; j++)
            if(userPokedex[j] == false)
                ctr++;
        return ctr;
    }
}
