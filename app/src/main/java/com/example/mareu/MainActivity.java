package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.mareu.ui.MeetingListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lie le layout de l'activité à la classe MainActivity
        setContentView(R.layout.activity_main);

        // Si l'activité est créée pour la première fois, ajoute le fragment de la liste des réunions
        if (savedInstanceState == null) {

            // Obtient le gestionnaire de fragments pour gérer les fragments de l'activité
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Démarre une transaction de fragment pour remplacer le conteneur de fragment par le fragment "MeetingListFragment"
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new MeetingListFragment())
                    .commit();
        }
    }
}