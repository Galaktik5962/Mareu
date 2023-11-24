import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

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
    private MutableLiveData<List<Meeting>> mockLiveData;
    private MeetingSharedViewModel meetingSharedViewModel;
    private MeetingRepository meetingRepository;
    private DummyMeetingApiService dummyMeetingApiService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dummyMeetingApiService = new DummyMeetingApiService();
        meetingRepository = new MeetingRepository(dummyMeetingApiService);

        // MockLiveData au lieu du LiveData réel
        meetingSharedViewModel = new MeetingSharedViewModel(meetingRepository) {
            @Override
            public MutableLiveData<List<Meeting>> getMeetingsLiveData() {
                return mockLiveData;
            }
        };
    }

    @Test
    public void testApplyFiltersAndUpdateList() {
        // Préparation des données de test
        List<Meeting> allMeetings = meetingRepository.getMeetings();

        // Configure le comportement du mockLiveData
        when(mockLiveData.getValue()).thenReturn(allMeetings);

        // Test avec aucun filtre
        meetingSharedViewModel.applyFiltersAndUpdateList();
        verify(mockLiveData).setValue(allMeetings);

        // Test avec un filtre par salle
        meetingSharedViewModel.setFilterByRoom("Room1");
        List<Meeting> filteredByRoom = meetingSharedViewModel.filterMeetingsByRoom("Room1");
        meetingSharedViewModel.applyFiltersAndUpdateList();
        verify(mockLiveData).setValue(filteredByRoom);

        // Test avec un filtre par date
        Calendar dateFilter = Calendar.getInstance();
        meetingSharedViewModel.setFilterByDate(dateFilter);
        List<Meeting> filteredByDate = meetingSharedViewModel.filterMeetingsByDate(dateFilter);
        meetingSharedViewModel.applyFiltersAndUpdateList();
        verify(mockLiveData).setValue(filteredByDate);
    }
}
