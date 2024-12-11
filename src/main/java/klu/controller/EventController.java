package klu.controller;

import klu.model.Event;
import klu.model.Registration;
import klu.services.EventService;
import klu.services.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private RegistrationService registrationService;

    private static final String UPLOAD_DIR = "C:/Users/manee/Documents/sai/bulkregister/uploads";

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    
 // Updated method in EventController
    @PostMapping("/create")
    public ResponseEntity<String> createEvent(
            @RequestParam("eventTitle") String eventTitle,
            @RequestParam("eventDescription") String eventDescription,
            @RequestParam("registrationStartingTime") LocalDateTime registrationStartingTime,
            @RequestParam("registrationEndingTime") LocalDateTime registrationEndingTime,
            @RequestParam("eventScheduleTime") LocalDateTime eventScheduleTime,
            @RequestParam("limit") int limit,
            @RequestParam("eventCoverPhoto") MultipartFile eventCoverPhoto) {

        // Saving the file
        String fileName = eventCoverPhoto.getOriginalFilename();
        File destinationFile = new File(UPLOAD_DIR + File.separator + fileName);

        try {
            eventCoverPhoto.transferTo(destinationFile);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Construct the accessible URL for the frontend
        String fileUrl = "/uploads/" + fileName;

        // Create a new event object and set the fields
        Event event = new Event();
        event.setEventTitle(eventTitle);
        event.setEventDescription(eventDescription);
        event.setCur_time(LocalDateTime.now());
        event.setRegistrationStartingTime(registrationStartingTime);
        event.setRegistrationEndingTime(registrationEndingTime);
        event.setEventScheduleTime(eventScheduleTime);
        event.setEventLimit(limit);

        event.setEventCoverPhoto(fileUrl); // Store the relative URL for accessing the image

        // Save the event
        eventService.addEvent(event);

        return new ResponseEntity<>("Event created successfully! Image URL: " + fileUrl, HttpStatus.CREATED);
    }
    @PostMapping("/{eventId}/register")
    public ResponseEntity<String> registerForEvent(@PathVariable Long eventId, @RequestParam String userId) {
        Event event = eventService.getEventById(eventId); // Assuming you have this method

        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found.");
        }

        LocalDateTime now = LocalDateTime.now();

        // Check registration conditions
        if (now.isAfter(event.getRegistrationEndingTime())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Registration closed.");
        }
        if (now.isBefore(event.getRegistrationStartingTime())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Registration not yet started.");
        }

        // Check if the user has already registered for this event
        if (registrationService.hasUserRegistered(eventId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are already registered for this event.");
        }

        // Check the event limit
        if (event.getEventLimit() <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Maximum limit reached.");
        }

        // Register the user for the event
        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setUserId(userId);
        registration.setRegistrationTime(now);

        registrationService.saveRegistration(registration); // Save registration
        event.setEventLimit(event.getEventLimit() - 1); // Decrease limit
        eventService.updateEvent(event); // Save updated event

        return ResponseEntity.ok("Registration successful!");
        
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        boolean isDeleted = eventService.deleteEventById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
