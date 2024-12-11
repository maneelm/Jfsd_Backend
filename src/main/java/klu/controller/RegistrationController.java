//package klu.controller;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import klu.services.RegistrationService;
//
//@RestController
//@RequestMapping("/api/student")
//public class RegistrationController {
//
//    @Autowired
//    private RegistrationService registrationService;
//
//    // Endpoint to register a student for an event
//    @PostMapping("/register/{studentId}/{eventId}")
//    public ResponseEntity<String> registerForEvent(@PathVariable Long studentId, @PathVariable Long eventId) {
//        String message = registrationService.registerForEvent(studentId, eventId);
//        return ResponseEntity.ok(message);  // Return the response with the registration status message
//    }
//}
