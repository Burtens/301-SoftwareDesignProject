package gradle.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import uc.seng301.eventapp.accessor.EventAccessor;
import uc.seng301.eventapp.accessor.ParticipantAccessor;
import uc.seng301.eventapp.handler.EventHandler;
import uc.seng301.eventapp.handler.EventHandlerImpl;
import uc.seng301.eventapp.model.Event;
import uc.seng301.eventapp.model.EventStatus;
import uc.seng301.eventapp.model.Participant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationFeature {

    private SessionFactory sessionFactory;
    private EventAccessor eventAccessor;
    private EventHandler eventHandler;

    private Event testEvent;
    private Participant testParticipant;
    private Participant mockParticipant;

    // Used to store system.out calls
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream stdout = System.out;
    private final InputStream stdin = System.in;

    @Before
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        eventAccessor = new EventAccessor(sessionFactory, new ParticipantAccessor(sessionFactory));
        eventHandler = new EventHandlerImpl(eventAccessor);
    }

    //
    // U4 - AC1
    //

    @Given("there is a scheduled event with name {string}")
    public void there_is_a_scheduled_event_with_name(String eventName) {
        testEvent = eventHandler.createEvent(eventName, "Some Description", "20/11/2021", "Some Type");
    }

    @Given("that event has three participants")
    public void that_event_has_three_participants() {
        // Create a Mocked Participant so we can check if the correct method is called
        mockParticipant = mock(Participant.class);

        List<Participant> participants = new ArrayList<>();

        // Add mock participant 3 times to ensure consistency.
        participants.add(mockParticipant);
        participants.add(mockParticipant);
        participants.add(mockParticipant);

        // Add "participants" to event
        testEvent.setParticipants(participants);
    }

    @When("the status of that event changes to canceled")
    public void the_status_of_that_event_changes_to_canceled() {
        // Change Events Status
        eventHandler.updateEventStatus(testEvent, EventStatus.CANCELED, null);
    }

    @Then("I expect each participant to receive a notification containing the event name and new status")
    public void i_expect_each_participant_to_receive_a_notification_containing_the_event_name_and_new_status() {
        // Check that the correct method was called 3 times and with the correct data
        verify(mockParticipant, times(3)).updateAttendance(testEvent.getName(), EventStatus.CANCELED);
    }

    //
    // U4 - AC2
    //

    @Given("there is a participant named {string} attending")
    public void there_is_a_participant_named_attending(String participantName) {
        testParticipant = new Participant(participantName);

        // Add the test event to the users attending events
        List<Event> events = new ArrayList<>();
        events.add(testEvent);
        testParticipant.setEvents(events);

        // Add the participant to the events participants
        testEvent.addParticipant(testParticipant);
    }

    @When("the status of that event is changed to canceled")
    public void the_status_of_that_event_is_changed() {
        // Set output stream to a local variable to check against
        System.setOut(new PrintStream(out));

        // Simulate Input from user
        ByteArrayInputStream in = new ByteArrayInputStream("No".getBytes());
        System.setIn(in);

        // Call event change
        eventHandler.updateEventStatus(testEvent, EventStatus.CANCELED, null);
    }

    @Then("I expect a notification message containing the participants name, the event name and its new status")
    public void i_expect_a_notification_message_containing_the_participants_name_the_event_name_and_its_new_status() {
        // Test to see if the output message contains the correct data.
        assertTrue(out.toString().contains(testParticipant.getName()));
        assertTrue(out.toString().contains(testEvent.getName()));
        assertTrue(out.toString().contains(EventStatus.CANCELED.toString()));

        // Reset All System Inputs and Outputs
        System.setOut(stdout);
        System.setIn(stdin);
    }


    //
    // U4 - AC3
    //

    @When("the status of that event is changed to canceled and the user wants to leave")
    public void the_status_of_that_event_is_changed_to_canceled_and_the_user_wants_to_leave() {
        // Set output stream to a local variable to check against
        System.setOut(new PrintStream(out));

        // Simulate Input from user, "yes" means they want to leave
        ByteArrayInputStream in = new ByteArrayInputStream("yes".getBytes());
        System.setIn(in);

        // Call event change
        eventHandler.updateEventStatus(testEvent, EventStatus.CANCELED, null);

        // Refreshes the participants attending the event
        eventHandler.refreshParticipants(testEvent);
    }

    @Then("I expect that participant to be removed from that event.")
    public void i_expect_that_participant_to_be_removed_from_that_event() {
        // Participant shouldn't be attending event anymore
        assertFalse(testEvent.getParticipants().contains(testParticipant));

        // Reset All System Inputs and Outputs
        System.setOut(stdout);
        System.setIn(stdin);
    }



    //
    // U4 - AC4
    //

    @Given("there is a canceled event with name {string}")
    public void there_is_a_canceled_event_with_name(String eventName) {
        // Create new event and set status to Canceled
        testEvent = eventHandler.createEvent(eventName, "Some Description", "20/11/2021", "Some Type");
        testEvent = eventHandler.updateEventStatus(testEvent, EventStatus.CANCELED, null);
    }

    @When("the status of that event changes to archived")
    public void the_status_of_that_event_changes_to_archived() {
        eventHandler.updateEventStatus(testEvent, EventStatus.ARCHIVED, null);
    }

    @Then("I expect there to be no participants attending that event")
    public void i_expect_there_to_be_no_participants_attending_that_event() {
        assertTrue(testEvent.getParticipants().isEmpty());
    }


}
