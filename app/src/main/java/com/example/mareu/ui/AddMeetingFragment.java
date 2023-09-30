package com.example.mareu.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

import com.example.mareu.DI.DI;
import com.example.mareu.DI.MeetingViewModelFactory;
import com.example.mareu.MainActivity;
import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddMeetingFragment extends Fragment {

    private AddMeetingViewModel viewModel;

    private List<String> salleList = new ArrayList<>();  // Liste des salles (10 options)

    private FragmentAddMeetingBinding binding; // Déclaration de la variable pour le ViewBinding

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

        // Crée une instance du ViewModel associée à ce fragment
        viewModel = new ViewModelProvider(this, new MeetingViewModelFactory(DI.checkIfRepositoryExists())).get(AddMeetingViewModel.class);

        // Lie les vues du layout du fragment à leurs éléments correspondants grâce à View Binding
        binding = FragmentAddMeetingBinding.bind(view); // Initialisation de la variable binding ici

        // Accès direct au Spinner via View Binding
        Spinner salleSpinner = binding.lieuDeLaReunion;

        // Remplissage de la liste des salles (salle 1 à salle 10)
        for (int i = 1; i <= 10; i++) {
            salleList.add("Room " + i);
        }

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
                viewModel.validateSubject(s.toString());
            }
        });

        viewModel.getIsDateValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDateValid) {
                if (isDateValid) {

                    // La date est valide, mise à jour de la date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                    binding.dateDeLaReunion.setText(dateFormat.format(viewModel.getSelectedDate().getTime()));

                } else {

                    // La date n'est pas valide, affichage d'un message d'erreur
                    Toast.makeText(requireContext(), "la date n'est pas valide", Toast.LENGTH_SHORT).show();

                    // Efface la date sélectionnée si elle n'est pas valide
                    binding.dateDeLaReunion.setText("");
                }
            }
        });

        viewModel.getIsTimeValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeValid) {
                if (isTimeValid) {

                    // L'heure est valide, mise à jour de l'heure
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                    binding.heureDeLaReunion.setText(timeFormat.format(viewModel.getSelectedTime().getTime()));

                } else {

                    // L'heure n'est pas valide, affichage d'un message d'erreur
                    Toast.makeText(requireContext(), "l'heure n'est pas valide", Toast.LENGTH_SHORT).show();

                    // Efface l'heure sélectionnée si elle n'est pas valide
                    binding.heureDeLaReunion.setText("");
                }
            }
        });

        viewModel.getIsFormValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFormValid) {
                binding.buttonAddMeeting.setEnabled(isFormValid);
            }
        });

        viewModel.areParticipantsValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean areParticipantsValid) {
                if (areParticipantsValid) {

                } else {
                    // L'adresse e-mail n'est pas valide, affichez un message d'erreur
                    Toast.makeText(requireContext(), "Adresse e-mail non valide", Toast.LENGTH_SHORT).show();
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
                // Récupérez l'élément sélectionné et appelez la méthode de validation appropriée avec la valeur sélectionnée.

                String selectedRoom = salleList.get(position);
                viewModel.validateRoom(selectedRoom);
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
                Calendar date = viewModel.getSelectedDate();
                Calendar time = viewModel.getSelectedTime();

                // Obtenez la liste des participants à partir des puces dans chipGroupParticipants
                StringBuilder participantListBuilder = new StringBuilder();
                for (int i = 0; i < binding.chipGroupParticipants.getChildCount(); i++) {
                    View childView = binding.chipGroupParticipants.getChildAt(i);
                    if (childView instanceof Chip) {
                        Chip chip = (Chip) childView;
                        String chipText = chip.getText().toString();
                        if (!TextUtils.isEmpty(chipText)) {
                            participantListBuilder.append(chipText);
                            participantListBuilder.append(", ");
                        }
                    }
                }

                // Supprimez la virgule et l'espace en trop à la fin de la liste
                String participantList = participantListBuilder.toString().trim();
                if (participantList.endsWith(",")) {
                    participantList = participantList.substring(0, participantList.length() - 1);
                }

                // Appelez la méthode du ViewModel pour ajouter la réunion
                viewModel.addMeetingToMeetingList(subject, room, date, time, participantList); // uniquement cette ligne à garder
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenez le gestionnaire de fragments
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Remplacez le fragment d'ajout de réunion par le fragment de liste des réunions
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new MeetingListFragment()) // Remplacez par le bon fragment
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

        // Créez un champ d'entrée pour l'adresse e-mail
        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        // Définir le backgroundTint pour éviter le soulignement rouge
        input.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));

        builder.setView(input);

        // Bouton "Ajouter"
        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    String email = input.getText().toString();

                    if (viewModel.areEmailsValid(email)) {
                        // L'adresse e-mail est valide, ajoutez-la en tant que Chip
                        addEmailAsChip(email);

                        // Appelez la méthode validateParticipants pour mettre à jour la validation des participants
                        viewModel.validateParticipants(email);

                    } else {

                        viewModel.validateParticipants(email);
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

    private void addEmailAsChip (String email) { // gérer ça dans l'observe d'une liste des participants // construire mes chip grâce à ma liste de mail
        ChipGroup chipGroup = binding.chipGroupParticipants;

        // Créez un nouveau Chip
        Chip chip = new Chip(requireContext());
        chip.setText(email);
        chip.setCloseIconVisible(true); // Affichez l'icône de fermeture (bouton de suppression)
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gérez la suppression de cette adresse e-mail ici
                chipGroup.removeView(chip); // Supprimez le Chip du ChipGroup
            }
        });

        // Ajoutez le Chip au ChipGroup
        chipGroup.addView(chip);

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
                viewModel.validateDate(selectedCalendar);
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
                viewModel.validateTime(selectedCalendar);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }


}