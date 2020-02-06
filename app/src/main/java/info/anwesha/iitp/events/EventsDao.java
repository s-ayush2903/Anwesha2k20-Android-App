package info.anwesha.iitp.events;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface EventsDao {

    @Query("SELECT * FROM events WHERE evCategory = 'pronite' OR evCategory = 'proshow' OR evCategory='pre-anwesha' OR evCategory='informal' OR evCategory='technical' OR evCategory = 'cultural' OR evCategory = 'awelfare' ORDER BY evStartTime ASC")
    LiveData<List<EventItem>> loadAllEvents();

    @Query("SELECT * FROM events WHERE evCategory = 'Exhibitions' ORDER BY evStartTime ASC")
    LiveData<List<EventItem>> loadAllExhibitions();

    @Query("SELECT * FROM events WHERE evCategory = 'Eventsschool' ORDER BY evStartTime ASC")
    LiveData<List<EventItem>> loadAllSchoolEvents();

    @Query("SELECT * FROM events WHERE evCategory = 'Ozone' ORDER BY evStartTime ASC")
    LiveData<List<EventItem>> loadOzoneEvents();

    @Query("SELECT * FROM events WHERE evCategory = 'Guesttalks' ORDER BY evStartTime ASC")
    LiveData<List<EventItem>> loadGuestTalks();

    @Query("SELECT * FROM events WHERE evCategory = 'Workshops' OR evCategory = 'workshops' ORDER BY evStartTime ASC")
    LiveData<List<EventItem>> loadWorkshops();

    @Query("SELECT evClub FROM events WHERE evCategory = 'Eventscollege' OR evCategory = 'Eventsall'")
    LiveData<List<String>> loadAllClubs();

    @Query("SELECT evCategory FROM events WHERE evCategory = 'technical' OR evCategory = 'cultural' OR evCategory = 'awelfare'")
    LiveData<List<String>> loadCompetetionsCategory();

    @Query("SELECT evCategory FROM events WHERE evCategory = 'pronite' OR evCategory = 'proshow' OR evCategory='pre-anwesha' OR evCategory='informal' ")
    LiveData<List<String>> loadEventsCategory();

    @Query("select * from events where id = :id")
    EventItem loadEventById(String id);

    @Insert(onConflict = REPLACE)
    void insertEvent(EventItem eventItem);

    @Insert(onConflict = REPLACE)
    void insertOrReplaceEvent(EventItem... eventItems);

    @Query("DELETE FROM events")
    void deleteAllEvents();

}
