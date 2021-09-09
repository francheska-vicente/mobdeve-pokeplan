package com.mobdeve.s11.pokeplan.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s11.pokeplan.R;
import com.mobdeve.s11.pokeplan.adapters.PokedexAdapter;
import com.mobdeve.s11.pokeplan.data.DatabaseHelper;
import com.mobdeve.s11.pokeplan.models.UserDetails;

import java.util.ArrayList;


public class PokedexFragment extends Fragment {
    private DatabaseHelper databaseHelper;

    public PokedexFragment() {}

    public static PokedexFragment newInstance() {
        return new PokedexFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        this.databaseHelper = new DatabaseHelper(true);
        this.initInfo(view);

        return view;
    }

    /**
     * Retrieves user information from the database
     * @param view the view of the fragment
     */
    private void initInfo(View view) {
        databaseHelper.getUserDetails((userDetails, isSuccessful, message)
                -> initComponents(view, userDetails));
    }

    /**
     * Initializes and sets all layout components
     * @param view the view of the fragment
     * @param user the details of the user
     */
    private void initComponents (View view, UserDetails user) {
        // sets the RecyclerView for the Pokedex
        ArrayList<Boolean> pokedex = user.getUserPokedex();
        RecyclerView rvPokedex = view.findViewById(R.id.rv_pokedex);
        PokedexAdapter pdAdapter = new PokedexAdapter(pokedex);
        rvPokedex.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        rvPokedex.setAdapter(pdAdapter);

        // sets the number of caught pokemon
        TextView tvcaught = view.findViewById(R.id.tv_pokedex_caught);
        String caught = "Caught Pokemon: " +
                user.getNumCaught() +  "/150";
        tvcaught.setText(caught);
    }
}