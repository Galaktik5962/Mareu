package com.example.mareu.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.data.Meeting;
import com.example.mareu.databinding.ItemMeetingListBinding;

import java.util.List;

/**
 * Adapter class for the RecyclerView in the MeetingListFragment.
 * Responsible for creating ViewHolders and binding meeting data to them.
 */
public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MeetingViewHolder> {

    /**
     * List of meetings to be displayed in the adapter.
     */
    private List<Meeting> meetings;

    /**
     * Listener interface for handling click events on meeting items.
     */
    private MeetingItemListener meetingItemListener;

    /**
     * Constructs a MeetingListAdapter with the specified list of meetings and a listener for item clicks.
     *
     * @param meetings              List of meetings to be displayed.
     * @param meetingItemListener   Listener for item click events.
     */
    public MeetingListAdapter(List<Meeting> meetings, MeetingItemListener meetingItemListener) {
        this.meetings = meetings;
        this.meetingItemListener = meetingItemListener;
    }

    /**
     * Creates and returns a new MeetingViewHolder with the specified parent and view type.
     *
     * @param parent    The ViewGroup into which the new View will be added.
     * @param viewType  The view type of the new View.
     * @return          A new MeetingViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom binding layout
        ItemMeetingListBinding binding = ItemMeetingListBinding.inflate(inflater, parent, false);
        return new MeetingViewHolder(binding);
    }

    /**
     * Binds the meeting data to the specified holder at the given position.
     *
     * @param holder    The MeetingViewHolder to bind the data to.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        // Get the meeting object at the specified position
        Meeting meeting = meetings.get(position);
        // Bind the meeting details to the ViewHolder
        holder.bind(meeting);
        // Set up a click listener for the delete button
        holder.binding.deleteButton.setOnClickListener(v -> meetingItemListener.onDeleteClick(meeting));
    }

    /**
     * Returns the total number of meetings in the adapter's data set.
     *
     * @return The total number of meetings in the adapter.
     */
    @Override
    public int getItemCount() {
        // Return the total number of meetings in the list
        return meetings.size();
    }

    /**
     * ViewHolder class for holding views associated with individual meeting items.
     * This class is responsible for binding meeting data to the views within the item layout.
     */
    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        /**
         * Binding object for the item layout.
         */
        private ItemMeetingListBinding binding;

        /**
         * Constructs a MeetingViewHolder with the specified binding.
         *
         * @param binding   The binding for the item layout.
         */
        public MeetingViewHolder(@NonNull ItemMeetingListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Binds the meeting data to the views within the ViewHolder.
         *
         * @param meeting   The Meeting object containing the data to bind.
         */
        public void bind(Meeting meeting) {
            // Use the new methods to get formatted dates and times
            String formattedDate = meeting.getFormattedDate();
            String formattedTime = meeting.getFormattedTime();

            // First TextView: Display the subject, time, and location of the meeting
            String meetingInfo = meeting.getSubjectOfMeeting() + " - "
                    + formattedDate + " - "
                    + formattedTime + " - "
                    + meeting.getMeetingLocation();
            binding.mainInfo.setText(meetingInfo);

            // Second TextView: Display the participants of the meeting
            List<String> participantsList = meeting.getMeetingParticipants();

            // Use 'participantsList' to set the text of the component after joining it with commas and a space
            binding.participants.setText(TextUtils.join(", ", participantsList));
        }
    }

    /**
     * Updates the list of meetings and notifies the adapter of the data set change.
     *
     * @param newMeetings   The new list of meetings to update the adapter with.
     */
    public void updateMeetings(List<Meeting> newMeetings) {
        meetings = newMeetings;
        notifyDataSetChanged();
    }
}


