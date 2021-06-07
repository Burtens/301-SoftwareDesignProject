package uc.seng301.eventapp.model;

public interface EventObserver {

    /**
     * Called by subject and updates participant based on data passed.
     *
     * @param eventName Name of the Event that updated
     * @param newStatus The new status of event that updated
     */
    void updateAttendance(String eventName, EventStatus newStatus);

}
