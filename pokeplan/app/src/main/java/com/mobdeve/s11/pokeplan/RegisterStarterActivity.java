package com.mobdeve.s11.pokeplan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegisterStarterActivity extends AppCompatActivity {
    private ImageButton btnregisterstartback;
    private ArrayList<ImageButton> btnspkmn;

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

    private ActivityResultLauncher addActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();

                    name =  intent.getStringExtra(RegisterActivity.KEY_NAME);
                    email = intent.getStringExtra(RegisterActivity.KEY_EMAIL);
                    password = intent.getStringExtra(RegisterActivity.KEY_PASSWORD);
                    username = intent.getStringExtra(RegisterActivity.KEY_USERNAME);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_starter);

        mAuth = FirebaseAuth.getInstance();

        initBackBtn();
        initPkmnBtns();
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
                            Pokedex pokedex = new Pokedex();
                            Pokemon pokemon = pokedex.getPokemon(pokeNum);

                            User user = new User (name, email, username, new UserPokemon (pokemon));

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void> () {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(RegisterStarterActivity.this, "User has been registered,",
                                                        Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(RegisterStarterActivity.this, "User has not been registered,",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
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

                    registerUser (pokeNum);
                }
            });
            btnspkmn.add(button);
        }
    }
}
