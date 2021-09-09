package com.mobdeve.s11.pokeplan.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.activities.PokemonPCActivity;
import com.mobdeve.s11.pokeplan.activities.UserProfileActivity;
import com.mobdeve.s11.pokeplan.adapters.PokemonPartyAdapter;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.UserDetails;
import com.mobdeve.s11.pokeplan.models.UserPokemon;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ConstraintLayout clComponents;
    private ProgressBar pbLoading;

    private ArrayList<UserPokemon> pokemonPartyList;
    private RecyclerView rvPokemonParty;
    private PokemonPartyAdapter ppAdapter;

    private ImageButton ibUserProfile;
    private ImageButton ibPokemonPC;

    private TextView tvOngoingTaskCtr;
    private TextView tvRareCandyCtr;
    private TextView tvSuperCandyCtr;
    private TextView tvPokedexCtr;

    private UserDetails user;
    private int ongoingTaskNum;
    private DatabaseHelper helper;

    public HomeFragment() {}

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

        this.loadingScreen(view);
        return view;
    }

    /**
     * Sets the view components for when the data has not loaded yet
     * @param view the view of the fragment
     */
    private void loadingScreen(View view) {
        this.pbLoading = view.findViewById(R.id.pb_home_loading);
        this.pbLoading.setVisibility(View.VISIBLE);
        this.clComponents = view.findViewById(R.id.cl_home_components);
        this.clComponents.setVisibility(View.INVISIBLE);

        this.initComponents(view);
    }

    /**
     * Sets the view components for when the data has finished loading
     */
    private void finishLoading() {
        pbLoading.setVisibility(View.GONE);
        clComponents.setVisibility(View.VISIBLE);
    }

    /**
     * Retrieves all needed information from the database
     */
    private void initInfo(){
        helper = new DatabaseHelper(true);
        helper.getPokemon((list, isSuccessful, message) -> {
            pokemonPartyList = new ArrayList<>(6);
            if (isSuccessful) {
                for (int i = 0; i < list.size (); i++) {
                    if (list.get(i).isInParty()) {
                        pokemonPartyList.add(list.get(i));
                    }
                }
                ppAdapter.setPokemonParty(pokemonPartyList);

                helper.getUserDetails((userDetails, isSuccessful1, message1) -> {
                    if(isSuccessful1) {
                        user = userDetails;

                        helper.getTasks((list1, isSuccessful2, message2) -> {
                            ongoingTaskNum = 0;
                            for(int i = 0; i < list1.size(); i++) {
                                Log.d("hello here", list1.get(i).getTaskName() + " " + list1.get(i).getIsFinished());
                                if (!list1.get(i).getIsFinished()) {
                                    ongoingTaskNum++;
                                    Log.d("hello counting", Integer.toString(ongoingTaskNum));
                                }
                            }
                            setCounterValues();
                            finishLoading();
                        });
                    }
                });
            }
        });
    }


    /**
     * Initializes the layout's components
     * @param view the view of the fragment
     */
    private void initComponents(View view) {
        this.rvPokemonParty = view.findViewById(R.id.rv_home_party);
        this.setRecyclerView();

        this.ibUserProfile = view.findViewById(R.id.ib_home_user);
        this.ibPokemonPC = view.findViewById(R.id.ib_home_pc);
        this.setButtonListeners();

        this.tvOngoingTaskCtr = view.findViewById(R.id.tv_home_ongoingtaskcount);
        this.tvRareCandyCtr = view.findViewById(R.id.tv_home_rarecandycount);
        this.tvSuperCandyCtr = view.findViewById(R.id.tv_home_supercandycount);
        this.tvPokedexCtr = view.findViewById(R.id.tv_home_pokedexcount);

        this.user = new UserDetails();
        this.ongoingTaskNum = 0;
        this.initInfo();
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

    /**
     * Helper function to convert counter values
     * @param value the number to convert
     * @return the formatted value
     */
    private String formatCounter(int value) {
        if (value > 999)
            return "999+";
        return "" + value;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingScreen(getView());
    }
}