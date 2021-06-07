/*
 * Created on Wed Apr 07 2021
 *
 * The Unlicense
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public
 * domain. We make this dedication for the benefit of the public at large and to
 * the detriment of our heirs and successors. We intend this dedication to be an
 * overt act of relinquishment in perpetuity of all present and future rights to
 * this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package uc.seng301.eventapp.model;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Abstract entity class (JPA-compliant) that represents an event with:
 * <ul>
 * <li>an identifying name (must be unique)</li>
 * <li>a description</li>
 * <li>a cost (optional)</li>
 * <li>a date</li>
 * <li>an {@link EventType}</li>
 * <li>(potentially empty) list of {@link Participant}</li>
 * <li>an optional {@link Location}</li>
 * </ul>
 *
 * The Event object should be manipulated through the
 * {@link uc.seng301.eventapp.accessor.EventAccessor}.
 *
 * 
 * ADDITIONAL EXPLANATIONS:
 * 
 * Note the difference with lab material where Events could be created. Now
 * Event is abstract and there are now multiple subtypes, depending on the
 * status of the event (e.g., scheduled, canceled).
 * 
 * All subtypes of Event will be persisted in a single table (defined by
 * the @Inheritance annotation below) and a column named "event_status" will be
 * added in the table to discrimate between subtypes (or statuses) (defined by
 * the @DiscriminatorColumn annotation), i.e. inside the Java code, multiple
 * classes (entities) exist but only one table is created in the database for
 * all subtypes. Subtypes will be properly instantiated according to the
 * "event_status" column.
 * 
 * Each of the subtype must have a @DiscriminatorValue annotation set (see e.g.,
 * ScheduledEvent).
 * 
 * See: <https://www.baeldung.com/hibernate-inheritance>
 */
@Entity
@Table(name = "event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "event_status")
public abstract class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_event")
  private Long eventId;

  private String name;
  private String description;

  private double cost;
  private Date date;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_event_type")
  private EventType eventType;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "participant_attends_event", joinColumns = @JoinColumn(name = "id_event"), inverseJoinColumns = @JoinColumn(name = "id_participant"))
  private List<Participant> participants;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_location")
  private Location location;

  /**
   * Cancel this event. Only Scheduled events can be canceled.
   */
  public abstract Event cancel();

  /**
   * Mark this event has having happened. Only Scheduled events can happen.
   */
  public abstract Event happen();

  /**
   * Reschedule this event. Only past or canceled events can be rescheduled.
   * 
   * @param date the new date for this event (must be in the future)
   * @throws IllegalArgumentException if given date is in the past.
   */
  public abstract Event reschedule(Date date) throws IllegalArgumentException;

  /**
   * Archive an event. Any (non-archived) events can be archived. Cannot happen,
   * being canceled or rescheduled afterwards.
   * 
   * Archived events are kept for the record only.
   */
  public abstract Event archive();

  /**
   * Retrieve the technical ID of this event
   *
   * @return the unique Id
   */
  public Long getEventId() {
    return eventId;
  }

  /**
   * Set the unique ID. Used by JPA and status change methods only (hence package
   * private).
   *
   * @param eventId a unique ID
   */
  void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  /**
   * Get the name of the event (should be unique)
   *
   * @return the unique name of this event
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set the unique name of this event
   *
   * @param name a unique name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get this event's cost (shouldn't be negative, but can be 0)
   *
   * @return this event cost
   */
  public double getCost() {
    return this.cost;
  }

  /**
   * Set this event's cost (shouldn't be negative, but can be 0)
   *
   * @param cost this event cost
   */
  public void setCost(double cost) {
    this.cost = cost;
  }

  /**
   * Get the event's date
   *
   * @return the date (shouldn't be null)
   */
  public Date getDate() {
    return this.date;
  }

  /**
   * Sets the event's date
   *
   * @param date the event's date (shouldn't be null)
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Get the event's description
   *
   * @return a non null and non-empty description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Set the description
   *
   * @param description a noon empty and non null description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get the event type entity for this event
   *
   * @return the non null type
   */
  public EventType getEventType() {
    return this.eventType;
  }

  /**
   * Set the type for this event (must exist in database, i.e have an id)
   *
   * @param eventType a type
   */
  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }

  /**
   * Add given participant to list of participants to this event
   * 
   * @param participant a (assumed non-null, non duplicate) participant
   * @return true if the add operation succeeded (from {@link Collections#add})
   */
  public boolean addParticipant(Participant participant) {
    if (getParticipants().isEmpty()) {
      participants = new ArrayList<>();
    }
    return getParticipants().add(participant);
  }

  /**
   * Remove given participant from list of participants in this event
   *
   * @param participant a (assumed is in list) participant
   * @return true if the remove operation succeeded (from {@link Collections#remove})
   */
  public boolean removeParticipant(Participant participant) {
    return getParticipants().remove(participant);
  }


  /**
   * Get the participants entities participating into this event
   *
   * @return a (possibly empty) list of participants
   */
  public List<Participant> getParticipants() {
    return null != this.participants ? this.participants : Collections.emptyList();
  }

  /**
   * Get the optional location for this event
   *
   * @return the location object (can be null)
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Set this event location
   *
   * @param location a location object (if not existing, will be saved/deleted in
   *                 cascade)
   */
  public void setLocation(Location location) {
    this.location = location;
  }

  /**
   * Set the list of participants
   *
   * @param participants a (possibly null) list of participants
   */
  public void setParticipants(List<Participant> participants) { this.participants = participants; }

  /**
   * Notifies all participants of this event.
   * @param status The status that the event is changing too.
   */
  protected void notifyParticipants(EventStatus status) {
    for (EventObserver eventObserver : getParticipants()) {
     eventObserver.updateAttendance(getName(), status);
    }
  }

  @Override
  public String toString() {
    // @formatter:off
    return "{" +
      " eventId='" + getEventId() + "'" +
      ", name='" + getName() + "'" +
      ", description='" + getDescription() + "'" +
      ", cost='" + getCost() + "'" +
      ", date='" + getDate() + "'" +
      ", eventType='" + getEventType() + "'" +
      ", location='" + getLocation() + "'" +
      "}";
      // @formatter:on
  }

}
