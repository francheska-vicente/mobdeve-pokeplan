package com.mobdeve.s11.pokeplan;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        initButtons(view);
        return view;
    }

    private void initComponents (View view) {
        this.pokemonPartyList = UserSingleton.getUser().getUserPokemonParty();
        this.rvPokemonParty = view.findViewById(R.id.rv_home_party);
        this.rvPokemonParty.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        this.ppAdapter = new PokemonPartyAdapter(this.pokemonPartyList);
        this.rvPokemonParty.setAdapter(this.ppAdapter);
    }

    private void initButtons(View view) {
        this.ibuserprofile = view.findViewById(R.id.ib_home_user);
        this.ibuserprofile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UserProfileActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initComponents(getView());
    }
}