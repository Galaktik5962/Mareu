package com.example.mareu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.ui.AddMeetingFragment;
import com.example.mareu.ui.MeetingListFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Utilisez View Binding pour lier le layout de l'activité à la classe MainActivity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar); // Configurez la Toolbar avec le View Binding

        // Si l'activité est créée pour la première fois, ajoute le fragment de la liste des réunions
        if (savedInstanceState == null) {

            // Obtient le gestionnaire de fragments pour gérer les fragments de l'activité
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Démarre une transaction de fragment pour remplacer le conteneur de fragment par le fragment "MeetingListFragment"
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new MeetingListFragment())
                    .commit();
        }



        // Attachez un écouteur de clic au bouton "add_button"
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Créez une instance du fragment "AddMeetingFragment"
                Fragment addMeetingFragment = new AddMeetingFragment();

                // Obtenez le gestionnaire de fragments
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Démarrez une transaction de fragment pour remplacer le contenu actuel par "AddMeetingFragment"
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, addMeetingFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void toolbarAndAddButtonAction (Fragment fragment) {

        if (fragment instanceof AddMeetingFragment) {

            // L'utilisateur est sur le fragment d'ajout de réunion, la toolbar et le bouton "Add" sont masqués
            getSupportActionBar().hide();
            binding.addButton.setVisibility(View.GONE);

        } else {

            // L'utilisateur est sur le fragment de la liste des réunions, la toolbar et le bouton "Add" sont affichés
            getSupportActionBar().show();
            binding.addButton.setVisibility(View.VISIBLE);
        }
    }
}
