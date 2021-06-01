package gradle.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import uc.seng301.eventapp.accessor.EventAccessor;
import uc.seng301.eventapp.accessor.ParticipantAccessor;
import uc.seng301.eventapp.handler.EventHandler;
import uc.seng301.eventapp.handler.EventHandlerImpl;
import uc.seng301.eventapp.model.Event;
import uc.seng301.eventapp.model.Participant;

import java.util.Collections;
import java.util.List;

public class AddingParticipantFeature {


    SessionFactory sessionFactory;
    EventAccessor eventAccessor;
    ParticipantAccessor participantAccessor;
    EventHandler eventHandler;

    Event testEvent;
    Long testEventId;
    Long testParticipantId;
    Participant testParticipant;

    // Invalid Participants
    Participant invalidParticipant1;
    Participant invalidParticipant2;
    Participant invalidParticipant3;
    Participant invalidParticipant4;
    Participant invalidParticipant5;

    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        participantAccessor = new ParticipantAccessor(sessionFactory);
        eventAccessor = new EventAccessor(sessionFactory, participantAccessor);
        eventHandler = new EventHandlerImpl(eventAccessor);
    }

    //
    // U3 - AC1
    //

    @Given("There is an existing participant with the name {string} and an existing event with name {string} and date {string}")
    public void there_is_an_existing_participant_with_name_and_an_existing_event_with_name_and_date(String participantName,
                                                                           String eventName, String eventDate) {
        // Create new participant and store it in persistent storage
        Participant participant = new Participant(participantName);
        participantAccessor.persistParticipant(participant);
        Participant storedParticipant = participantAccessor.getParticipantByName(participantName);

        testParticipantId = storedParticipant.getParticipantId();

        // Check that the participant is created correctly
        Assertions.assertNotNull(storedParticipant);
        Assertions.assertEquals(participantName , storedParticipant.getName());

        //Create a new event
        testEvent = eventHandler.createEvent(eventName, "Some Description", eventDate, "Workshop");
    }

    @When("I add that participant to the event")
    public void i_add_that_participant_to_the_event() {
        testParticipant = participantAccessor.getParticipantById(testParticipantId);
        testEvent.addParticipant(testParticipant);
        testEventId = eventAccessor.persistEvent(testEvent);

    }

    @Then("I expect that event to contain the participant in its attending participants")
    public void i_expect_that_event_to_contain_the_participant_in_its_attending_participants() {
        Event storedEvent = eventAccessor.getEventAndParticipantsById(testEventId);

        List<Participant> participantList = storedEvent.getParticipants();

        // Check that the list is correctly return
        Assertions.assertNotNull(participantList);

        // Check that the participant is added to the event
        Assertions.assertEquals(1, participantList.size());

        Participant returnedParticipant = participantList.get(0);
        Assertions.assertEquals(testParticipant.getName(), returnedParticipant.getName());

        // Also test to see if the participants events contain the given event
        Assertions.assertTrue(returnedParticipant.getEvents().stream().anyMatch(event -> event.getEventId().equals(testEventId)));
    }

    //
    // U3 - AC2
    //

    @Given("There is an existing event with name {string} and date {string}")
    public void there_is_an_existing_event_with_name_and_date(String eventName, String eventDate) {
        //Create a new event
        String eventDescription = "Some Description";
        String eventType = "Some Type";
        testEvent = eventHandler.createEvent(eventName, eventDescription, eventDate, eventType);

    }

    @When("I add a non-existent participant with name {string} to that event")
    public void i_add_a_non_existent_participant_with_name_to_that_event(String participantName) {
        testParticipant = new Participant(participantName);

        testEvent.addParticipant(testParticipant);

        // Check if the test participant was added to the event
        List<Participant> participants = testEvent.getParticipants();

        Assertions.assertEquals(1, participants.size());
        Assertions.assertEquals(testParticipant.getName(), participants.get(0).getName());

        // Persist the event with given participant
        testEventId = eventAccessor.persistEventAndParticipants(testEvent);

        // Check that the event was added to persistent storage
        Assertions.assertTrue(eventAccessor.eventExistsWithName(testEvent.getName()));

    }

    @Then("A new participant should be generated with the given name and added to that event")
    public void a_new_participant_should_be_generated_with_the_given_name_and_added_to_that_event() {
        // Get new participant
        Participant storedParticipant = participantAccessor.getParticipantByName(testParticipant.getName());

        // Check that the participant with the given name exists
        Assertions.assertNotNull(storedParticipant);

        // Get the events the user is participating in
        List<Event> eventsParticipating = storedParticipant.getEvents();

        Assertions.assertEquals(1, eventsParticipating.size());

        Event participantEvent = eventsParticipating.get(0);

        // Check that the event is the same as the one given
        Assertions.assertEquals(testEvent.getName(), participantEvent.getName());
        Assertions.assertEquals(testEvent.getEventType().getName(), participantEvent.getEventType().getName());
        Assertions.assertEquals(testEvent.getDescription(), participantEvent.getDescription());
        Assertions.assertEquals(testEvent.getDate(), participantEvent.getDate());
    }

    //
    // U3 - AC3
    //

    @When("I attempt to add an empty participant or participants with names {string}, {string} and {string}")
    public void i_attempt_to_add_an_empty_participant_or_participants_with_names(String name1, String name2, String name3) {
        invalidParticipant1 = null;
        invalidParticipant2 = new Participant(null);
        invalidParticipant3 = new Participant(name1);
        invalidParticipant4 = new Participant(name2);
        invalidParticipant5 = new Participant(name3);
    }

    @Then("I should get an exception when I try to add any of those to the event.")
    public void i_should_get_an_exception_when_i_try_to_add_any_of_those_to_the_event() {

        // Test adding a null participant
        testEvent.addParticipant(invalidParticipant1);
        Assertions.assertThrows(NullPointerException.class, () -> eventAccessor.persistEventAndParticipants(testEvent));

        // Test adding a participant with null as its name
        testEvent.setParticipants(Collections.emptyList());
        testEvent.addParticipant(invalidParticipant2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> eventAccessor.persistEventAndParticipants(testEvent));


        // Tests Participants with invalid names
//        testEvent.setParticipants(Collections.emptyList());
//        testEvent.addParticipant(invalidParticipant3);
//        Assertions.assertThrows(IllegalArgumentException.class, () -> eventAccessor.persistEventAndParticipants(testEvent));

        testEvent.setParticipants(Collections.emptyList());
        testEvent.addParticipant(invalidParticipant4);
        Assertions.assertThrows(IllegalArgumentException.class, () -> eventAccessor.persistEventAndParticipants(testEvent));

//        testEvent.setParticipants(Collections.emptyList());
//        testEvent.addParticipant(invalidParticipant5);
//        Assertions.assertThrows(IllegalArgumentException.class, () -> eventAccessor.persistEventAndParticipants(testEvent));
    }


}
