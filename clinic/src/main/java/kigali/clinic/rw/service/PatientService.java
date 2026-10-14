package kigali.clinic.rw.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    // CREATE
    public String savePatient(Patient patient) {
        patientRepo.save(patient);
        return "Patient saved successfully";
    }

    // READ all
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    // READ one
    public Patient getPatientById(UUID id) {
        return patientRepo.findById(id).orElse(null);
    }

    // UPDATE
    public String updatePatient(UUID id, Patient newData) {
        Patient existing = patientRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Patient not found";
        }
        existing.setFirstName(newData.getFirstName());
        existing.setLastName(newData.getLastName());
        existing.setDateOfBirth(newData.getDateOfBirth());
        patientRepo.save(existing);
        return "Patient updated successfully";
    }

    // DELETE
    public String deletePatient(UUID id) {
        Patient existing = patientRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Patient not found";
        }
        // appointment.patient_id points here, so the database would refuse
        if (!existing.getAppointments().isEmpty()) {
            return "Patient has appointments and cannot be deleted";
        }
        patientRepo.delete(existing);
        return "Patient deleted successfully";
    }
}