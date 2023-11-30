import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.MeetingSharedViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class MeetingSharedViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private MeetingRepository meetingRepository;
    private MeetingSharedViewModel meetingSharedViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Préparation des données de test
        DummyMeetingApiService dummyMeetingApiService = new DummyMeetingApiService();
        List<Meeting> allMeetings = dummyMeetingApiService.getMeetings();

        //meetingRepository = new MeetingRepository(dummyMeetingApiService);

        // Configure le comportement de meetingRepository.getMeetings()
        when(meetingRepository.getMeetings()).thenReturn(allMeetings);

        // Initialise le ViewModel avec le meetingRepository mocké
        meetingSharedViewModel = new MeetingSharedViewModel(meetingRepository);
    }

    @Test
    public void testApplyFiltersAndUpdateList() {

        // Créez manuellement un objet Meeting avec les propriétés attendues
        Meeting expectedMeeting = new Meeting("Test", "Room1", new GregorianCalendar(2023, 11, 30).getTime(), List.of("test@lamzone.com"));

        // Configurez le comportement du mock
        when(meetingRepository.getMeetings()).thenReturn(Collections.singletonList(expectedMeeting));

        // Testez avec aucun filtre
        meetingSharedViewModel.applyFiltersAndUpdateList();

        // Utilisez assertEquals pour comparer les valeurs directement à partir du LiveData de la classe
        assertEquals(Collections.singletonList(expectedMeeting), meetingSharedViewModel.getMeetingsLiveData().getValue());

        // Testez avec un filtre par salle (Room1)
        meetingSharedViewModel.setFilterByRoom("Room1");
        meetingSharedViewModel.applyFiltersAndUpdateList();

        // Utilisez assertEquals pour comparer les valeurs directement à partir du LiveData de la classe
        assertEquals(Collections.singletonList(expectedMeeting), meetingSharedViewModel.getMeetingsLiveData().getValue());

        // Testez avec un filtre par date (30/11/2023)
        Calendar dateFilter = new GregorianCalendar(2023, 11, 30);
        meetingSharedViewModel.setFilterByDate(dateFilter);
        meetingSharedViewModel.applyFiltersAndUpdateList();

        // Utilisez assertEquals pour comparer les valeurs directement à partir du LiveData de la classe
        assertEquals(Collections.singletonList(expectedMeeting), meetingSharedViewModel.getMeetingsLiveData().getValue());
    }

    @Test
    public void testResetFilters() {
        // Configurez les filtres actuels pour simuler un état filtré
        meetingSharedViewModel.setFilterByRoom("Room1");
        Calendar dateFilter = Calendar.getInstance();
        meetingSharedViewModel.setFilterByDate(dateFilter);

        // Appelez la méthode de réinitialisation des filtres
        meetingSharedViewModel.resetFilters();

        // Vérifiez que les filtres ont été réinitialisés correctement
        assertEquals("", meetingSharedViewModel.getCurrentFilter());
        assertEquals(null, meetingSharedViewModel.getCurrentDateFilter());

        // apply filter + vérifier que mon LD renvoi bien allmeetings
        meetingSharedViewModel.applyFiltersAndUpdateList();
        assertEquals(meetingRepository.getMeetings(), meetingSharedViewModel.getMeetingsLiveData().getValue());

    }
}
