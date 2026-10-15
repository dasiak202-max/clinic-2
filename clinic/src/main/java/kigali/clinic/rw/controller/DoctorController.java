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

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping(value = "/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorServ;

    // CREATE  ->  POST /api/doctor/save?officeId=...
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor, @RequestParam(required =false) UUID officeId) {
        String message = doctorServ.saveDoctor(doctor, officeId);
        return respond(message, "Doctor saved successfully", HttpStatus.CREATED);
    }

    // READ all  ->  GET /api/doctor/all
    @GetMapping(value = "/all")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return new ResponseEntity<>(doctorServ.getAllDoctors(), HttpStatus.OK);
    }

        // B1  ->  GET /api/doctors/by-specialization?name=cardiology
    @GetMapping(value = "/by-specialization")
    public ResponseEntity<List<Doctor>> getBySpecialization(@RequestParam String name) {
        return new ResponseEntity<>(doctorServ.getDoctorsBySpecialization(name), HttpStatus.OK);
    }

        // B2  ->  GET /api/doctors/without-office
    @GetMapping(value = "/without-office")
    public ResponseEntity<List<Doctor>> getWithoutOffice() {
        return new ResponseEntity<>(doctorServ.getDoctorsWithoutOffice(), HttpStatus.OK);
    }

    // READ one  ->  GET /api/doctor/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id) {
        Doctor doctor = doctorServ.getDoctorById(id);
        if (doctor == null) {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    // UPDATE  ->  PUT /api/doctor/update/{id}   (optional ?officeId=...)
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor,
                                          @RequestParam(required = false) UUID officeId) {
        String message = doctorServ.updateDoctor(id, doctor, officeId);
        return respond(message, "Doctor updated successfully", HttpStatus.OK);
    }

    // DELETE  ->  DELETE /api/doctor/delete/{id}
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {
        String message = doctorServ.deleteDoctor(id);
        return respond(message, "Doctor deleted successfully", HttpStatus.OK);
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