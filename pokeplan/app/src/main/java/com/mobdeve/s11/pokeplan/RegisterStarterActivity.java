package com.mobdeve.s11.pokeplan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegisterStarterActivity extends AppCompatActivity {
    private ImageButton btnregisterstartback;
    private ArrayList<ImageButton> btnspkmn;
    private ProgressBar pbLoading;

    private static final int[] BUTTON_IDS = {
            R.id.ib_pkmn1, R.id.ib_pkmn2, R.id.ib_pkmn3, R.id.ib_pkmn4, R.id.ib_pkmn5,
            R.id.ib_pkmn6, R.id.ib_pkmn7, R.id.ib_pkmn8, R.id.ib_pkmn9, R.id.ib_pkmn10,
            R.id.ib_pkmn11, R.id.ib_pkmn12, R.id.ib_pkmn13, R.id.ib_pkmn14, R.id.ib_pkmn15,
    };

    private FirebaseAuth mAuth;
    private Dialog successDialog;

    private String name;
    private String email;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_starter);

        mAuth = FirebaseAuth.getInstance();
        pbLoading = findViewById(R.id.pb_register_starter);
        pbLoading.setVisibility(View.GONE);
        initValues ();
        initBackBtn();
        initPkmnBtns();
    }

    private void initValues () {
        Intent intent = getIntent();
        this.name =  intent.getStringExtra(RegisterActivity.KEY_NAME);
        this.email = intent.getStringExtra(RegisterActivity.KEY_EMAIL);
        this.password = intent.getStringExtra(RegisterActivity.KEY_PASSWORD);
        this.username = intent.getStringExtra(RegisterActivity.KEY_USERNAME);
    }

    private void initBackBtn() {
        btnregisterstartback = findViewById(R.id.ib_register2_back);
        btnregisterstartback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), RegisterActivity.class);
                view.getContext().startActivity(i);
            }
        });
    }

    private void registerUser (int pokeNum) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            UserDetails user = new UserDetails (name, email, username, pokeNum);

                            DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://pokeplan-8930c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            DatabaseReference temp = databaseRef.child(uid);

                            temp.setValue(user).addOnCompleteListener(new OnCompleteListener<Void> () {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterStarterActivity.this, "User has been registered!",
                                                Toast.LENGTH_LONG).show();
                                        pbLoading.setVisibility(View.GONE);
                                        finish();

                                        Pokemon pokemon = Pokedex.getPokedex().getPokemon(pokeNum);
                                        Log.d("pukenum", Integer.toString(pokeNum));
                                        UserSingleton.getUser().setUserDetails(user);
                                        UserSingleton.getUser().addPokemon(pokemon);
                                        UserSingleton.removeUser();

                                        Intent intent = new Intent(RegisterStarterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(RegisterStarterActivity.this, "User has not been registered!",
                                                Toast.LENGTH_LONG).show();
                                        pbLoading.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterStarterActivity.this, task.getException().toString(),
                                    Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(RegisterStarterActivity.this, InitActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }

    private void initPkmnBtns() {
        btnspkmn = new ArrayList<ImageButton>();
        for(int id : BUTTON_IDS) {
            ImageButton button = (ImageButton)findViewById(id);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int pokeNum = Integer.valueOf((String) view.getTag ());
                    pbLoading.setVisibility(View.VISIBLE);
                    registerUser (pokeNum);
                }
            });
            btnspkmn.add(button);
        }
    }
}