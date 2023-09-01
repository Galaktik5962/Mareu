package com.example.mareu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mareu.DI.DI;
import com.example.mareu.DI.MeetingViewModelFactory;
import com.example.mareu.R;
import com.example.mareu.data.Meeting;
import com.example.mareu.databinding.FragmentMeetingListBinding;

import java.util.ArrayList;

public class MeetingListFragment extends Fragment implements MeetingItemListener {

    private MeetingListViewModel viewModel;
    private MeetingListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate le layout associé au fragment et le relie à la vue parente (container)
        return inflater.inflate(R.layout.fragment_meeting_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Crée une instance du ViewModel associée à ce fragment
        viewModel = new ViewModelProvider(this, new MeetingViewModelFactory(DI.checkIfRepositoryExists())).get(MeetingListViewModel.class);

        // Crée une instance de l'adaptateur de la RecyclerView avec la liste des réunions du ViewModel
        adapter = new MeetingListAdapter(viewModel.getMeetings(), this);

        // Lie les vues du layout du fragment à leurs éléments correspondants grâce à View Binding
        FragmentMeetingListBinding binding = FragmentMeetingListBinding.bind(view);

        // Configure le gestionnaire de mise en page (layout manager) de la RecyclerView en utilisant un LinearLayoutManager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Associe l'adaptateur à la RecyclerView pour afficher les réunions
        binding.recyclerView.setAdapter(adapter);

        viewModel.getMeetingsLiveData().observe(getViewLifecycleOwner(), meetings -> {
            adapter.updateMeetings(meetings);
        });
    }

    @Override
    public void onDeleteClick(Meeting meeting) {

        // Appeler la méthode de suppression du ViewModel en utilisant la référence au ViewModel
        viewModel.deleteMeeting(meeting);
    }
}
