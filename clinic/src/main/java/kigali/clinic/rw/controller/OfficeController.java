package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.OfficeService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/api/offices")
public class OfficeController {


    @Autowired
    private OfficeService officeServ;

    // CREATE  ->  POST /api/office/save
    @PostMapping(value = "/save")
    public ResponseEntity<?> saveOffice(@RequestBody Office office) {
        String message = officeServ.saveOffice(office);
        if (message.equals("Office saved successfully")) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    // READ all  ->  GET /api/office/all
    @GetMapping(value = "/all")
    public ResponseEntity<List<Office>> getAllOffices() {
        return new ResponseEntity<>(officeServ.getAllOffices(), HttpStatus.OK);
    }

    // READ one  ->  GET /api/office/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id) {
        Office office = officeServ.getOfficeById(id);
        if (office == null) {
            return new ResponseEntity<>("Office not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(office, HttpStatus.OK);
    }

    // UPDATE  ->  PUT /api/office/update/{id}
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office office) {
        String message = officeServ.updateOffice(id, office);
        if (message.equals("Office updated successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if (message.equals("Office not found")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }

    // DELETE  ->  DELETE /api/office/delete/{id}
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id) {
        String message = officeServ.deleteOffice(id);
        if (message.equals("Office deleted successfully")) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        if (message.equals("Office not found")) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}
