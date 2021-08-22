package com.mobdeve.s11.pokeplan;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

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

    public static void removeUser() {
        user = null;
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
                ongoingTasks = new ArrayList<>();
                completedTasks = new ArrayList<>();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    Task temp = ds.getValue(Task.class);
                    if(!temp.getIsFinished()) {
                        ongoingTasks.add(temp);
                    } else {
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
                userPokemonParty = new ArrayList<>();
                userPokemonPC = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserPokemon temp = ds.getValue(UserPokemon.class);
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

    public void addOngoingTask(Task taskCreated) {
        String key = mTask.push().getKey();
        taskCreated.setTaskID(key);

        mTask.child(key).setValue(taskCreated).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()) {

                } else {

                }
            }
        });

    }

    public void moveToCompletedTask (String key) {
        HashMap hash = new HashMap();
        hash.put("isFinished", true);
        mTask.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {

            }
        });
    }

    public void deleteTask (String key) {
        Query query = mTask.child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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

    public void editNickname (String key, String nickname) {
        HashMap hash = new HashMap();
        hash.put("nickname", nickname);

        mPokemon.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {

            }
        });
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
    public void movePokemonToPC(String key) {
        HashMap hash = new HashMap();
        hash.put("inParty", false);

        mPokemon.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {

            }
        });
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
