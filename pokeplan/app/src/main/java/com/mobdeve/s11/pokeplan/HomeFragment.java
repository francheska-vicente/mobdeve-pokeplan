package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ArrayList<UserPokemon> pokemonPartyList;
    private RecyclerView rvPokemonParty;
    private PokemonPartyAdapter ppAdapter;

    private ImageButton ibUserProfile;
    private ImageButton ibPokemonPC;

    private TextView tvOngoingTaskCtr;
    private TextView tvRareCandyCtr;
    private TextView tvSuperCandyCtr;
    private TextView tvPokedexCtr;

    FirebaseDatabase mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mTask;
    private DatabaseReference mPokemon;

    private String userID;
    private UserDetails user;
    private int ongoingTaskNum;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(view);
        Log.d("hello pare", "huhuhhu1");
        return view;
    }

    /**
     * Initializes the layout's components
     * @param view the view of the fragment
     */
    private void initComponents(View view) {
        rvPokemonParty = view.findViewById(R.id.rv_home_party);
        setRecyclerView();

        this.ibUserProfile = view.findViewById(R.id.ib_home_user);
        this.ibPokemonPC = view.findViewById(R.id.ib_home_pc);
        this.setButtonListeners();

        this.tvOngoingTaskCtr = view.findViewById(R.id.tv_home_ongoingtaskcount);
        this.tvRareCandyCtr = view.findViewById(R.id.tv_home_rarecandycount);
        this.tvSuperCandyCtr = view.findViewById(R.id.tv_home_supercandycount);
        this.tvPokedexCtr = view.findViewById(R.id.tv_home_pokedexcount);

        mDatabase = FirebaseDatabase.getInstance("https://pokeplan-8930c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.mTask = mDatabase.getReference("Tasks").child(this.userID);
        this.mUser = mDatabase.getReference("Users").child(this.userID);
        this.mPokemon = mDatabase.getReference("UserPokemon").child(this.userID);

        pokemonPartyList = new ArrayList<>(6);
        user = new UserDetails();
        ongoingTaskNum = 0;

        initPokemonParty(new FirebaseCallbackPokemon() {
            @Override
            public void onCallbackPokemon(ArrayList<UserPokemon> list) {
                ppAdapter.setPokemonParty(list);
                ppAdapter.notifyItemRangeInserted(0, list.size());

            }
        });

        initUser(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails) {
                user = userDetails;

                initTask(new FirebaseCallbackTask() {
                    @Override
                    public void onCallbackTask(ArrayList<Task> list) {
                        setCounterValues();
                    }
                });
            }
        });
    }

    private void initUser (FirebaseCallbackUser firebaseCallback) {
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserDetails userDetails = snapshot.getValue(UserDetails.class);

                firebaseCallback.onCallbackUser(userDetails);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    private void initTask (FirebaseCallbackTask firebaseCallback) {
        mTask.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<Task> ongoingTask = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Task temp = ds.getValue(Task.class);
                    if(!temp.getIsFinished()) {
                        ongoingTask.add(temp);
                        ongoingTaskNum++;
                    }
                }

                firebaseCallback.onCallbackTask(ongoingTask);
                Log.d("Task DB", "User's task information was successfully loaded to the application.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("DEBUG TASKS ERROR: ", Integer.toString(error.getCode()));
            }
        });
    }

    private void initPokemonParty (FirebaseCallbackPokemon firebaseCallback) {
        mPokemon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserPokemon temp = ds.getValue(UserPokemon.class);
                    if (temp.isInParty()) {
                        pokemonPartyList.add(temp);
                    }
                }

                firebaseCallback.onCallbackPokemon(pokemonPartyList);
                Log.d("Pokemon DB", "User's pokemon information was successfully loaded to the application.");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("DEBUG POKEMON ERROR: ", Integer.toString(error.getCode()));
            }
        });

    }


    /**
     * Sets the adapter of the RecyclerView
     */
    private void setRecyclerView() {
        this.rvPokemonParty.setLayoutManager(new LinearLayoutManager(
                getActivity(),LinearLayoutManager.VERTICAL,false));
        this.ppAdapter = new PokemonPartyAdapter();
        this.rvPokemonParty.setAdapter(this.ppAdapter);
    }

    /**
     * Sets the onClickListeners for all buttons
     */
    private void setButtonListeners() {
        this.ibUserProfile.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), UserProfileActivity.class);
            v.getContext().startActivity(i);
        });
        this.ibPokemonPC.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), PokemonPCActivity.class);
            v.getContext().startActivity(i);
        });
    }

    /**
     * Sets the counters in the Your Progress area
     */
    private void setCounterValues() {
        int ongoingtask = this.ongoingTaskNum;
        int rarecandy = this.user.getRareCandy();
        int supercandy = this.user.getSuperCandy();
        int pokedex = this.user.getNumCaught();
        Log.d("hello pare from here", Integer.toString(this.user.getRareCandy()));
        this.tvOngoingTaskCtr.setText(formatCounter(ongoingtask));
        this.tvRareCandyCtr.setText(formatCounter(rarecandy));
        this.tvSuperCandyCtr.setText(formatCounter(supercandy));
        this.tvPokedexCtr.setText(Integer.toString(pokedex));
    }

    private String formatCounter(int value) {
        if (value > 999)
            return "999+";
        return "" + value;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}