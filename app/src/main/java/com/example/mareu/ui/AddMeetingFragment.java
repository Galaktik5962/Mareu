package com.example.mareu.ui;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.DI.MeetingViewModelFactory;
import com.example.mareu.MainActivity;
import com.example.mareu.R;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.databinding.FragmentAddMeetingBinding;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddMeetingFragment extends Fragment {

    private EditText input;
    private AddMeetingViewModel addMeetingViewModel;
    private MeetingSharedViewModel meetingSharedViewModel;
    private MeetingRepository meetingRepository;
    private FragmentAddMeetingBinding binding; // Déclaration de la variable pour le ViewBinding
    private String selectedRoom; // Déclarer la variable en dehors de la méthode


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Appel à la méthode toolbarAndAddButton pour masquer ou afficher la toolbar et le bouton "Add"
        ((MainActivity) requireActivity()).toolbarAndAddButtonAction(this);

        // Inflate le layout associé au fragment et le relie à la vue parente (container)
            return inflater.inflate(R.layout.fragment_add_meeting, container, false);
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Crée une nouvelle instance de MeetingViewModelFactory
        MeetingViewModelFactory factory = new MeetingViewModelFactory(meetingRepository);

        // Utilise ViewModelProvider pour obtenir une instance de AddMeetingViewModel en utilisant la factory créée
        addMeetingViewModel = new ViewModelProvider(this, factory).get(AddMeetingViewModel.class);

        meetingSharedViewModel = new ViewModelProvider(requireActivity()).get(MeetingSharedViewModel.class);

        // Lie les vues du layout du fragment à leurs éléments correspondants grâce à View Binding
        binding = FragmentAddMeetingBinding.bind(view); // Initialisation de la variable binding ici

        // Accès direct au Spinner via View Binding
        Spinner salleSpinner = binding.lieuDeLaReunion;

        // Récupération de la liste des salles depuis DummyMeetingGenerator
        List<String> salleList = DummyMeetingGenerator.getDUMMY_ROOMS();

        // Création de l'adaptateur pour le Spinner
        ArrayAdapter<String> salleAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, salleList);
        salleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configuration du Spinner avec l'adaptateur
        salleSpinner.setAdapter(salleAdapter);

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

                // Appel de la méthode validateSubject
                addMeetingViewModel.validateSubject(s.toString());
            }
        });

        addMeetingViewModel.getIsDateValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDateValid) {
                if (isDateValid) {

                    // Ne rien faire, les actions souhaitées sont effectuées à la sélection dans le DatePickerDialog

                } else {

                    // La date n'est pas valide, affichage d'un message d'erreur
                    Toast.makeText(requireContext(), "la date n'est pas valide", Toast.LENGTH_SHORT).show();

                    // Utilisation d'un Handler pour effacer la date
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            binding.dateDeLaReunion.setText(null);
                        }});
                }
            }
        });

        addMeetingViewModel.getIsTimeValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeValid) {
                if (isTimeValid) {

                    // Ne rien faire, les actions souhaitées sont effectuées à la sélection dans le TimePickerDialog

                } else {

                    Log.d("TAG", "onChanged: heure non valide");

                    // L'heure n'est pas valide, affichage d'un message d'erreur
                    Toast.makeText(requireContext(), "l'heure n'est pas valide", Toast.LENGTH_SHORT).show();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            binding.heureDeLaReunion.setText(null);
                        }});
                }
            }
        });

        addMeetingViewModel.areParticipantsValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() { // créer un livedata de list, + creer les chip sur ce live data
            @Override
            public void onChanged(Boolean areParticipantsValid) {

                if (areParticipantsValid) {

                } else {
                    // L'adresse e-mail n'est pas valide, affichage d'un message d'erreur
                    Toast.makeText(requireContext(), "Adresse e-mail non valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addMeetingViewModel.getParticipantListLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> participantList) {

                binding.chipGroupParticipants.removeAllViews();

                for (String participant : participantList) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(participant);
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Suppression du Chip du ChipGroup
                            binding.chipGroupParticipants.removeView(chip);

                            // Supprime également l'e-mail de la liste des participants via le ViewModel
                            addMeetingViewModel.removeParticipant(participant);
                        }
                    });

                    // Ajout du Chip au ChipGroup
                    binding.chipGroupParticipants.addView(chip);
                }
            }
        });

        addMeetingViewModel.getMeetingAddedSuccessfully().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean meetingAdded) {
                if (meetingAdded) {
                    Toast.makeText(requireContext(), "Réunion ajoutée avec succès", Toast.LENGTH_SHORT).show();

                    // Obtention du gestionnaire de fragments
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                    // Remplace le fragment d'ajout de réunion par le fragment de liste des réunions
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new MeetingListFragment())
                            .commit();
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

                // Récupère l'élément sélectionné
                selectedRoom = salleList.get(position);

                // Utilise les variables du ViewModel pour obtenir les valeurs des champs
                Calendar selectedDate = addMeetingViewModel.getSelectedDate();
                Calendar selectedTime = addMeetingViewModel.getSelectedTime();

                // Appel de la méthode pour valider la salle sélectionnée
                boolean isRoomValid = addMeetingViewModel.isRoomAvailable(selectedRoom, selectedDate, selectedTime);

                // Afficher le message d'erreur si la salle n'est pas valide
                if (!isRoomValid) {

                    Toast.makeText(requireContext(), "La salle n'est pas disponible", Toast.LENGTH_SHORT).show();

                    } else {

                    // La salle est valide, appeler la méthode pour valider la salle et mettre à jour l'état du formulaire

                }

                addMeetingViewModel.validateRoom(selectedRoom);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne rien faire en cas de sélection vide
            }
        });


        // Observer le LiveData isFormValidLiveData du ViewModel
        addMeetingViewModel.getIsFormValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFormValid) {

                    // Activer ou désactiver le bouton en fonction de l'état de la validation du formulaire
                    binding.buttonAddMeeting.setEnabled(isFormValid);
            }
        });


        binding.buttonAddMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Utilisation des méthodes du ViewModel pour obtenir les valeurs des champs
                String subject = addMeetingViewModel.getSelectedSubject();
                String room = addMeetingViewModel.getSelectedRoom();
                Calendar date = addMeetingViewModel.getSelectedDate();
                Calendar time = addMeetingViewModel.getSelectedTime();

                // Utilisation de la méthode du ViewModel pour obtenir la liste des participants
                List<String> participantList = addMeetingViewModel.getParticipantList();

                // Utilisation de la méthode du ViewModel pour ajouter la réunion à la liste des réunions
                addMeetingViewModel.addMeetingToMeetingList(subject, room, date, time, participantList);

                meetingSharedViewModel.applyFiltersAndUpdateList();
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtention du  gestionnaire de fragments
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new MeetingListFragment())
                        .commit();
                    }
                });

                // Écouteur de clic pour le bouton "button add mail"
                binding.buttonAddMail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddEmailDialog();
                    }
                });
            }

            // Méthode pour afficher la boîte de dialogue d'ajout d'adresse e-mail
            private void showAddEmailDialog() {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Ajouter une adresse e-mail");

                // Utilisation de la variable de classe input
                input = new EditText(requireContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                // Définition du backgroundTint pour éviter le soulignement rouge
                input.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

                builder.setView(input);

                // Bouton "Ajouter"
                builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            String email = input.getText().toString();

                            if (addMeetingViewModel.areEmailsValid(email)) {

                                // Appelez la méthode addParticipant du ViewModel pour ajouter l'e-mail à la liste.
                                addMeetingViewModel.addParticipant(email);
                                addMeetingViewModel.validateParticipants(email);
                            } else {
                                addMeetingViewModel.validateParticipants(email);
                            }
                    }
                });

                // Bouton "Retour"
                builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

            private void showDatePicker() {
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // Création d'une instance de Calendar pour stocker la date sélectionnée
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, monthOfYear, dayOfMonth);

                        // Mise à jour de la LiveData dans le ViewModel avec la date sélectionnée
                        addMeetingViewModel.validateDate(selectedCalendar);
//                        addMeetingViewModel.checkTimeAfterDate();

                        // Mise à jour de la vue de date avec la nouvelle date sélectionnée
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                        binding.dateDeLaReunion.setText(dateFormat.format(selectedCalendar.getTime()));


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

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedCalendar.set(Calendar.MINUTE, minute);

                        addMeetingViewModel.validateTime(selectedCalendar);

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                        binding.heureDeLaReunion.setText(timeFormat.format(addMeetingViewModel.getSelectedTime().getTime()));
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        }