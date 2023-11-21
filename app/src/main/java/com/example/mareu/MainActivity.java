package com.example.mareu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.DI.MeetingViewModelFactory;
import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingApiService;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.ui.AddMeetingFragment;
import com.example.mareu.ui.MeetingListFragment;
import com.example.mareu.ui.MeetingSharedViewModel;


import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public MeetingSharedViewModel meetingSharedViewModel;

    private MeetingRepository meetingRepository;


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.filter_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.filter_icon) {
                // Affichage du PopupMenu lorsque l'élément "filter_icon" est sélectionné
                showPopupMenu();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void showPopupMenu() {
            PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.filter_icon));
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.filter_popup_menu, popupMenu.getMenu());

            // Écouteur de clic pour gérer les éléments du menu
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.filter_by_room) {
                    // Si l'option "Filter by Room" est sélectionnée, affichage de la fenêtre de dialogue pour choisir une salle
                    showRoomSelectionDialog();
                    return true;

                }  else if (itemId == R.id.filter_by_date) {
                    // Si l'option "Filter by Date" est sélectionnée, affichage de la fenêtre de dialogue pour choisir une plage de dates
                    showDateSelectionDialog();
                    return true;

                } else if (itemId == R.id.clear_filters) {
                    // Si l'option "Clear Filters" est sélectionnée, réinitialisation des filtres
                    meetingSharedViewModel.resetFilters();
                    return true;

                }

                return false;
            });

            popupMenu.show();
        }

    private void showRoomSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selectionnez une salle"); // Titre de la fenêtre de dialogue

        // Accédez à la liste de salles depuis DummyMeetingGenerator
        String[] roomOptions = DummyMeetingGenerator.getDUMMY_ROOMS().toArray(new String[0]);


        builder.setItems(roomOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedRoom = roomOptions[which];

                // Appel de la méthode du ViewModel pour filtrer les réunions par salle
                meetingSharedViewModel.setFilterByRoom(selectedRoom);
            }
        });

        builder.setNegativeButton("Annuler", null); // Bouton Annuler
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDateSelectionDialog() {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {

            // Création d'objet Calendar pour la date sélectionnée
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);

            meetingSharedViewModel.setFilterByDate(selectedDate);

        }, year, month, day);

        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Annuler", (dialog, which) -> {
            dialog.dismiss(); // Ferme le dialogue
        });

        datePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crée une nouvelle instance de MeetingViewModelFactory
        MeetingViewModelFactory factory = new MeetingViewModelFactory(meetingRepository);

        // Utilise ViewModelProvider pour obtenir une instance de MeetingSharedViewModel en utilisant la factory créée ci-dessus
        meetingSharedViewModel = new ViewModelProvider(this, factory).get(MeetingSharedViewModel.class);

        // Obtention de la liste complète de réunions non filtrées depuis le repository
        List<Meeting> allMeetings = meetingSharedViewModel.getMeetings();



        // Utilisation du View Binding pour lier le layout de l'activité à la classe MainActivity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar); // Configuration de la Toolbar avec le View Binding

        // Si l'activité est créée pour la première fois, ajoute le fragment de la liste des réunions
        if (savedInstanceState == null) {

            // Obtient le gestionnaire de fragments pour gérer les fragments de l'activité
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Démarre une transaction de fragment pour remplacer le conteneur de fragment par le fragment "MeetingListFragment"
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new MeetingListFragment())
                    .commit();
        }

        // Ecouteur de clic sur le bouton "add_button"
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Création d'une instance du fragment "AddMeetingFragment"
                Fragment addMeetingFragment = new AddMeetingFragment();

                // Obtention du gestionnaire de fragments
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Démarrage d'une transaction de fragment pour remplacer le contenu actuel par "AddMeetingFragment"
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
