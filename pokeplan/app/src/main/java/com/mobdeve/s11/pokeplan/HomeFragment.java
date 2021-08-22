package com.mobdeve.s11.pokeplan;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ArrayList<UserPokemon> pokemonPartyList;
    private RecyclerView rvPokemonParty;
    private PokemonPartyAdapter ppAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
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

    private void initComponents (View view) {
        this.pokemonPartyList = UserSingleton.getUser().getUserPokemonParty();
        this.rvPokemonParty = view.findViewById(R.id.rv_home_party);
        this.rvPokemonParty.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        this.ppAdapter = new PokemonPartyAdapter(this.pokemonPartyList);
        this.rvPokemonParty.setAdapter(this.ppAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents(getView());
    }
}