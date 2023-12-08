package com.example.mareu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.DI.MeetingViewModelFactory;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.ui.AddMeetingFragment;
import com.example.mareu.ui.MeetingListFragment;
import com.example.mareu.ui.MeetingSharedViewModel;

import java.util.Calendar;
import java.util.List;

/**
 * MainActivity class representing the main entry point of the application.
 * This activity manages the app's UI and functionality, including fragments for meeting lists and meeting addition.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Data-binding class for the main activity layout.
     */
    private ActivityMainBinding binding;

    /**
     * ViewModel shared between fragments to manage meeting-related data.
     */
    public MeetingSharedViewModel meetingSharedViewModel;

    /**
     * Repository class responsible for managing meeting data and providing access to the list of meetings.
     * It does not handle filters directly but serves as a data source for filtered lists in the ViewModel.
     */
    private MeetingRepository meetingRepository;

    /**
     * Initializes the contents of the Activity's options menu.
     *
     * @param menu The options menu in which the items are placed.
     * @return true for the menu to be displayed; false for it to be hidden.
     */
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.filter_menu, menu);
            return true;
        }

    /**
     * Called when a menu item is selected.
     *
     * @param item The selected menu item.
     * @return true if the menu item selection is handled; otherwise, false.
     */
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.filter_icon) {
                // Displays the PopupMenu when the "filter_icon" item is selected
                showPopupMenu();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    /**
     * Displays a PopupMenu for filtering options when the "filter_icon" item is selected.
     * The PopupMenu contains options to filter meetings by room, date, or clear filters.
     */
        private void showPopupMenu() {
            PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.filter_icon));
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.filter_popup_menu, popupMenu.getMenu());

            // Click listener to handle menu item selections
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.filter_by_room) {
                    // If "Filter by Room" option is selected, display the dialog to choose a room
                    showRoomSelectionDialog();
                    return true;

                }  else if (itemId == R.id.filter_by_date) {
                    // If "Filter by Date" option is selected, display the dialog to choose a date
                    showDateSelectionDialog();
                    return true;

                } else if (itemId == R.id.clear_filters) {
                    // If "Clear Filters" option is selected, reset filters
                    meetingSharedViewModel.resetFilters();
                    return true;
                }

                return false;
            });

            popupMenu.show();
        }

    /**
     * Displays a dialog for selecting a meeting room.
     * The dialog presents a list of room options retrieved from DummyMeetingGenerator.
     * When a room is selected, the ViewModel method is called to filter meetings by the selected room.
     */
    private void showRoomSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selectionnez une salle");

        // Access the list of rooms from DummyMeetingGenerator
        String[] roomOptions = DummyMeetingGenerator.getDUMMY_ROOMS().toArray(new String[0]);

        builder.setItems(roomOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedRoom = roomOptions[which];

                // Call the ViewModel method to filter meetings by room
                meetingSharedViewModel.setFilterByRoom(selectedRoom);
            }
        });

        builder.setNegativeButton("Annuler", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays a dialog for selecting a date.
     * The dialog uses a DatePickerDialog to allow the user to pick a date.
     * When a date is selected, a Calendar object is created for the selected date,
     * and the ViewModel method is called to filter meetings by the selected date.
     */
    private void showDateSelectionDialog() {

        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog for date selection
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {

            // Create a Calendar object for the selected date
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);

            // Call the ViewModel method to filter meetings by date
            meetingSharedViewModel.setFilterByDate(selectedDate);

        }, year, month, day);

        // Set the "Cancel" button action to dismiss the dialog
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Annuler", (dialog, which) -> {
            dialog.dismiss();
        });

        datePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new instance of MeetingViewModelFactory
        MeetingViewModelFactory factory = new MeetingViewModelFactory(meetingRepository);

        // Use ViewModelProvider to obtain an instance of MeetingSharedViewModel using the created factory
        meetingSharedViewModel = new ViewModelProvider(this, factory).get(MeetingSharedViewModel.class);

        // Get the complete list of unfiltered meetings from the repository
        List<Meeting> allMeetings = meetingSharedViewModel.getMeetings();

        // Use View Binding to link the activity layout to the MainActivity class
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar); // Configure the Toolbar with View Binding

        // If the activity is created for the first time, add the fragment for the list of meetings
        if (savedInstanceState == null) {

            // Get the fragment manager to manage the activity's fragments
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Start a fragment transaction to replace the fragment container with the "MeetingListFragment"
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new MeetingListFragment())
                    .commit();
        }

        /**
         * Sets up the click listener for the "Add" button in the UI.
         * When the button is clicked, it creates an instance of the "AddMeetingFragment,"
         * obtains the fragment manager, and starts a fragment transaction to replace
         * the current content with the "AddMeetingFragment." It also adds the transaction to the back stack.
         */
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an instance of the "AddMeetingFragment"
                Fragment addMeetingFragment = new AddMeetingFragment();

                // Get the fragment manager to manage the activity's fragments
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Start a fragment transaction to replace the current content with "AddMeetingFragment"
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, addMeetingFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Configures the Toolbar and visibility of the "Add" button based on the current fragment.
     *
     * @param fragment The current fragment being displayed.
     *                 If it is an instance of AddMeetingFragment, the Toolbar and "Add" button are hidden.
     *                 If it is the MeetingListFragment, the Toolbar and "Add" button are shown.
     */
    public void toolbarAndAddButtonAction (Fragment fragment) {

        if (fragment instanceof AddMeetingFragment) {

            // User is on the "AddMeetingFragment," hide the Toolbar and "Add" button
            getSupportActionBar().hide();
            binding.addButton.setVisibility(View.GONE);

        } else {

            // User is on the "MeetingListFragment," show the Toolbar and "Add" button
            getSupportActionBar().show();
            binding.addButton.setVisibility(View.VISIBLE);
        }
    }
}
