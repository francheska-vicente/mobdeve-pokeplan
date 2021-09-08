package com.mobdeve.s11.pokeplan.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.mobdeve.s11.pokeplan.models.CustomDate;
import com.mobdeve.s11.pokeplan.models.Pokemon;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.models.UserPokemon;
import com.mobdeve.s11.pokeplan.models.UserTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles all the database functions and directly accesses the Firebase realtime database.
 */
public class DatabaseHelper {

    private final FirebaseAuth mAuth;

    private  DatabaseReference mUser;
    private  DatabaseReference mTask;
    private  DatabaseReference mPokemon;
    private  FirebaseDatabase mDatabase;
    private  String userID;

    /**
     * This is the constructor of the DatabaseHelper class. It initializes the FirebaseAuth object and the different
     * DatabaseReferences if the user is logged in
     * @param loggedIn is true if the user is logged in; false otherwise
     */
    public DatabaseHelper (boolean loggedIn) {
        this.mAuth = FirebaseAuth.getInstance();

        if (loggedIn) {
            initLoggedInDB ();
        } else {
            initNotLoggedIn ();
        }
    }

    /**
     * Initializes the database and the database reference to the User, Pokemon and Task table. It also gets the UID of the
     * currently logged in user.
     */
    private void initLoggedInDB () {
        this.mDatabase = FirebaseDatabase.getInstance("https://pokeplan-8930c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.mUser = mDatabase.getReference("Users").child(this.userID);
        this.mPokemon = mDatabase.getReference("UserPokemon").child(this.userID);
        this.mTask = mDatabase.getReference("Tasks").child(this.userID);
    }

    /**
     * Initializes the database, the UID,  and the database reference to the User, Pokemon and Task table to null
     * if the user is not logged in.
     */
    private void initNotLoggedIn () {
        this.mDatabase = null;
        this.userID = null;

        this.mUser = null;
        this.mPokemon = null;
        this.mTask = null;
    }

    /**
     * Adds the user to the list of Authenticated users and his/her information to the User table.
     * @param firebaseCallbackUser is a FirebaseCallbackUser object that would handle the asynchronous database.
     * @param user is the UserDetails object that holds the user's information
     * @param password is the user's password
     */
    public void addUser (FirebaseCallbackUser firebaseCallbackUser, UserDetails user, String password) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                initLoggedInDB ();

                mUser.setValue(user).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        firebaseCallbackUser.onCallbackUser(user, true, "User registered.");
                    }
                    else {
                        firebaseCallbackUser.onCallbackUser(null, false, "Error encountered in the registration!");
                    }
                });
            }
            else {
                firebaseCallbackUser.onCallbackUser(null, false, "User was not registered!");
            }
        });

    }

    /**
     * This function gets the user's details from the Users table in the Database.
     * @param firebaseCallbackUser is a FirebaseCallbackUser object that would handle the asynchronous database.
     */
    public void getUserDetails (FirebaseCallbackUser firebaseCallbackUser) {
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                userDetails.setFinishedTaskCount(dataSnapshot.child("completedTaskCount").getValue(Integer.class));
                firebaseCallbackUser.onCallbackUser(userDetails, true, "User information successfully loaded.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEBUG USER ERROR: ", Integer.toString(databaseError.getCode()));
                firebaseCallbackUser.onCallbackUser(null, false, "User information was not loaded");
            }
        });
    }

    /**
     * If the given email matches the currently logged in user, and the given password matches the password of that user,
     * the user and his/her information (Pokemon, Users, Tasks) would be deleted from the Firebase Database.
     * @param firebaseCallbackUser is a FirebaseCallbackUser object that would handle the asynchronous database.
     * @param email is the email provided by the user
     * @param password is the password provided by the user
     */
    public void deleteUser (FirebaseCallbackUser firebaseCallbackUser, String email, String password) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // checking if the email provided matches the current user's email
        if (email.equalsIgnoreCase(user.getEmail())) {
            // checking if the credential provided is authenticated in Firebase
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.delete(); // deletes the user's authentication

                    // deleting the user's information in the Users table
                    Query query = mUser;
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();
                            firebaseCallbackUser.onCallbackUser(null, true, "Your account was successfully deleted.");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            firebaseCallbackUser.onCallbackUser(null, false, "Your account was not deleted.");
                        }
                    });

                    // deleting the user's information in the Tasks table
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

                    // deleting the user's information in the UserPokemon table
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

                } else {
                    firebaseCallbackUser.onCallbackUser(null, false, "Your account was not deleted.");
                }
            });
        } else {
            firebaseCallbackUser.onCallbackUser(null, false, "The email provided does not match the current user's email.");
        }
    }

    /**
     * Modifies the user's information in the Users table (e.g. Birthday, Username, Full name) in the FirebaseDatabase.
     * @param firebaseCallbackUser is a FirebaseCallbackUser object that would handle the asynchronous database.
     * @param hashUser is the HashMap that holds the information to be modified in the Database.
     * @param email is the email of the currently logged in user.
     * @param password is the password provided by the user.
     */
    public void modifyUserOnDB (FirebaseCallbackUser firebaseCallbackUser, HashMap<String, Object> hashUser, String email, String password) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mUser.updateChildren(hashUser).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        firebaseCallbackUser.onCallbackUser(null, true, "User was successfully modified in the database.");
                    } else {
                        firebaseCallbackUser.onCallbackUser(null, false, "An error was encountered in the modification!");
                    }
                });
            } else {
                firebaseCallbackUser.onCallbackUser(null, false, "The email and password combination does not match any user.");
            }
        });
    }

    /**
     * Modifies the user's information that is used by the application for displays (e.g. completedTaskCount, hatchedPkmncount)
     * in the Database.
     * @param firebaseCallbackUser is a FirebaseCallbackUser object that would handle the asynchronous database.
     * @param hashUser is the HashMap that holds the information to be modified in the Database.
     */
    public void updateUser (FirebaseCallbackUser firebaseCallbackUser, HashMap<String, Object> hashUser) {
        mUser.updateChildren(hashUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    firebaseCallbackUser.onCallbackUser(null, true, "User's informationw as modified.");
                } else {
                    firebaseCallbackUser.onCallbackUser(null, false, "There is an error encountered!");
                }
            }
        });
    }

    /**
     * Retrieves the list of Pokemons of the current user in the User's table.
     * @param firebaseCallbackPokemon is a FirebaseCallbackPokemon object that would handle the asynchronous database.
     */
    public void getPokemon (FirebaseCallbackPokemon firebaseCallbackPokemon) {
        ArrayList<UserPokemon> userPokemons = new ArrayList<>();

        mPokemon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserPokemon temp = ds.getValue(UserPokemon.class);

                    userPokemons.add(temp);
                }

                firebaseCallbackPokemon.onCallbackPokemon(userPokemons, true, "User's pokemon information was successfully loaded to the application.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("DEBUG POKEMON ERROR: ", Integer.toString(error.getCode()));
                firebaseCallbackPokemon.onCallbackPokemon(null, false, "There was an error encountered in getting the information.");
            }
        });
    }

    /**
     * Adds the pokemon in the UserPokemon table under a specific user.
     * @param firebaseCallbackPokemon is a FirebaseCallbackPokemon object that would handle the asynchronous database.
     * @param checker true if the pokemon is a hatched pokemon; false otherwise
     * @param details is a Pokemon object that holds the information of the pokemon
     * @param userDetails is a UserDetails object that holds the current information of the user
     */
    public void addPokemon (FirebaseCallbackPokemon firebaseCallbackPokemon, boolean checker, Pokemon details, UserDetails userDetails) {
        ArrayList<UserPokemon> pokemonParty = new ArrayList<>();
        ArrayList<UserPokemon> pokemonPC = new ArrayList<>();

        // gets the Pokemon of the current user
        getPokemon((list, isSuccessful, message) -> {
            if (isSuccessful) {
                // adds the Pokemon of the user to their respective teams
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isInParty()) {
                        pokemonParty.add(list.get(i));
                    } else {
                        pokemonPC.add(list.get(i));
                    }
                }

                UserPokemon userPokemon;
                String key = mPokemon.push().getKey();

                if (pokemonParty.size() < 6) {
                    userPokemon = new UserPokemon(details, true);
                    userPokemon.setUserPokemonID(key);
                    pokemonParty.add(userPokemon);
                }
                else {
                    userPokemon = new UserPokemon(details, false);
                    userPokemon.setUserPokemonID(key);
                    pokemonPC.add(userPokemon);
                }

                mPokemon.child(key).setValue(userPokemon)
                        .addOnCompleteListener(task -> {
                            ArrayList<UserPokemon> tempPokemon = new ArrayList<>(1);
                            tempPokemon.add(userPokemon);
                            if (task.isSuccessful()) {
                                updateUserPokemon(details, userDetails, checker);
                                firebaseCallbackPokemon.onCallbackPokemon(tempPokemon, true, "Pokemon was successfully added to the database.");
                            } else {
                                firebaseCallbackPokemon.onCallbackPokemon(null, false, "Pokemon was not added to the database.");
                            }
                        });
            }
            else {
                firebaseCallbackPokemon.onCallbackPokemon(null, false, "User's pokemon information was not found.");
                return;
            }
        });
    }

    /**
     * Updates the number of caught and number of not caught Pokemon of the User. It also the specific Pokemon to the
     * to the current user's Pokedex.
     * @param details is a Pokemon object that holds the information of the pokemon
     * @param userDetails is a UserDetails object that holds the current information of the user
     * @param checker true if the pokemon is a hatched pokemon; false otherwise
     */
    private void updateUserPokemon (Pokemon details, UserDetails userDetails, boolean checker) {
        HashMap <String, Object> hashUser = new HashMap<>();
        hashUser.put(Integer.toString(details.getDexNum() - 1), true);
        mUser.child("userPokedex").updateChildren(hashUser).addOnCompleteListener(task -> Log.d("User DB", "User's caught pokemon information was added."));

        HashMap<String, Object> hashNum = new HashMap<>();

        if (!userDetails.getUserPokedex().get(details.getDexNum() - 1)) {
            hashNum.put("numCaught", userDetails.getNumCaught() + 1);
            hashNum.put("numNotCaught", userDetails.getNumNotCaught() - 1);
        }

        if (checker) {
            userDetails.addHatchedPkmn(1);
            hashNum.put("hatchedPkmnCount", userDetails.getHatchedPkmnCount());
        }

        mUser.updateChildren(hashNum).addOnCompleteListener(task -> Log.d("User DB", "User's number of pokemons and hatched egg were updated. "));
    }

    /**
     * Edits the nickname of a specific Pokemon of the current user in the Database.
     * @param firebaseCallbackPokemon is a FirebaseCallbackPokemon object that would handle the asynchronous database.
     * @param key is the key of the UserPokemon that would be edited
     * @param nickname is the new nickname of the UserPokemon
     */
    public void editNickname (FirebaseCallbackPokemon firebaseCallbackPokemon, String key, String nickname) {
        HashMap <String, Object> hash = new HashMap<>();
        hash.put("nickname", nickname);

        mPokemon.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    firebaseCallbackPokemon.onCallbackPokemon(null, true, "Pokemon's nickname was modified.");
                } else {
                    firebaseCallbackPokemon.onCallbackPokemon(null, false, "Pokemon's nickname was not modified.");
                }
            }
        });
    }

    
    public void updatePokemon (FirebaseCallbackPokemon firebaseCallbackPokemon, UserPokemon pokemon, UserDetails userDetails) {
        HashMap <String, Object> hash = new HashMap<>();
        hash.put("details", pokemon.getDetails());
        hash.put("fedCandy", pokemon.getFedCandy());
        hash.put("level", pokemon.getLevel());
        hash.put("nickname", pokemon.getNickname());
        ArrayList<UserPokemon> pokemons = new ArrayList<>(1);
        pokemons.add(pokemon);

        mPokemon.child(pokemon.getUserPokemonID()).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    HashMap <String, Object> userHash = new HashMap<>();
                    userHash.put("rareCandy", userDetails.getRareCandy());
                    userHash.put("superCandy", userDetails.getSuperCandy());

                    mUser.updateChildren(userHash).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                            if (task.isSuccessful()) {
                                firebaseCallbackPokemon.onCallbackPokemon(pokemons, true, "Pokemon's information was successfully modified.");
                            }
                            else {
                                firebaseCallbackPokemon.onCallbackPokemon(pokemons, false, "Pokemon's information was not modified.");
                            }
                        }
                    });
                } else {
                    firebaseCallbackPokemon.onCallbackPokemon(pokemons, false, "Pokemon's information was not modified.");
                }
            }
        });

        if (!userDetails.getUserPokedex().get(pokemon.getDetails().getDexNum() - 1)) {
            updateUserPokemon(pokemon.getDetails(), userDetails, false);
        }
    }

    public void movePokemon(FirebaseCallbackPokemon firebaseCallbackPokemon, String key, boolean checker) {
        HashMap <String, Object> hash = new HashMap<>();
        hash.put("inParty", checker);

        mPokemon.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    firebaseCallbackPokemon.onCallbackPokemon(null, true, "Pokemon was moved.");
                } else {
                    firebaseCallbackPokemon.onCallbackPokemon(null, false, "Pokemon was not moved.");
                }
            }
        });
    }

    public void deletePokemon (FirebaseCallbackPokemon firebaseCallbackPokemon, UserPokemon pokemon) {
        Query query = mPokemon.child(pokemon.getUserPokemonID());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Log.d("Pokemon DB", "Pokemon was deleted from the DB.");
                firebaseCallbackPokemon.onCallbackPokemon(null, true, "Pokemon was deleted from the DB.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                firebaseCallbackPokemon.onCallbackPokemon(null, false, "There is an error encountered!");
            }
        });
    }

    public void getTasks (FirebaseCallbackTask firebaseCallbackTask) {
        ArrayList<UserTask> tasks = new ArrayList<>();
        mTask.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserTask temp = ds.getValue(UserTask.class);
                    temp.setNotifRequestCode(ds.child("notifCode").getValue(Integer.class));
                    tasks.add(temp);
                }

                firebaseCallbackTask.onCallbackTask(tasks, true, "User's task information was successfully loaded to the application.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                firebaseCallbackTask.onCallbackTask(null, false, "User's task information was not loaded to the application.");
            }
        });
    }

    public void addOngoingTask(FirebaseCallbackTask firebaseCallbackTask, UserTask taskCreated) {
        String key = mTask.push().getKey();
        taskCreated.setTaskID(key);

        mTask.child(key).setValue(taskCreated).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                ArrayList<UserTask> tasks = new ArrayList<>();
                tasks.add(taskCreated);
                firebaseCallbackTask.onCallbackTask(tasks, true, "Task was added to the list of ongoing tasks.");
            } else {
                firebaseCallbackTask.onCallbackTask(null, false, "Task was not added to the list of ongoing tasks.");
            }
        });
    }

    public void moveToCompletedTask (FirebaseCallbackTask firebaseCallbackTask, String key, UserDetails user) {
        HashMap <String, Object> hash = new HashMap<>();

        hash.put("isFinished", true);
        mTask.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                Log.d("Task DB", "Task was successfully moved from ongoing list to completed list.");
                user.addCompletedTask(1);
                hash.put("completedTaskCount", user.getCompletedTaskCount());
                mUser.updateChildren(hash).addOnCompleteListener(task1 ->
                        firebaseCallbackTask.onCallbackTask(null, true, "Task was successfully moved to completed task."));
            }
        });
    }

    public void deleteTask (FirebaseCallbackTask firebaseCallbackTask, String key) {
        Query query = mTask.child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                firebaseCallbackTask.onCallbackTask(null, true, "Task was sucessfully deleted.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                firebaseCallbackTask.onCallbackTask(null, false, "Task was not deleted.");
            }
        });
    }

    public void editTask (FirebaseCallbackTask firebaseCallbackTask, String name, int priority, String category, CustomDate startDate,
                          CustomDate endDate, String notes, String key, String notif, boolean val, boolean isNotif, int notifCode) {
        HashMap <String, Object> hash = new HashMap <>();
        hash.put("taskName", name);
        hash.put("endDate", endDate);
        hash.put("startDate", startDate);
        hash.put("priority", priority);
        hash.put("category", category);
        hash.put("description", notes);
        hash.put("notifWhen", notif);
        hash.put("beforeStartTime", val);
        hash.put("isNotif", isNotif);
        hash.put("notifCode", notifCode);

        mTask.child(key).updateChildren(hash).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                firebaseCallbackTask.onCallbackTask(null, true, "Task information was modified.");
            } else {
                firebaseCallbackTask.onCallbackTask(null, true, "Task information was not modified.");
            }
        });
    }

    public void removeNotif (FirebaseCallbackTask firebaseCallbackTask, String key) {
        HashMap <String, Object> hash = new HashMap <>();
        hash.put("isNotif", false);

        mTask.child(key).updateChildren(hash).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                firebaseCallbackTask.onCallbackTask(null, true, "Notification was removed.");
            } else {
                firebaseCallbackTask.onCallbackTask(null, true, "Notification was not removed.");
            }
        });
    }

    public void createNotif (FirebaseCallbackTask firebaseCallbackTask, int notifCode, String key) {
        HashMap<String, Object> hashMap = new HashMap<>();


        mTask.child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseCallbackTask.onCallbackTask(null, true, "Notification successfully created.");
                } else {
                    firebaseCallbackTask.onCallbackTask(null, false, "Notification was not created.");
                }
            }
        });
    }
}
