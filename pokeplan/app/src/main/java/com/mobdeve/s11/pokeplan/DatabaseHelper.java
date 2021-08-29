package com.mobdeve.s11.pokeplan;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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

public class DatabaseHelper {

    private final FirebaseAuth mAuth;

    private final DatabaseReference mUser;
    private final DatabaseReference mTask;
    private final DatabaseReference mPokemon;
    private final FirebaseDatabase mDatabase;
    private final String userID;

    public DatabaseHelper () {
        this.mDatabase = FirebaseDatabase.getInstance("https://pokeplan-8930c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        this.mAuth = FirebaseAuth.getInstance();
        this.userID = this.mAuth.getCurrentUser().getUid();

        this.mUser = mDatabase.getReference("Users").child(this.userID);
        this.mPokemon = mDatabase.getReference("UserPokemon").child(this.userID);
        this.mTask = mDatabase.getReference("Tasks").child(this.userID);
    }

    public void addUser (FirebaseCallbackUser firebaseCallbackUser, UserDetails user, String password) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    mUser.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                firebaseCallbackUser.onCallbackUser(user, true, "User registered.");
                            } else {
                                firebaseCallbackUser.onCallbackUser(null, false, "Error encountered in the registration!");
                            }
                        }
                    });
                } else {
                    firebaseCallbackUser.onCallbackUser(null, true, "User was not registered!");
                }
            }
        });
    }

    public void getUserDetails (FirebaseCallbackUser firebaseCallbackUser) {
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                firebaseCallbackUser.onCallbackUser(userDetails, true, "User information successfully loaded.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEBUG USER ERROR: ", Integer.toString(databaseError.getCode()));
                firebaseCallbackUser.onCallbackUser(null, false, "User information was not loaded");
            }
        });
    }

    public void deleteUser (FirebaseCallbackUser firebaseCallbackUser, String email, String password) {
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
                            firebaseCallbackUser.onCallbackUser(null, true, "Your account was successfully deleted.");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            firebaseCallbackUser.onCallbackUser(null, false, "Your account was not deleted.");
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

                } else {
                    firebaseCallbackUser.onCallbackUser(null, false, "Your account was not deleted.");
                }
            }
        });
    }

    public void modifyUserOnDB (FirebaseCallbackUser firebaseCallbackUser, HashMap<String, Object> hashUser, String email, String password) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mUser.updateChildren(hashUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                firebaseCallbackUser.onCallbackUser(null, true, "User was successfully modified in the database.");
                            } else {
                                firebaseCallbackUser.onCallbackUser(null, false, "An error was encountered in the modification!");
                            }
                        }
                    });
                } else {
                    firebaseCallbackUser.onCallbackUser(null, false, "The email and password combination does not match any user.");
                }
            }
        });
    }

    public void updateUser (FirebaseCallbackUser firebaseCallbackUser, HashMap<String, Object> hashUser) {
        mUser.updateChildren(hashUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    firebaseCallbackUser.onCallbackUser(null, true, "User's informationw as modified.");
                } else {
                    firebaseCallbackUser.onCallbackUser(null, false, "There is an error encountered!");
                }
            }
        });
    }

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

    public void addPokemon (FirebaseCallbackPokemon firebaseCallbackPokemon, boolean checker, Pokemon details, UserDetails userDetails) {
        ArrayList<UserPokemon> pokemonParty = new ArrayList<>();
        ArrayList<UserPokemon> pokemonPC = new ArrayList<>();

        getPokemon(new FirebaseCallbackPokemon() {
            @Override
            public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                if (isSuccessful) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isInParty()) {
                            pokemonParty.add(list.get(i));
                        } else {
                            pokemonPC.add(list.get(i));
                        }
                    }
                } else {
                    firebaseCallbackPokemon.onCallbackPokemon(null, false, "User's pokemon information was not found.");
                    return;
                }
            }
        });

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
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                        ArrayList<UserPokemon> tempPokemon = new ArrayList<>(1);
                        tempPokemon.add(userPokemon);
                        if (task.isSuccessful()) {
                            HashMap <String, Object> hashUser = new HashMap<>();
                            hashUser.put(Integer.toString(details.getDexNum() - 1), true);
                            mUser.child("userPokedex").updateChildren(hashUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                                    Log.d("User DB", "User's caught pokemon information was added.");
                                }
                            });

                            HashMap<String, Object> hashNum = new HashMap<>();

                            if (!userDetails.getUserPokedex().get(details.getDexNum() - 1)) {
                                hashNum.put("numCaught", userDetails.getNumCaught() + 1);
                                hashNum.put("numNotCaught", userDetails.getNumNotCaught() - 1);
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

                            firebaseCallbackPokemon.onCallbackPokemon(tempPokemon, true, "Pokemon was successfully added to the database.");
                        } else {
                            firebaseCallbackPokemon.onCallbackPokemon(null, false, "Pokemon was not added to the database.");
                        }
                    }
                });
    }

    public void editNickname (FirebaseCallbackPokemon firebaseCallbackPokemon, String key, String nickname) {
        HashMap <String, Object> hash = new HashMap <String, Object>();
        hash.put("nickname", nickname);

        mPokemon.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if(task.isSuccessful()) {
                    firebaseCallbackPokemon.onCallbackPokemon(null, true, "Pokemon's nickname was modified.");
                } else {
                    firebaseCallbackPokemon.onCallbackPokemon(null, false, "Pokemon's nickname was not modified.");
                }
            }
        });
    }

    public void updatePokemon (FirebaseCallbackPokemon firebaseCallbackPokemon, UserPokemon pokemon, UserDetails userDetails) {
        HashMap <String, Object> hash = new HashMap <String, Object>();
        hash.put("details", pokemon.getPokemonDetails());
        hash.put("fedCandy", pokemon.getFedCandy());
        hash.put("level", pokemon.getLevel());

        ArrayList<UserPokemon> pokemons = new ArrayList<>(1);
        pokemons.add(pokemon);

        mPokemon.child(pokemon.getUserPokemonID()).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                if (task.isSuccessful()) {
                    HashMap <String, Object> userHash = new HashMap <String, Object>();
                    userHash.put("rareCandy", userDetails.getRareCandy());
                    userHash.put("superCandy", userDetails.getSuperCandy());

                    mUser.updateChildren(userHash).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {

                            if (task.isSuccessful()) {
                                firebaseCallbackPokemon.onCallbackPokemon(pokemons, true, "Pokemon's information was successfully modified.");
                            } else {
                                firebaseCallbackPokemon.onCallbackPokemon(pokemons, false, "Pokemon's information was successfully modified.");
                            }
                        }
                    });
                } else {
                    firebaseCallbackPokemon.onCallbackPokemon(pokemons, false, "Pokemon's information was successfully modified.");
                }
            }
        });
    }

    public void movePokemon(FirebaseCallbackPokemon firebaseCallbackPokemon, String key, boolean checker) {
        HashMap <String, Object> hash = new HashMap<String, Object>();
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
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                firebaseCallbackPokemon.onCallbackPokemon(null, false, "There is an error encountered!");
            }
        });
    }

    public void getTasks (FirebaseCallbackTask firebaseCallbackTask) {
        ArrayList<com.mobdeve.s11.pokeplan.Task> tasks = new ArrayList<>();
        mTask.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    com.mobdeve.s11.pokeplan.Task temp = ds.getValue(com.mobdeve.s11.pokeplan.Task.class);
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

    public void addOngoingTask(FirebaseCallbackTask firebaseCallbackTask, com.mobdeve.s11.pokeplan.Task taskCreated) {
        String key = mTask.push().getKey();
        taskCreated.setTaskID(key);

        mTask.child(key).setValue(taskCreated).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                if(task.isSuccessful()) {
                    ArrayList<com.mobdeve.s11.pokeplan.Task> tasks = new ArrayList<>();
                    tasks.add(taskCreated);
                    firebaseCallbackTask.onCallbackTask(tasks, true, "Task was added to the list of ongoing tasks.");
                } else {
                    firebaseCallbackTask.onCallbackTask(null, false, "Task was not added to the list of ongoing tasks.");
                }
            }
        });
    }

    public void moveToCompletedTask (FirebaseCallbackTask firebaseCallbackTask, String key, UserDetails user) {
        HashMap <String, Object> hash = new HashMap <String, Object>();

        hash.put("isFinished", true);
        mTask.child(key).updateChildren(hash).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task task) {
                Log.d("Task DB", "Task was successfully moved from ongoing list to completed list.");
                HashMap<String, Object> hashUser = new HashMap<>();
                hash.put("completedTaskCount", user.getCompletedTaskCount() + 1);
                mUser.updateChildren(hash).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Void> task) {
                        firebaseCallbackTask.onCallbackTask(null, true, "Task was successfully moved to completed task.");
                    }
                });
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
                if(task.isSuccessful()) {
                    firebaseCallbackTask.onCallbackTask(null, true, "Task information was modified.");
                } else {
                    firebaseCallbackTask.onCallbackTask(null, true, "Task information was not modified.");
                }
            }
        });
    }
}
