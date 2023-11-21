// Annotations permettant l'utilisation de Mockito et l'initialisation des mocks
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

// Importations de classes nécessaires
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingApiService;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.MeetingSharedViewModel;

// Déclaration de la classe de test
public class UnitTest {

    // Annotation pour créer un mock de MeetingApiService
    @Mock
    private MeetingApiService mockMeetingApiService;

    // Instance de MeetingRepository à utiliser
    private MeetingRepository meetingRepository;

    // Instance de DummyMeetingApiService à utiliser
    private DummyMeetingApiService dummyMeetingApiService;


    // Méthode d'initialisation exécutée avant chaque test
    @Before
    public void setup() {

        // Initialise les mocks
        MockitoAnnotations.initMocks(this);

        // Initialise MeetingRepository avec le mock créé précédemment
        meetingRepository = new MeetingRepository(mockMeetingApiService);

        // Initialise DummyMeetingApiService
        dummyMeetingApiService = new DummyMeetingApiService();

        // Initialise MeetingRepository avec DummyMeetingApiService
        meetingRepository = new MeetingRepository(dummyMeetingApiService);
    }

    // Méthode de test pour la méthode getMeetings de MeetingRepository avec mockito
    @Test
    public void functionGetMeetingsTestWithMockito() {

        // Arrange (Préparation)
        // Génère une liste de réunions factices
        List<Meeting> expectedMeetings = DummyMeetingGenerator.generateMeetings();

        // Configure le mock pour renvoyer la liste factice lorsque getMeetings() est appelé
        when(mockMeetingApiService.getMeetings()).thenReturn(expectedMeetings);

        // Act (Action)
        // Appelle la méthode à tester
        List<Meeting> result = meetingRepository.getMeetings();

        // Assert (Vérification)
        // Vérifie que la liste renvoyée par la méthode est la même que la liste attendue
        assertEquals(expectedMeetings, result);
    }
}

