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

Singleton

#### What is its goal in the code?

This design pattern is implemented to ensure that only one instance of the DateUtil class is created.
The singleton pattern also provides a single access point to the class that is generated when the getInstance method is 
first called. This is useful as it provides control over how and when clients access it.

In the case of this application DateUtils is built this way to ensure that all the dates used are consistent with each other
with a set default format. This also means that the default date java library doesn't have to be imported into every class that 
needs it, instead we can get the current instance of the DateUtils class. This also improved the name space lowering the amount of 
imports and variables that each class needs.

#### Name of UML Class diagram attached:

Singleton Class Diagram.png

#### Mapping to GoF pattern elements:

| GoF element | Code element |
| ----------- | ------------ |
| Singleton   | DateUtil     |
| uniqueInstance| instance   |
| instance() | getInstance() |
| Singleton()| DateUtil()    |
### Pattern 2

#### What pattern is it?

State

#### What is its goal in the code?

This design pattern is implemented into the code to allow events to have multiple states that they can be in 
and allow the events state to change based on a users decisions. 

By using a State patten this allows the different events i.e. Scheduled, Archived, Past and Canceled to have different 
behavior defined separately in their own sub-classes with transitions defined as explicit methods. 
This makes it easier to the follow transition path and can make the codes intent clearer.

In contrast, the context implementation would have to use data values to define internal states. 
This would intern require repeat conditional or case operations against these values in order to preform actions.
The side effects of this could cause large conditional statements or even monolithic methods that make it hard to follow what
the code is doing and mean that if a new state is added all that code would have to be refactored in order to make room for the new state.

#### Name of UML Class diagram attached:

**YOUR ANSWER**

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
| handle() | happen() |
| handle() | cancel() |
| handle() | reschedule() |
| handle() | archive() |

## Task 3 - Full UML Class diagram

### Name of file of full UML Class diagram attached

**YOUR ANSWER**

## Task 4 - Implement new feature

### What pattern fulfils the need for the feature?

**YOUR ANSWER**

### What is its goal and why is it needed here?

**YOUR ANSWER**

### Name of UML Class diagram attached:

**YOUR ANSWER**

### Mapping to GoF pattern elements:

| GoF element | Code element |
| ----------- | ------------ |
|             |              |

## Task 5 - BONUS - Acceptance tests for Task 4

### Feature file (Cucumber Scenarios)

**NAME OF FEATURE FILE**

### Java class implementing the acceptance tests

**NAME OF JAVA FILE**
