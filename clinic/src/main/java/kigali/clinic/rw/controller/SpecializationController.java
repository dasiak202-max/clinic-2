package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping(value = "/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specServ;

    // CREATE  ->  POST /api/specialization/save
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveSpecialization(@RequestBody Specialization spec) {
        String message = specServ.saveSpecialization(spec);
        return respond(message, "Specialization saved successfully", HttpStatus.CREATED);
    }

    // READ all  ->  GET /api/specialization/all
    @GetMapping(value = "/all")
    public ResponseEntity<List<Specialization>> getAllSpecializations() {
        return new ResponseEntity<>(specServ.getAllSpecializations(), HttpStatus.OK);
    }

        // B3  ->  GET /api/specializations/unused
    @GetMapping(value = "/unused")
    public ResponseEntity<List<Specialization>> getUnused() {
        return new ResponseEntity<>(specServ.getUnusedSpecializations(), HttpStatus.OK);
    }

    // READ one  ->  GET /api/specialization/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSpecializationById(@PathVariable UUID id) {
        Specialization spec = specServ.getSpecializationById(id);
        if (spec == null) {
            return new ResponseEntity<>("Specialization not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(spec, HttpStatus.OK);
    }

    // UPDATE  ->  PUT /api/specialization/update/{id}
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateSpecialization(@PathVariable UUID id, @RequestBody Specialization spec) {
        String message = specServ.updateSpecialization(id, spec);
        return respond(message, "Specialization updated successfully", HttpStatus.OK);
    }

    // DELETE  ->  DELETE /api/specialization/delete/{id}
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable UUID id) {
        String message = specServ.deleteSpecialization(id);
        return respond(message, "Specialization deleted successfully", HttpStatus.OK);
    }

        // LINK  ->  POST /api/specialization/{specId}/doctor/{doctorId}
    @PostMapping(value = "/{specId}/doctor/{doctorId}")
    public ResponseEntity<?> addDoctor(@PathVariable UUID specId, @PathVariable UUID doctorId) {
        String message = specServ.addDoctorToSpecialization(specId, doctorId);
        return respond(message, "Doctor added to specialization successfully", HttpStatus.OK);
    }

    // UNLINK  ->  DELETE /api/specialization/{specId}/doctor/{doctorId}
    @DeleteMapping(value = "/{specId}/doctor/{doctorId}")
    public ResponseEntity<?> removeDoctor(@PathVariable UUID specId, @PathVariable UUID doctorId) {
        String message = specServ.removeDoctorFromSpecialization(specId, doctorId);
        return respond(message, "Doctor removed from specialization successfully", HttpStatus.OK);
    }

    // READ  ->  GET /api/specialization/{specId}/doctors
    @GetMapping(value = "/{specId}/doctors")
    public ResponseEntity<?> getDoctors(@PathVariable UUID specId) {
        List<Doctor> doctors = specServ.getDoctorsOfSpecialization(specId);
        if (doctors == null) {
            return new ResponseEntity<>("Specialization not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    // Helper: turns the service's message into the right HTTP status
    private ResponseEntity<String> respond(String message, String successMessage, HttpStatus successStatus) {
        if (message.equals(successMessage)) {
            return new ResponseEntity<>(message, successStatus);
        }
        if (message.endsWith("not found")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}
