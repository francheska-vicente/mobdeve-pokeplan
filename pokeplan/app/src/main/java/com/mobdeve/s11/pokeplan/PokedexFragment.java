package com.mobdeve.s11.pokeplan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PokedexFragment extends Fragment {
    private ArrayList<Boolean> pokedex;
    private RecyclerView rvPokedex;
    private PokedexAdapter pdAdapter;

    private TextView tvcaught;

    private ProgressBar pbload;

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
        pokedex = UserSingleton.getUser().getUserDetails().getUserPokedex();
        this.rvPokedex = view.findViewById(R.id.rv_pokedex);
        this.pdAdapter = new PokedexAdapter(pokedex);
        rvPokedex.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        rvPokedex.setAdapter(pdAdapter);

        tvcaught = view.findViewById(R.id.tv_pokedex_caught);
        String caught = "Caught Pokemon: " +
                UserSingleton.getUser().getUserDetails().getNumCaught() +  "/150";
        tvcaught.setText(caught);
    }

}