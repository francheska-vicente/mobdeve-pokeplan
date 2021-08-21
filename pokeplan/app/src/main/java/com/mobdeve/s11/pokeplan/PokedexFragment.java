package com.mobdeve.s11.pokeplan;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PokedexFragment extends Fragment {
    private Boolean[] pokedex;
    private RecyclerView rvPokedex;
    private PokedexAdapter pdAdapter;

    private TextView tvcaught;

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
        this.pokedex = UserSingleton.getUser().getUserPokedex();
        this.rvPokedex = view.findViewById(R.id.rv_pokedex);
        this.rvPokedex.setLayoutManager(new GridLayoutManager(getActivity(), 5));

        this.pdAdapter = new PokedexAdapter(this.pokedex);
        this.rvPokedex.setAdapter(this.pdAdapter);

        this.tvcaught = view.findViewById(R.id.tv_pokedex_caught);
        String caught = "Caught Pokemon: " + UserSingleton.getUser().getNumCaught() +  "/150";
        tvcaught.setText(caught);
    }
}