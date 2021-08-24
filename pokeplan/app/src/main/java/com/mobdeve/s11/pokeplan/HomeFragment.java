package com.mobdeve.s11.pokeplan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ArrayList<UserPokemon> pokemonPartyList;
    private RecyclerView rvPokemonParty;
    private PokemonPartyAdapter ppAdapter;

    private ImageButton ibuserprofile;
    private ImageButton ibpokemonpc;

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

        return view;
    }

    /**
     * Initializes the layout's components
     * @param view the view of the fragment
     */
    private void initComponents(View view) {
        this.rvPokemonParty = view.findViewById(R.id.rv_home_party);
        setRecyclerView();

        this.ibuserprofile = view.findViewById(R.id.ib_home_user);
        this.ibpokemonpc = view.findViewById(R.id.ib_home_pc);
        setButtonListeners();
    }

    /**
     * Sets the adapter of the RecyclerView
     */
    private void setRecyclerView() {
        this.pokemonPartyList = UserSingleton.getUser().getUserPokemonParty();
        this.rvPokemonParty.setLayoutManager(new LinearLayoutManager(
                getActivity(),LinearLayoutManager.VERTICAL,false));
        this.ppAdapter = new PokemonPartyAdapter(this.pokemonPartyList);
        this.rvPokemonParty.setAdapter(this.ppAdapter);
    }

    /**
     * Sets the onClickListeners for all buttons
     */
    private void setButtonListeners() {
        this.ibuserprofile.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), UserProfileActivity.class);
            v.getContext().startActivity(i);
        });
        this.ibpokemonpc.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), PokemonPCActivity.class);
            v.getContext().startActivity(i);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        handler.postDelayed(() -> initComponents(getView()), 200);
    }
}