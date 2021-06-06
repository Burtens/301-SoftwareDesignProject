package uc.seng301.eventapp.model;

public interface EventObserver {

    /**
     * Updates participant
     * @param id Id of the event that changed
     */
    void Update(Long id);

}
