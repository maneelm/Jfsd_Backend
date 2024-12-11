package klu.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import klu.model.Event;
import klu.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Add a new event
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get an event by ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    // Delete an event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    public void updateEvent(Event event) {
		// TODO Auto-generated method stub
		eventRepository.save(event);
		
	}
    public boolean deleteEventById(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
