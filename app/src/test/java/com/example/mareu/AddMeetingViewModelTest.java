import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.AddMeetingViewModel;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AddMeetingViewModel addMeetingViewModel;
    private MeetingRepository meetingRepository;

    private DummyMeetingApiService dummyMeetingApiService;

    @Before
    public void setUp() {
        // Initialisation de DummyMeetingApiService
        dummyMeetingApiService = new DummyMeetingApiService();

        // Initialisation de MeetingRepository avec DummyMeetingApiService
        meetingRepository = new MeetingRepository(dummyMeetingApiService);

        // Initialisation de AddMeetingViewModel avec MeetingRepository
        addMeetingViewModel = new AddMeetingViewModel(meetingRepository);
    }

    @Test
    public void testAddMeetingToMeetingList() {
        // Préparation des données de test
        String subject = "Test Meeting";
        String room = "Room Test";
        Calendar date = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        List<String> participants = List.of("test@lamzone.com");

        // Appel de la fonction à tester
        addMeetingViewModel.addMeetingToMeetingList(subject, room, date, time, participants);

        // Vérifie que la conversion de la date et de l'heure s'est faite correctement
        Date expectedDateAndTime = addMeetingViewModel.createMeetingDate(date, time);

        // Vérifie que la réunion a été ajoutée avec les bons détails
        List<Meeting> meetings = meetingRepository.getMeetings();
        Meeting addedMeeting = meetings.get(meetings.size() - 1);

        assertEquals(subject, addedMeeting.getSubjectOfMeeting());
        assertEquals(room, addedMeeting.getMeetingLocation());

        // Comparaison des dates en ignorant les secondes
        assertTrue(DateUtils.truncatedEquals(expectedDateAndTime, addedMeeting.getMeetingDateAndTime(), Calendar.MINUTE));

        assertEquals(participants, addedMeeting.getMeetingParticipants());

        // Vérifie que la valeur de meetingAddedSuccessfully est correctement mise à true
        assertTrue(addMeetingViewModel.getMeetingAddedSuccessfully().getValue());

    }
}
