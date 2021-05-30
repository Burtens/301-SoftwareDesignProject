package gradle.cucumber;

import io.cucumber.java.Before;
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

public class AddingParticipantFeature {


    SessionFactory sessionFactory;
    EventAccessor eventAccessor;
    ParticipantAccessor participantAccessor;
    EventHandler eventHandler;

    Event testEvent;

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

    @Given("There is a participant with the name {string} and an existing event with name {string} and date {string}")
    public void there_is_a_participant_with_name_and_an_existing_event_with_name_and_date(String participantName,
                                                                           String eventName, String eventDate) {
        // Create new participant and store it in persistent storage
        Participant participant = new Participant(participantName);
        participantAccessor.persistParticipant(participant);
        Participant storedParticipant = participantAccessor.getParticipantByName(participantName);

        // Check that the participant is created correctly
        Assertions.assertNotNull(storedParticipant);
        Assertions.assertEquals(participantName , storedParticipant.getName());

        //Create a new event and store it in persistent storage
        testEvent = eventHandler.createEvent(eventName, "Some Description", eventDate, "Workshop");
        eventAccessor.persistEvent(testEvent);




    }

    @When("I add that participant to the event")
    public void i_add_that_participant_to_the_event() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("I expect that event to contain the participant in its attending participants")
    public void i_expect_that_event_to_contain_the_participant_in_its_attending_participants() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    //
    // U3 - AC2
    //

    @Given("There is an existing event")
    public void there_is_an_existing_event() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


    //
    // U3 - AC3
    //


    @When("I attempt to add an empty participant or participants with names {string}, {string}, {string}")
    public void i_attempt_to_add_an_empty_participant_or_participants_with_names(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("I should get an exception that disallows me to add any of those.")
    public void i_should_get_an_exception_that_disallows_me_to_add_any_of_those() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
