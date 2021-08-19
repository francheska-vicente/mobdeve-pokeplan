package com.mobdeve.s11.pokeplan;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PokedexFragment extends Fragment {
    private Boolean[] pokedex;
    private RecyclerView rvPokedex;
    private PokedexAdapter pdAdapter;

    public PokedexFragment() {
    }

    public static PokedexFragment newInstance() {
        PokedexFragment fragment = new PokedexFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents (View view) {
        User helper = new User();

        this.pokedex = helper.getUserPokedex();
        this.rvPokedex = view.findViewById(R.id.rv_pokedex);
        this.rvPokedex.setLayoutManager(new GridLayoutManager(getActivity(), 5));

        this.pdAdapter = new PokedexAdapter(this.pokedex);
        this.rvPokedex.setAdapter(this.pdAdapter);

    }
}