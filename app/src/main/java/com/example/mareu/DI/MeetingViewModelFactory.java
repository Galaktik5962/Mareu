package com.example.mareu.DI;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.AddMeetingViewModel;
import com.example.mareu.ui.MeetingSharedViewModel;

/**
 * ViewModel factory class responsible for creating instances of specific ViewModels.
 */
public class MeetingViewModelFactory implements ViewModelProvider.Factory {

    private final MeetingRepository meetingRepository;

    /**
     * Constructs a ViewModel factory for the specified MeetingRepository.
     *
     * @param meetingRepository The MeetingRepository instance to be used for creating ViewModels.
     */
    public MeetingViewModelFactory(MeetingRepository meetingRepository) {
        this.meetingRepository = DI.checkIfRepositoryExists();
    }

    /**
     * Creates a ViewModel of the specified class.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @param <T>        The type of the ViewModel.
     * @return A new instance of the requested ViewModel.
     * @throws IllegalArgumentException If the requested ViewModel class is unknown.
     */
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(AddMeetingViewModel.class)) {
            return (T) new AddMeetingViewModel(meetingRepository);

        } else if (modelClass.isAssignableFrom(MeetingSharedViewModel.class)) {
            return (T) new MeetingSharedViewModel(meetingRepository);

        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
