package com.example.mareu.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mareu.DI.DI;
import com.example.mareu.DI.MeetingViewModelFactory;
import com.example.mareu.MainActivity;
import com.example.mareu.R;
import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.databinding.FragmentMeetingListBinding;

import java.util.ArrayList;
import java.util.List;

public class MeetingListFragment extends Fragment implements MeetingItemListener {

    private MeetingSharedViewModel meetingSharedViewModel;

    private MeetingListAdapter adapter;


    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupère l'instance du MeetingSharedViewModel créée dans la MainActivity
        meetingSharedViewModel = new ViewModelProvider(getActivity()).get(MeetingSharedViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) requireActivity()).toolbarAndAddButtonAction(this);

        // Inflate le layout associé au fragment et le relie à la vue parente (container)
        return inflater.inflate(R.layout.fragment_meeting_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("TAG", "onViewCreated: ");



        // Crée une instance de l'adaptateur de la RecyclerView avec la liste des réunions du ViewModel
        adapter = new MeetingListAdapter(new ArrayList<>(), this);

        // Lie les vues du layout du fragment à leurs éléments correspondants grâce à View Binding
        FragmentMeetingListBinding binding = FragmentMeetingListBinding.bind(view);

        // Configure le gestionnaire de mise en page (layout manager) de la RecyclerView en utilisant un LinearLayoutManager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Associe l'adaptateur à la RecyclerView pour afficher les réunions
        binding.recyclerView.setAdapter(adapter);


        // Observe les changements de la liste de réunions dans le ViewModel
        meetingSharedViewModel.getMeetingsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetings) {
                // Mettez à jour l'adaptateur avec la nouvelle liste de réunions
                Log.d("TAG", "onChanged: " + meetings.size());
                adapter.updateMeetings(meetings);
            }
        });
    }

    @Override
    public void onDeleteClick(Meeting meeting) {

        // Appel à la méthode de suppression du ViewModel en utilisant la référence au ViewModel
        meetingSharedViewModel.deleteMeeting(meeting);
    }
}
