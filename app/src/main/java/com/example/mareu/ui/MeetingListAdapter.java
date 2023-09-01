package com.example.mareu.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.data.Meeting;
import com.example.mareu.databinding.ItemMeetingListBinding;

import java.util.List;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MeetingViewHolder> {

    // Liste des réunions à afficher dans l'adaptateur
    private List<Meeting> meetings;

    private MeetingItemListener meetingItemListener;

    // Constructeur de l'adaptateur qui reçoit la liste des réunions
    public MeetingListAdapter(List<Meeting> meetings, MeetingItemListener meetingItemListener) {

        this.meetings = meetings;
        this.meetingItemListener = meetingItemListener;
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Crée un inflateur pour convertir le layout XML en objet View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Utilise ViewBinding pour créer et lier les vues du layout item_meeting_list.xml
        ItemMeetingListBinding binding = ItemMeetingListBinding.inflate(inflater, parent, false);

        // Crée un ViewHolder en utilisant le Binding
        return new MeetingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {

        // Récupère la réunion à la position donnée dans la liste
        Meeting meeting = meetings.get(position);

        // Appelle la méthode bind du ViewHolder pour remplir les vues avec les données de la réunion
        holder.bind(meeting);

        // Écouteur de clic pour le bouton de suppression
        holder.binding.deleteButton.setOnClickListener(v -> {
               meetingItemListener.onDeleteClick(meeting);
            });
    }

    @Override
    public int getItemCount() {

        // Retourne le nombre total de réunions dans la liste
        return meetings.size();
    }

    // Classe interne pour représenter chaque élément de la liste (ViewHolder)
    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        private ItemMeetingListBinding binding;

        public MeetingViewHolder(@NonNull ItemMeetingListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Méthode pour remplir les vues du ViewHolder avec les détails de la réunion
        public void bind(Meeting meeting) {

            // Premier TextView : Affiche le sujet, l'heure et l'endroit de la réunion
            String meetingInfo = meeting.getSubjectOfMeeting() + " - "
                    + meeting.getFormattedMeetingDateAndTime() + " - "
                    + meeting.getMeetingLocation();
            binding.mainInfo.setText(meetingInfo);

            // Deuxième TextView : Affiche les participants de la réunion
            String participants = meeting.getMeetingParticipants();

            binding.participants.setText(participants);
        }
    }

    // Méthode pour mettre à jour la liste des réunions
    public void updateMeetings(List<Meeting> newMeetings) {
        meetings = newMeetings;
        notifyDataSetChanged();
    }
}

