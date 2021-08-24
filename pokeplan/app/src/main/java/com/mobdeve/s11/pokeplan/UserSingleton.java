package com.mobdeve.s11.pokeplan;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSingleton {
    private static UserSingleton user;

    private UserDetails userDetails;
    private ArrayList<Task> ongoingTasks;
    private ArrayList<Task> completedTasks;
    private ArrayList<UserPokemon> userPokemonPC;
    private ArrayList<UserPokemon> userPokemonParty;

    private final String userID;

    private DatabaseReference mUser;
    private DatabaseReference mTask;
    private DatabaseReference mPokemon;

    private UserSingleton(){
        this.ongoingTasks = new ArrayList<>();
        this.completedTasks = new ArrayList<>();
        this.userPokemonParty = new ArrayList<>();
        this.userPokemonPC = new ArrayList<>();
        this.userDetails = new UserDetails();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pokeplan-8930c-default-rtdb.asia-southeast1.firebasedatabase.app/");

        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mUser = mDatabase.getReference("Users").child(this.userID);
        this.mTask = mDatabase.getReference("Tasks").child(this.userID);
        this.mPokemon = mDatabase.getReference("UserPokemon").child(this.userID);

        initDbUser();
        initDbTask();
        initDbPokemon();
    }

    public static UserSingleton getUser() {
        if (user == null)
            user = new UserSingleton();
        return user;
    }

    public static void removeUser() {
        user = null;
    }

    // initialize data from db
    private void initDbUser () {
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userDetails = dataSnapshot.getValue(UserDetails.class);
                userDetails.setUserName(dataSnapshot.child("userName").getValue(String.class));
                Log.d("User DB", "User's information was successfully loaded to the application.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEBUG USER ERROR: ", Integer.toString(databaseError.getCode()));
            }
        });
    }

    private void initDbTask () {
        ongoingTasks = new ArrayList<>();
        completedTasks = new ArrayList<>();

        mTask.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Task temp = ds.getValue(Task.class);
                    if(!temp.getIsFinished()) {
                        ongoingTasks.add(temp);
                    } else {
                        completedTasks.add(temp);
                    }
                }

                Log.d("Task DB", "User's task information was successfully loaded to the application.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("DEBUG TASKS ERROR: ", Integer.toString(error.getCode()));
            }
        });
    }

    private void initDbPokemon () {
        userPokemonParty = new ArrayList<>();
        userPokemonPC = new ArrayList<>();

        mPokemon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserPokemon temp = ds.getValue(UserPokemon.class);

                    userDetails.setCaught(temp.getDetails().getDexNum());
                    if (temp.isInParty()) {
                        userPokemonParty.add(temp);
                    } else {
                        userPokemonPC.add(temp);
                    }
                }

                Log.d("Pokemon DB", "User's pokemon information was successfully loaded to the application.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("DEBUG POKEMON ERROR: ", Integer.toString(error.getCode()));
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

    public void updateUser(HashMap<String, Object> hash) {
        mUser.updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    Log.d("User DB", "User Information was successfully modified.");
                } else {
                    Log.e("User DB", "There is an error encountered! " + task.getException().toString());
                }
            }
        });
    }

    public void deleteUser (String email, String password) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                if (task.isSuccessful()) {
                    user.delete();
                    Query query = mUser;
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();
                            Log.d("User DB", "User was deleted from the DB.");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Log.e("User DB", "There is an error encountered! " + error.toException().toString());
                        }
                    });

                    query = mTask;

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();
                            Log.d("Task DB", "All of the tasks of this user were deleted from the DB.");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Log.e("Task DB", "There is an error encountered! " + error.toException().toString());
                        }
                    });

                    query = mPokemon;
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();
                            Log.d("Pokemon DB", "All of the pokemons of this user were deleted from the DB.");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Log.e("Pokemon DB", "There is an error encountered! " + error.toException().toString());
                        }
                    });

                    removeUser();
                } else {
                    Log.d("USER DB", "A problem was encountered! " + task.getException().toString());
                }
            }
        });
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

    public void addOngoingTask(@NotNull Task taskCreated) {
        String key = mTask.push().getKey();
        taskCreated.setTaskID(key);

        assert key != null;
        mTask.child(key).setValue(taskCreated).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                task.isSuccessful();
                Log.d("Task DB", "Task was added to the list of ongoing tasks.");
            }
        });

        ongoingTasks.add(taskCreated);
    }

    public void moveToCompletedTask (String key) {
        HashMap <String, Object> hash = new HashMap <String, Object>();

        hash.put("isFinished", true);
        mTask.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                Log.d("Task DB", "Task was successfully moved from ongoing list to completed list.");
            }
        });

        for (int i = 0; i < ongoingTasks.size(); i++) {
            if (ongoingTasks.get(i).getTaskID().equalsIgnoreCase(key)) {
                ongoingTasks.get(i).setIsFinished(true);
                completedTasks.add(ongoingTasks.remove(i));
                break;
            }
        }

        HashMap<String, Object> hashUser = new HashMap<>();
        hash.put("completedTaskCount", userDetails.getCompletedTaskCount() + 1);
        mUser.updateChildren(hash).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                Log.d("User DB", "User's number of completed ask has been updated.");
                userDetails.addCompletedTask();
            }
        });
    }

    public void deleteTask (String key) {
        Query query = mTask.child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Log.d("Task DB", "Task was deleted from the DB.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("Task DB", "There is an error encountered! " + error.toException().toString());
            }
        });

        boolean checker = true;
        for (int i = 0; i < ongoingTasks.size() && checker; i++) {
            if (ongoingTasks.get(i).getTaskID().equalsIgnoreCase(key)) {
                ongoingTasks.remove(i);
                checker = false;
            }
        }

        for (int i = 0; i < completedTasks.size() && checker; i++) {
            if (completedTasks.get(i).getTaskID().equalsIgnoreCase(key)) {
                completedTasks.remove(i);
                checker = false;
            }
        }
    }

    public void editTask (String name, int priority, String category, CustomDate startDate,
                          CustomDate endDate, String notes, String key, String notif, boolean val, boolean isNotif) {
        HashMap <String, Object> hash = new HashMap <String, Object>();
        hash.put("taskName", name);
        hash.put("endDate", endDate);
        hash.put("startDate", startDate);
        hash.put("priority", priority);
        hash.put("category", category);
        hash.put("description", notes);
        hash.put("notifWhen", notif);
        hash.put("beforeStartTime", val);
        hash.put("isNotif", isNotif);

        mTask.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                Log.d("Task DB", "Task information was modified.");
            }
        });

        boolean checker = true;
        for (int i = 0; i < ongoingTasks.size() && checker; i++) {
            if (ongoingTasks.get(i).getTaskID().equalsIgnoreCase(key)) {
                ongoingTasks.get(i).setTaskName(name);
                ongoingTasks.get(i).setEndDate(endDate);
                ongoingTasks.get(i).setStartDate(startDate);
                ongoingTasks.get(i).setPriority(priority);
                ongoingTasks.get(i).setCategory(category);
                ongoingTasks.get(i).setDescription(notes);
                ongoingTasks.get(i).setNotifWhen(notif);
                ongoingTasks.get(i).setBeforeStartTime(val);
                ongoingTasks.get(i).setIsNotif(isNotif);
                checker = false;
            }
        }

        for (int i = 0; i < completedTasks.size() && checker; i++) {
            if (completedTasks.get(i).getTaskID().equalsIgnoreCase(key)) {
                completedTasks.get(i).setTaskName(name);
                completedTasks.get(i).setEndDate(endDate);
                completedTasks.get(i).setStartDate(startDate);
                completedTasks.get(i).setPriority(priority);
                completedTasks.get(i).setCategory(category);
                completedTasks.get(i).setDescription(notes);
                completedTasks.get(i).setNotifWhen(notif);
                completedTasks.get(i).setBeforeStartTime(val);
                completedTasks.get(i).setIsNotif(isNotif);
                checker = false;
            }
        }

    }

    // pokemons
    public void addPokemon(Pokemon details, boolean checker) {
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

        DatabaseReference temp = mPokemon.child(key);

        temp.setValue(userPokemon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Pokemon DB", "Pokemon was successfully added to the database.");
                } else {
                    Log.e("Pokemon DB", "Pokemon was not added to the database.");
                }
            }
        });

        HashMap<String, Object> hashNum = new HashMap<>();

        if (!this.userDetails.getUserPokedex().get(details.getDexNum() - 1)) {
            hashNum.put("numCaught", this.userDetails.getNumCaught() + 1);
            hashNum.put("numNotCaught", this.userDetails.getNumNotCaught() - 1);
        }

        if (checker) {
            hashNum.put("hatchedPkmnCount", userDetails.getHatchedPkmnCount() + 1);
        }

        mUser.updateChildren(hashNum).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                Log.d("User DB", "User's number of pokemons and hatched egg were updated. ");
            }
        });

        this.userDetails.setCaught(details.getDexNum());

        HashMap <String, Object> hashUser = new HashMap<>();
        hashUser.put(Integer.toString(details.getDexNum() - 1), true);
        mUser.child("userPokedex").updateChildren(hashUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                Log.d("User DB", "User's caught pokemon information was added.");
            }
        });
    }

    public void editNickname (String key, String nickname) {
        HashMap <String, Object> hash = new HashMap <String, Object>();
        hash.put("nickname", nickname);

        mPokemon.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                Log.d("Pokemon DB", "Pokemon's nickname was modified.");
            }
        });

        boolean checker = true;
        for(int i = 0; i < userPokemonPC.size() && checker; i++) {
            if (userPokemonPC.get(i).getUserPokemonID().equalsIgnoreCase(key)) {
                userPokemonPC.get(i).setNickname(nickname);
                checker = false;
            }
        }

        for(int i = 0; i < userPokemonParty.size() && checker; i++) {
            if (userPokemonParty.get(i).getUserPokemonID().equalsIgnoreCase(key)) {
                userPokemonParty.get(i).setNickname(nickname);
                checker = false;
            }
        }
    }

    public void updatePokemon (@NotNull UserPokemon pokemon) {
        HashMap <String, Object> hash = new HashMap <String, Object>();
        hash.put("details", pokemon.getPokemonDetails());
        hash.put("fedCandy", pokemon.getFedCandy());
        hash.put("level", pokemon.getLevel());

        mPokemon.child(pokemon.getUserPokemonID()).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                Log.d("Pokemon DB", "Pokemon's information was modified.");
            }
        });

        boolean checker = true;
        for(int i = 0; i < userPokemonPC.size() && checker; i++) {
            if (userPokemonPC.get(i).getUserPokemonID().equalsIgnoreCase(pokemon.getUserPokemonID())) {
                userPokemonPC.get(i).setDetails(pokemon.getPokemonDetails());
                userPokemonPC.get(i).setFedCandy(pokemon.getFedCandy());
                userPokemonPC.get(i).setLevel(pokemon.getLevel());
                checker = false;
            }
        }

        for(int i = 0; i < userPokemonParty.size() && checker; i++) {
            if (userPokemonParty.get(i).getUserPokemonID().equalsIgnoreCase(pokemon.getUserPokemonID())) {
                userPokemonParty.get(i).setDetails(pokemon.getPokemonDetails());
                userPokemonParty.get(i).setFedCandy(pokemon.getFedCandy());
                userPokemonParty.get(i).setLevel(pokemon.getLevel());
                checker = false;
            }
        }

        HashMap <String, Object> userHash = new HashMap <String, Object>();
        userHash.put("rareCandy", userDetails.getRareCandy());
        userHash.put("superCandy", userDetails.getSuperCandy());

        mUser.updateChildren(userHash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    Log.d("User DB", "User's number of candies was modified.");
                } else {
                    Log.e("User DB", "User's number of candies was not modified.");
                }
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

    public void movePokemon(String key, boolean checker) {
        HashMap <String, Object> hash = new HashMap<String, Object>();
        hash.put("inParty", checker);

        mPokemon.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    Log.d("Pokemon DB", "Pokemon was moved.");

                    if (!checker) {
                        for (int i = 0; i < userPokemonParty.size(); i++) {
                            if (userPokemonParty.get(i).getUserPokemonID().equalsIgnoreCase(key)) {
                                userPokemonPC.add(userPokemonParty.remove(i));
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < userPokemonPC.size(); i++) {
                            if (userPokemonPC.get(i).getUserPokemonID().equalsIgnoreCase(key)) {
                                userPokemonParty.add(userPokemonPC.remove(i));
                                break;
                            }
                        }
                    }
                } else {
                    Log.e("Pokemon DB", "Pokemon was not moved.");
                }
            }
        });
    }

    public void deletePokemon (UserPokemon pokemon) {
        Query query = mPokemon.child(pokemon.getUserPokemonID());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Log.d("Pokemon DB", "Pokemon was deleted from the DB.");

                if (userPokemonPC.contains(pokemon)) {
                    userPokemonPC.remove(pokemon);
                } else if (userPokemonParty.contains(pokemon)) {
                    userPokemonParty.remove(pokemon);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("Pokemon DB", "There is an error encountered! " + error.toException().toString());
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
}
