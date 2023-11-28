import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.MeetingSharedViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
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

        // Configure le comportement de meetingRepository.getMeetings()
        when(meetingRepository.getMeetings()).thenReturn(allMeetings);

        // Initialise le ViewModel avec le meetingRepository mocké
        meetingSharedViewModel = new MeetingSharedViewModel(meetingRepository);
    }

    @Test
    public void testApplyFiltersAndUpdateList() {
        // Préparation des données de test
        List<Meeting> allMeetings = meetingRepository.getMeetings();

        // Test avec aucun filtre
        meetingSharedViewModel.applyFiltersAndUpdateList();

        // Utilisez assertEquals pour comparer les valeurs directement à partir du LiveData de la classe
        assertEquals(allMeetings, meetingSharedViewModel.getMeetingsLiveData().getValue());

        // Test avec un filtre par salle
        meetingSharedViewModel.setFilterByRoom("Room1");
        List<Meeting> filteredByRoom = meetingSharedViewModel.filterMeetingsByRoom("Room1");
        meetingSharedViewModel.applyFiltersAndUpdateList();

        // Utilisez assertEquals pour comparer les valeurs directement à partir du LiveData de la classe
        assertEquals(filteredByRoom, meetingSharedViewModel.getMeetingsLiveData().getValue());

        // Test avec un filtre par date
        Calendar dateFilter = Calendar.getInstance();
        meetingSharedViewModel.setFilterByDate(dateFilter);
        List<Meeting> filteredByDate = meetingSharedViewModel.filterMeetingsByDate(dateFilter);
        meetingSharedViewModel.applyFiltersAndUpdateList();

        // Utilisez assertEquals pour comparer les valeurs directement à partir du LiveData de la classe
        assertEquals(filteredByDate, meetingSharedViewModel.getMeetingsLiveData().getValue());
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
    }
}
