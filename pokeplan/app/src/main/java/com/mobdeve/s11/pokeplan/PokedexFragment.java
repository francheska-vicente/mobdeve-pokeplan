package com.mobdeve.s11.pokeplan;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
        this.pbload = view.findViewById(R.id.pb_pkdex_load);
        pbload.setVisibility(View.GONE);
        pokedex = UserSingleton.getUser().getUserDetails().getUserPokedex();
        this.rvPokedex = view.findViewById(R.id.rv_pkmnpc);
        this.pdAdapter = new PokedexAdapter(pokedex);
        rvPokedex.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        rvPokedex.setAdapter(pdAdapter);

        tvcaught = view.findViewById(R.id.tv_pokedex_caught);
        String caught = "Caught Pokemon: " +
                UserSingleton.getUser().getUserDetails().getNumCaught() +  "/150";
        tvcaught.setText(caught);
    }

    private class loadPokedexTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pbload.setVisibility(View.VISIBLE);
            rvPokedex.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            rvPokedex.setAdapter(pdAdapter);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
        }

        @Override
        protected void onPostExecute(Void voids) {
            pbload.setVisibility(View.GONE);
            rvPokedex.setVisibility(View.VISIBLE);
        }
    }
}