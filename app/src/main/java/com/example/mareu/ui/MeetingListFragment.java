package com.example.mareu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mareu.MainActivity;
import com.example.mareu.R;
import com.example.mareu.data.Meeting;
import com.example.mareu.databinding.FragmentMeetingListBinding;

import java.util.List;


/**
 * Fragment displaying the list of meetings.
 */
public class MeetingListFragment extends Fragment implements MeetingItemListener {

    /**
     * ViewModel shared between fragments to manage meeting-related data.
     */
    private MeetingSharedViewModel meetingSharedViewModel;

    /**
     * Adapter for the RecyclerView to display the list of meetings.
     */
    private MeetingListAdapter adapter;


    /**
     * Inflates the layout associated with the fragment and attaches it to the parent view (container).
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set up the toolbar and add button actions from the MainActivity
        ((MainActivity) requireActivity()).toolbarAndAddButtonAction(this);

        // Inflate the layout associated with the fragment and attach it to the parent view (container)
        return inflater.inflate(R.layout.fragment_meeting_list, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view. It is always followed by onResume().
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the instance of MeetingSharedViewModel created in MainActivity
        meetingSharedViewModel = new ViewModelProvider(getActivity()).get(MeetingSharedViewModel.class);

        // Create an instance of the RecyclerView adapter with the list of meetings from the ViewModel
        adapter = new MeetingListAdapter(meetingSharedViewModel.getMeetingsLiveData().getValue(), this);

        // Bind the views of the fragment layout to their corresponding elements using View Binding
        FragmentMeetingListBinding binding = FragmentMeetingListBinding.bind(view);

        // Configure the layout manager of the RecyclerView using a LinearLayoutManager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Associate the adapter with the RecyclerView to display meetings
        binding.recyclerView.setAdapter(adapter);

        /**
         * Observes changes in the list of meetings from the shared ViewModel and updates the RecyclerView adapter accordingly.
         *
         * This observer is set up to listen for changes in the list of meetings and update the RecyclerView adapter
         * when the data changes. It ensures that the UI stays synchronized with the latest meeting data.
         *
         * @param meetings The updated list of meetings received from the ViewModel.
         */
        meetingSharedViewModel.getMeetingsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetings) {
                // Update the adapter with the new list of meetings
                adapter.updateMeetings(meetings);
            }
        });
    }

    /**
     * Handles the click event when the delete button of a meeting item is clicked.
     *
     * @param meeting The Meeting object representing the meeting item to be deleted.
     */
    @Override
    public void onDeleteClick(Meeting meeting) {
        // Call the ViewModel's delete method using the ViewModel reference
        meetingSharedViewModel.deleteMeeting(meeting);
    }
}
