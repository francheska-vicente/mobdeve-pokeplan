package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

    private ProgressBar pbLoading;

    private String userID;
    private UserDetails user;
    private int ongoingTaskNum;
    private DatabaseHelper helper;

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
        helper = new DatabaseHelper();
        this.pbLoading = view.findViewById(R.id.pb_home_loading);
        this.pbLoading.setVisibility(View.VISIBLE);
        initComponents(view);
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


        user = new UserDetails();
        ongoingTaskNum = 0;

        helper.getPokemon(new FirebaseCallbackPokemon() {
            @Override
            public void onCallbackPokemon(ArrayList<UserPokemon> list, Boolean isSuccessful, String message) {
                pokemonPartyList = new ArrayList<>(6);
                if (isSuccessful) {
                    for (int i = 0; i < list.size (); i++) {
                        if (list.get(i).isInParty()) {
                            pokemonPartyList.add(list.get(i));
                        }
                    }

                    ppAdapter.setPokemonParty(pokemonPartyList);
                    ppAdapter.notifyItemRangeInserted(0, list.size());
                }
            }
        });

        helper.getUserDetails(new FirebaseCallbackUser() {
            @Override
            public void onCallbackUser(UserDetails userDetails, Boolean isSuccessful, String message) {
                if(isSuccessful) {
                    user = userDetails;

                    helper.getTasks(new FirebaseCallbackTask() {
                        @Override
                        public void onCallbackTask(ArrayList<Task> list, Boolean isSuccesful, String message) {
                            for(int i = 0; i < list.size(); i++) {
                                if (!list.get(i).getIsFinished()) {
                                    ongoingTaskNum++;
                                }
                            }
                            setCounterValues();
                            pbLoading.setVisibility(View.GONE);
                        }
                    });
                }
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
        initComponents(getView());
    }
}