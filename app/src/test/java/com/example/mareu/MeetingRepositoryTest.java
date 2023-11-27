import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MeetingRepositoryTest {

    private MeetingRepository meetingRepository;

    private DummyMeetingApiService dummyMeetingApiService;


    @Before
    public void setUp() { // POSSIBILITE D UTILISER LA DI ?

        // Initialisation de dummyMeetingApiService
        dummyMeetingApiService = new DummyMeetingApiService();

        // Initialisation de MeetingRepository avec dummyMeetingApiService
        meetingRepository = new MeetingRepository(dummyMeetingApiService);


    }

    @Test
    public void functionGetMeetingsTest() {
        // Obtention de la liste générée par DummyMeetingGenerator
        List<Meeting> expectedMeetings = DummyMeetingGenerator.generateMeetings();

        // Appel de la fonction à tester
        List<Meeting> meetings = meetingRepository.getMeetings();

        // Vérifie que la liste retournée est la même que celle générée
        assertEquals(expectedMeetings, meetings);
    }

    @Test
    public void functionDeleteMeetingTest() {

        // Obtention de la liste initiale des réunions
        List<Meeting> initialMeetings = DummyMeetingGenerator.generateMeetings();

        // Sélection d'une réunion à supprimer (par exemple, la première dans la liste initiale)
        Meeting meetingToDelete = initialMeetings.get(0);

        // Vérifie la taille initiale de la liste
        int initialSize = dummyMeetingApiService.getMeetings().size();

        // Appel de la fonction à tester
        meetingRepository.deleteMeeting(meetingToDelete);

        // Obtention de la liste mise à jour après la suppression
        List<Meeting> updatedMeetings = dummyMeetingApiService.getMeetings();

        // Vérifie que la taille de la liste a diminué d'une unité
        assertEquals(initialSize - 1, updatedMeetings.size());

        // Vérifie que la réunion a été correctement supprimée
        assertFalse(updatedMeetings.contains(meetingToDelete));

    }

    @Test
    public void functionCreateMeetingTest() {
        // Obtention de la liste initiale des réunions
        List<Meeting> initialMeetings = dummyMeetingApiService.getMeetings();

        // Création d'une nouvelle réunion
        Meeting newMeeting = new Meeting("Test Meeting", "Room Test", DummyMeetingGenerator.generateTime(2023, 11, 24, 14, 0), List.of("test@lamzone.com"));

        // Vérifi la taille initiale de la liste
        int initialSize = initialMeetings.size();

        // Appel de la fonction à tester
        meetingRepository.createMeeting(newMeeting);

        // Obtention de la liste mise à jour après la création
        List<Meeting> updatedMeetings = dummyMeetingApiService.getMeetings();

        // Vérifie que la taille de la liste a augmenté d'une unité
        assertEquals(initialSize + 1, updatedMeetings.size());

        // Vérifie que la nouvelle réunion a été correctement ajoutée
        assertTrue(updatedMeetings.contains(newMeeting));
    }

}


