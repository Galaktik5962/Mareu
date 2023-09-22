package com.example.mareu.ui;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.DI.DI;
import com.example.mareu.DI.MeetingViewModelFactory;
import com.example.mareu.MainActivity;
import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddMeetingFragment extends Fragment {

    private AddMeetingViewModel viewModel;

    private List<String> salleList = new ArrayList<>();  // Liste des salles (10 options)

    private FragmentAddMeetingBinding binding; // Déclarez la variable pour le ViewBinding

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Appeler la méthode toolbarAndAddButton pour masquer la toolbar et le bouton "Add"
        ((MainActivity) requireActivity()).toolbarAndAddButtonAction(this);

        // Inflate le layout associé au fragment et le relie à la vue parente (container)

            return inflater.inflate(R.layout.fragment_add_meeting, container, false);
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Crée une instance du ViewModel associée à ce fragment
        viewModel = new ViewModelProvider(this, new MeetingViewModelFactory(DI.checkIfRepositoryExists())).get(AddMeetingViewModel.class);

        // Lie les vues du layout du fragment à leurs éléments correspondants grâce à View Binding
        binding = FragmentAddMeetingBinding.bind(view); // Initialisez la variable binding ici

        // Accès direct au Spinner via View Binding
        Spinner salleSpinner = binding.lieuDeLaReunion;

        // Remplissez la liste des salles (salle 1 à salle 10)
        for (int i = 1; i <= 10; i++) {
            salleList.add("Room " + i);
        }

        // Création de l'adaptateur pour le Spinner
        ArrayAdapter<String> salleAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, salleList);
        salleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configuration du Spinner avec l'adaptateur
        salleSpinner.setAdapter(salleAdapter);

        viewModel.getSubjectOfMeeting().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String subject) {
                // Mettez à jour le TextView du sujet de la réunion avec la nouvelle valeur
                binding.sujetDeLaReunion.setText(subject);
            }
        });

        binding.sujetDeLaReunion.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Ne rien faire après la modification du texte
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ne rien faire avant la modification du texte
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Permet de placer le curseur à la fin du texte, même lorsque l'on change d'input et que l'on revient sur celui-ci
                binding.sujetDeLaReunion.setSelection(s.length());

                // Vérifiez si la nouvelle valeur est différente de l'ancienne
                if (!TextUtils.equals(s.toString(), viewModel.getSubjectOfMeeting().getValue())) {
                    viewModel.setSubjectOfMeeting(s.toString());
                }
            }
        });

        viewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                if (calendar != null) {
                    // Mettez à jour le champ de date avec la date sélectionnée au format souhaité
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                    binding.dateDeLaReunion.setText(dateFormat.format(calendar.getTime()));
                }
            }
        });

        viewModel.getSelectedTime().observe(getViewLifecycleOwner(), new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                if (calendar != null) {
                    // Mettez à jour le champ d'heure avec l'heure sélectionnée au format souhaité
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                    binding.heureDeLaReunion.setText(timeFormat.format(calendar.getTime()));
                }
            }
        });

        // Observer pour mettre à jour le LiveData du Spinner lorsque l'élément sélectionné change
        viewModel.getSelectedRoom().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectedRoom) {
                // Mettez à jour la sélection du Spinner
                int position = salleList.indexOf(selectedRoom);
                if (position != -1) {
                    salleSpinner.setSelection(position);
                }
            }
        });

        viewModel.getParticipantList().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String participants) {
                // Mettez à jour le TextView de la liste des participants avec la nouvelle valeur
                binding.listeDesParticipants.setText(participants);
            }
        });

        binding.listeDesParticipants.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Ne rien faire après la modification du texte
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ne rien faire avant la modification du texte
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Permet de placer le curseur à la fin du texte, même lorsque l'on change d'input et que l'on revient sur celui-ci
                binding.listeDesParticipants.setSelection(s.length());


                // Vérifiez si la nouvelle valeur est différente de l'ancienne
                if (!TextUtils.equals(s.toString(), viewModel.getParticipantList().getValue())) {
                    viewModel.setParticipantList(s.toString());
                }
            }
        });

        viewModel.getMeetingAddedSuccessfully().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean meetingAdded) {
                if (meetingAdded) {
                    Toast.makeText(requireContext(), "Réunion ajoutée avec succès", Toast.LENGTH_SHORT).show();

                    // Obtenez le gestionnaire de fragments
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                    // Remplacez le fragment d'ajout de réunion par le fragment de liste des réunions
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new MeetingListFragment()) // Remplacez par le bon fragment
                            .commit();
                    } else {
                        Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    }
                }
        });


        binding.buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        binding.buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Écouteur pour gérer la sélection du Spinner
        salleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Récupérez l'élément sélectionné et mettez à jour le LiveData du ViewModel
                String selectedRoom = salleList.get(position);
                viewModel.setSelectedRoom(selectedRoom);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne rien faire en cas de sélection vide
            }
        });

        binding.buttonAddMeeting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Récupérez les valeurs des champs de saisie
                String subject = binding.sujetDeLaReunion.getText().toString();
                String room = binding.lieuDeLaReunion.getSelectedItem().toString();
                Calendar date = viewModel.getSelectedDate().getValue();
                Calendar time = viewModel.getSelectedTime().getValue();
                String participants = binding.listeDesParticipants.getText().toString();

                // Appelez la méthode du ViewModel pour ajouter la réunion
                viewModel.addMeetingToMeetingList(subject, room, date, time, participants);
            }
        });
    }

    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Créez une instance de Calendar pour stocker la date sélectionnée
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, monthOfYear, dayOfMonth);

                // Mettez à jour la LiveData dans le ViewModel avec la date sélectionnée
                viewModel.setSelectedDate(selectedCalendar);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Créez une instance de Calendar pour stocker l'heure sélectionnée
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedCalendar.set(Calendar.MINUTE, minute);

                // Mettez à jour la LiveData dans le ViewModel avec l'heure sélectionnée
                viewModel.setSelectedTime(selectedCalendar);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }


}