package com.example.mareu.DI;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.AddMeetingViewModel;
import com.example.mareu.ui.MeetingListViewModel;

public class MeetingViewModelFactory implements ViewModelProvider.Factory {

    private final MeetingRepository meetingRepository;

    // Constructeur de la fabrication des ViewModels
    public MeetingViewModelFactory(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        // Vérifie si le ViewModel demandé est de type MeetingListViewModel
        if (modelClass.isAssignableFrom(MeetingListViewModel.class)) {

            // Crée et retourne une instance de MeetingListViewModel en lui fournissant le MeetingRepository
            return (T) new MeetingListViewModel(meetingRepository);

        } else if (modelClass.isAssignableFrom(AddMeetingViewModel.class)) {
            return (T) new AddMeetingViewModel(meetingRepository);
        }

        // Si le ViewModel demandé n'est ni MeetingListViewModel ni AddMeetingViewModel, lance une exception
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}




