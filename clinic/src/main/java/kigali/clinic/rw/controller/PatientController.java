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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.PatientService;

@RestController
@RequestMapping(value = "/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientServ;

    // CREATE  ->  POST /api/patient/save
    @PostMapping(value = "/save")
    public ResponseEntity<?> savePatient(@RequestBody Patient patient) {
        String message = patientServ.savePatient(patient);
        return respond(message, "Patient saved successfully", HttpStatus.CREATED);
    }

    // READ all  ->  GET /api/patient/all
    @GetMapping(value = "/all")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return new ResponseEntity<>(patientServ.getAllPatients(), HttpStatus.OK);
    }

    @GetMapping(value = "/by-last-name")
    public ResponseEntity<List<Patient>> getByLastName(@RequestParam String lastName) {
        return new ResponseEntity<>(patientServ.getPatientsByLastName(lastName), HttpStatus.OK);
    }

    // READ one  ->  GET /api/patient/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable UUID id) {
        Patient patient = patientServ.getPatientById(id);
        if (patient == null) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    // UPDATE  ->  PUT /api/patient/update/{id}
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        String message = patientServ.updatePatient(id, patient);
        return respond(message, "Patient updated successfully", HttpStatus.OK);
    }

    // DELETE  ->  DELETE /api/patient/delete/{id}
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable UUID id) {
        String message = patientServ.deletePatient(id);
        return respond(message, "Patient deleted successfully", HttpStatus.OK);
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
