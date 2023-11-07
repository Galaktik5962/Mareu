package com.example.mareu.DI;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.AddMeetingViewModel;
import com.example.mareu.ui.MeetingSharedViewModel;

public class MeetingViewModelFactory implements ViewModelProvider.Factory {

    private final MeetingRepository meetingRepository;


    // Constructeur de la fabrication des ViewModels
    public MeetingViewModelFactory() {
        this.meetingRepository = DI.checkIfRepositoryExists(); // Si le repository existe ne pas le créer, sinon le créer

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        // Vérifie si le ViewModel demandé est de type MeetingListViewModel

        if (modelClass.isAssignableFrom(AddMeetingViewModel.class)) {
            return (T) new AddMeetingViewModel(meetingRepository);

        } else if (modelClass.isAssignableFrom(MeetingSharedViewModel.class)) {
            return (T) new MeetingSharedViewModel(meetingRepository);

        }

        // Si le ViewModel demandé n'est ni MeetingListViewModel ni AddMeetingViewModel ni MeetingSharedViewModel, lance une exception
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}




