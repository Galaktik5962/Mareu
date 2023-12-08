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

/**
 * Fragment for adding a new meeting.
 */
public class AddMeetingFragment extends Fragment {

    /**
     * This Fragment is responsible for adding a new meeting. It includes UI components
     * such as EditText, ViewModels (AddMeetingViewModel and MeetingSharedViewModel),
     * a repository for meeting data (MeetingRepository), data binding (FragmentAddMeetingBinding),
     * and a variable to store the selected meeting room (selectedRoom).
     */
    private EditText input;
    private AddMeetingViewModel addMeetingViewModel;
    private MeetingSharedViewModel meetingSharedViewModel;
    private MeetingRepository meetingRepository;
    private FragmentAddMeetingBinding binding;
    private String selectedRoom;

    /**
     * Creates and returns the view for this fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).toolbarAndAddButtonAction(this);
        return inflater.inflate(R.layout.fragment_add_meeting, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned, but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize view models and data binding
        MeetingViewModelFactory factory = new MeetingViewModelFactory(meetingRepository);
        addMeetingViewModel = new ViewModelProvider(this, factory).get(AddMeetingViewModel.class);
        meetingSharedViewModel = new ViewModelProvider(requireActivity()).get(MeetingSharedViewModel.class);
        binding = FragmentAddMeetingBinding.bind(view);

        // Set up the spinner for room selection
        Spinner salleSpinner = binding.lieuDeLaReunion;

        // Get the list of available meeting rooms from the dummy data generator
        List<String> salleList = DummyMeetingGenerator.getDUMMY_ROOMS();

        // Create an adapter for the spinner and set its layout
        ArrayAdapter<String> salleAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, salleList);
        salleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the spinner
        salleSpinner.setAdapter(salleAdapter);

        /**
         * Set up a text watcher for the meeting subject EditText.
         * The text watcher monitors changes in the input text and triggers the validation of the meeting subject.
         *
         * @param s      The editable text representing the current content of the EditText.
         * @param start  The start position of the changed part of the text.
         * @param before The length of the text that has been changed, before the change occurred.
         * @param count  The length of the new text that has been added.
         */
        binding.sujetDeLaReunion.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Set the selection of the meeting subject EditText to the end of the text
                binding.sujetDeLaReunion.setSelection(s.length());
                // Trigger the validation of the meeting subject in the ViewModel
                addMeetingViewModel.validateSubject(s.toString());
            }
        });

        /**
         * Observer for the LiveData that monitors the validity of the selected date.
         *
         * @param isDateValid Boolean indicating whether the selected date is valid.
         */
        addMeetingViewModel.getIsDateValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDateValid) {
                if (isDateValid) {
                    // Handle valid date
                } else {
                    Toast.makeText(requireContext(), "la date n'est pas valide", Toast.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            binding.dateDeLaReunion.setText(null);
                        }
                    });
                }
            }
        });

        /**
         * Observer for the LiveData representing the validation status of the meeting time.
         * This observer is responsible for handling changes in the validity of the meeting time
         * and updating the UI accordingly.
         *
         * @param isTimeValid A Boolean representing whether the meeting time is valid or not.
         *                    True indicates a valid time, and false indicates an invalid time.
         */
        addMeetingViewModel.getIsTimeValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeValid) {
                if (isTimeValid) { // faire un if !isTimeValid comme ça pas de else
                    // Handle valid time
                } else {
                    Toast.makeText(requireContext(), "l'heure n'est pas valide", Toast.LENGTH_SHORT).show();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            binding.heureDeLaReunion.setText(null);
                        }
                    });
                }
            }
        });

        /**
         * Observer for the LiveData representing the validation status of meeting participants' email addresses.
         * This observer is responsible for handling changes in the validity of participants' email addresses
         * and updating the UI accordingly.
         *
         * @param areParticipantsValid A Boolean representing whether the participants' email addresses are valid or not.
         *                             True indicates valid email addresses, and false indicates at least one invalid email address.
         */
        addMeetingViewModel.areParticipantsValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean areParticipantsValid) {
                if (areParticipantsValid) {
                    // Handle valid participants
                } else {
                    Toast.makeText(requireContext(), "Adresse e-mail non valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * Observer for the LiveData representing the list of participants in a meeting.
         * This observer is responsible for updating the UI when the list of participants changes.
         *
         * @param participantList The updated list of participants in the meeting.
         */
        addMeetingViewModel.getParticipantListLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> participantList) {
                // Remove all views from the ChipGroup to avoid duplicates
                binding.chipGroupParticipants.removeAllViews();

                // Iterate through the updated participant list and create a Chip for each participant
                for (String participant : participantList) {
                    Chip chip = new Chip(requireContext());
                    chip.setText(participant);
                    chip.setCloseIconVisible(true);

                    // Set an onClickListener to handle the removal of a participant when the close icon is clicked
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            binding.chipGroupParticipants.removeView(chip);
                            addMeetingViewModel.removeParticipant(participant);
                        }
                    });
                    binding.chipGroupParticipants.addView(chip);
                }
            }
        });

        /**
         * Observer for the LiveData representing the success of adding a meeting.
         * This observer is responsible for handling UI updates when a meeting is successfully added.
         *
         * @param meetingAdded A boolean indicating whether the meeting was added successfully or not.
         */
        addMeetingViewModel.getMeetingAddedSuccessfully().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean meetingAdded) {
                if (meetingAdded) {
                    Toast.makeText(requireContext(), "Réunion ajoutée avec succès", Toast.LENGTH_SHORT).show();

                    // Navigate to the MeetingListFragment after a successful meeting addition
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new MeetingListFragment())
                            .commit();
                }
            }
        });

        /**
         * Observer for the LiveData representing the overall validity of the meeting creation form.
         * This observer enables or disables the "Add Meeting" button based on the validity of the form.
         *
         * @param isFormValid A boolean indicating whether the entire meeting creation form is valid or not.
         */
        addMeetingViewModel.getIsFormValidLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFormValid) {
                binding.buttonAddMeeting.setEnabled(isFormValid);
            }
        });

        /**
         * Sets up the item selection listener for the room selection spinner.
         * This listener is triggered when a room is selected from the spinner.
         * It updates the selected room and validates its availability.
         *
         * @param parent   The AdapterView where the selection happened.
         * @param view     The view within the AdapterView that was clicked.
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that is selected.
         */
        salleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected room from the spinner
                selectedRoom = salleList.get(position);

                // Get the selected date and time from the ViewModel
                Calendar selectedDate = addMeetingViewModel.getSelectedDate();
                Calendar selectedTime = addMeetingViewModel.getSelectedTime();

                // Validate if the selected room is available at the chosen date and time
                boolean isRoomValid = addMeetingViewModel.isRoomAvailable(selectedRoom, selectedDate, selectedTime);

                if (!isRoomValid) {
                    Toast.makeText(requireContext(), "La salle n'est pas disponible", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle valid room
                }

                // Validate the selected room's availability at the chosen date and time
                addMeetingViewModel.validateRoom(selectedRoom, selectedDate, selectedTime);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         * Sets up a click listener for the calendar button.
         * When the button is clicked, it triggers the display of the date picker.
         *
         * @param v The view that was clicked.
         */
        binding.buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        /**
         * Sets up a click listener for the time selection button.
         * When the button is clicked, it triggers the display of a time picker.
         *
         * @param v The view that was clicked.
         */
        binding.buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        /**
         * Sets up a click listener for the "Add Meeting" button.
         * When the button is clicked, it retrieves the selected subject, room, date, time, and participant list from the ViewModel.
         * Then, it adds a new meeting to the meeting list using the ViewModel and triggers an update of the meeting list.
         * Finally, it triggers an update of the meeting list by applying filters (or not) and updating the list.
         *
         * @param v The view that was clicked.
         */
        binding.buttonAddMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the selected subject, room, date, time, and participant list from the ViewModel
                String subject = addMeetingViewModel.getSelectedSubject();
                String room = addMeetingViewModel.getSelectedRoom();
                Calendar date = addMeetingViewModel.getSelectedDate();
                Calendar time = addMeetingViewModel.getSelectedTime();
                List<String> participantList = addMeetingViewModel.getParticipantList();

                // Add a new meeting to the meeting list using the ViewModel
                addMeetingViewModel.addMeetingToMeetingList(subject, room, date, time, participantList);

                // Trigger an update of the meeting list by applying filters (or not) and updating the list
                meetingSharedViewModel.applyFiltersAndUpdateList();
            }
        });

        /**
         * Sets up a click listener for the "Cancel" button.
         * When the button is clicked, it replaces the current fragment with the MeetingListFragment.
         *
         * @param v The view that was clicked.
         */
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new MeetingListFragment())
                        .commit();
            }
        });

        /**
         * Sets up a click listener for the "Add Mail" button.
         * When the button is clicked, it displays a dialog to add a new email address.
         *
         * @param v The view that was clicked.
         */
        binding.buttonAddMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEmailDialog();
            }
        });
    }

    /**
     * Displays an AlertDialog for adding a new email address.
     * The AlertDialog contains an EditText for entering the email address.
     * It validates the entered email address and adds it to the participant list if valid.
     */
    private void showAddEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Ajouter une adresse e-mail");

        // Set up an EditText for entering the email address
        input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        builder.setView(input);

        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the entered email address
                String email = input.getText().toString();

                // Validate the entered email address and add it to the participant list if valid
                if (addMeetingViewModel.areEmailsValid(email)) {
                    addMeetingViewModel.addParticipant(email);
                    addMeetingViewModel.validateParticipants(email);
                } else {
                    // Update the LiveData to indicate whether the entered email address is valid or not
                    addMeetingViewModel.validateParticipants(email);
                }
            }
        });

        builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Displays a DatePickerDialog to allow the user to select a date for the meeting.
     * The method initializes the dialog with the current date and sets up a listener to handle the date selection.
     * After the user selects a date, it triggers the validation of the selected date and checks if the time
     * is valid after the selected date. It updates the UI to display the selected date in the specified format.
     */
    private void showDatePicker() {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog with a listener for date selection
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {

            /**
             * Called when the user sets a date on the DatePickerDialog.
             *
             * @param view        The DatePicker view.
             * @param year        The selected year.
             * @param monthOfYear The selected month (0-11).
             * @param dayOfMonth  The selected day of the month.
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                // Create a Calendar object for the selected date
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, monthOfYear, dayOfMonth);

                // Validate the selected date and check if the time is valid after the selected date
                addMeetingViewModel.validateDate(selectedCalendar);
                addMeetingViewModel.checkTimeAfterDate();

                // Format the selected date and update the UI
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                binding.dateDeLaReunion.setText(dateFormat.format(selectedCalendar.getTime()));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Displays a time picker to allow the user to choose the meeting time.
     * Validates the selected time using the ViewModel and updates the display accordingly.
     */
    private void showTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Create and show the time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {

            /**
             * Called when the user sets a time on the TimePickerDialog.
             *
             * @param view      The TimePicker view.
             * @param hourOfDay The selected hour of the day.
             * @param minute    The selected minute.
             */
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Create a Calendar object with the selected time
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedCalendar.set(Calendar.MINUTE, minute);

                // Validate the selected time using the ViewModel
                addMeetingViewModel.validateTime(selectedCalendar);

                // Format the selected time and update the display
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                binding.heureDeLaReunion.setText(timeFormat.format(addMeetingViewModel.getSelectedTime().getTime()));
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
}