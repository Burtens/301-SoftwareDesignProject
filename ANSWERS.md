# SENG301 Assignment 1 (2021) - Student answers

**Samuel Burtenshaw - sgb79**

## Task 1.b - Write acceptance tests for U3 - Add participants to events

### Feature file (Cucumber Scenarios)

participant.feature

### Java class implementing the acceptance tests

AddingParticipantFeature

## Task 2 - Identify the patterns in the code

### Pattern 1

#### What pattern is it?

Factory Method

#### What is its goal in the code?
The Factory method's use in this code is to define an interface for an object but only let the subclasses decide what
class to instantiate. 
By using the factory method we encapsulate the knowledge of what subclass to create and move it away from the 
framework of the application. Due to this not only do we decrease coupling as we don't have to create new objects directly (Principle: Program to an Interface), 
but we also hide away the true implementation of the concrete methods and products (Principle: hide your decisions). 

In contrast, everytime you would need to create a new LocationServiceResult you would need to connect the classes related 
to getting that object directly into your code, Even if they are only needed for that one section. This greatly increases
coupling and even violates the dependency inversion principle.
#### Name of UML Class diagram attached:

Factory Method Pattern UML.png

#### Mapping to GoF pattern elements:

| GoF element | Code element |
| ----------- | ------------ |
| AbstractCreator | LocationService       |
| ConcreteCreator | NominatimQuery        |
| AbstractProduct | LocationServiceResult |
| ConcreteProduct | NominatimResult       |
| FactoryMethod   | getCityFromString()   |
### Pattern 2

#### What pattern is it?

State

#### What is its goal in the code?

This design pattern is implemented into the code to allow events to have multiple states that they can be in 
and allow the events state to change based on a users decisions. 

By using a State patten this allows the different events i.e. Scheduled, Archived, Past and Canceled to have different 
behavior defined separately in their own sub-classes with transitions defined as explicit methods (Principle: Tell, don't ask, Program to an Interface). 
This makes it easier to the follow transition path and can make the codes intent clearer.

In contrast, the context implementation would have to use data values to define internal states. 
This would intern require repeat conditional or case operations against these values in order to preform actions.
The side effects of this could cause large conditional statements or even monolithic methods that make it hard to follow what
the code is doing and mean that if a new state is added all that code would have to be refactored in order to make room for the new state.

#### Name of UML Class diagram attached:

State Pattern UML.png

#### Mapping to GoF pattern elements:

| GoF element | Code element |
| ----------- | ------------ |
| Context     | EventHandler |
| request()   | updateEventStatus() |
| State       | Event        |
| ConcreteStateA | ScheduledEvent |
| ConcreteStateB | CanceledEvent |
| ConcreteStateC | ArchivedEvent |
| ConcreteStateD | PastEvent |
| handle() | archive() |
| handle() | happen() |
| handle() | reschedule() |
| handle() | cancel() |

## Task 3 - Full UML Class diagram

### Name of file of full UML Class diagram attached

Full UML.png

## Task 4 - Implement new feature

### What pattern fulfils the need for the feature?

Observer

### What is its goal and why is it needed here?

In the case of this AC users need to be notified when the events they are attending change status. The idea of an eventObserver
pattern allows us to do this without forcing events to directly know about the participants that are attending the event.
This ensures that events and participants are kept in sync whilst ensuring that we are still separating concerns and avoiding 
tight coupling. 

We can use the eventObserver pattern to notify participants of events when they change. This is exactly what we need to implement U4.


### Name of UML Class diagram attached:

Observer Pattern UML.png

### Mapping to GoF pattern elements:

| GoF element      | Code element |
| -----------      | ------------ |
| Subject          | Event                |
| ConcreteSubjectA | ArchivedEvent        |
| ConcreteSubjectB | CanceledEvent        |
| ConcreteSubjectC | PastEvent            |
| ConcreteSubjectD | ScheduledEvent       |
| Observer         | EventObserver        |
| ConcreteObserver | Participant          |
| notify()         | notifyParticipants() |
| attach()         | addParticipant()     |
| detach()         | removeParticipant()  |
| update()         | updateAttendance()   |


## Task 5 - BONUS - Acceptance tests for Task 4

### Feature file (Cucumber Scenarios)

**NAME OF FEATURE FILE**

### Java class implementing the acceptance tests

**NAME OF JAVA FILE**
