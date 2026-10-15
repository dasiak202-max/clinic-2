package kigali.clinic.rw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specRepo;

    // CREATE
    public String saveSpecialization(Specialization spec) {
        if (specRepo.findByNameIgnoreCase(spec.getName()) != null) {
            return "Specialization already exists";
        }
        specRepo.save(spec);
        return "Specialization saved successfully";
    }

        @Autowired
    private DoctorRepository doctorRepo;

    // READ all
    public List<Specialization> getAllSpecializations() {
        return specRepo.findAll();
    }

        // B3
    public List<Specialization> getUnusedSpecializations() {
        return specRepo.findUnusedSpecializations();
    }

    // READ one
    public Specialization getSpecializationById(UUID id) {
        return specRepo.findById(id).orElse(null);
    }

    // UPDATE
    public String updateSpecialization(UUID id, Specialization newData) {
        Specialization existing = specRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Specialization not found";
        }
        // the new name must not belong to a DIFFERENT specialization
        Specialization sameName = specRepo.findByNameIgnoreCase(newData.getName());
        if (sameName != null && !sameName.getId().equals(id)) {
            return "Specialization name already used";
        }
        existing.setName(newData.getName());
        specRepo.save(existing);
        return "Specialization updated successfully";
    }

    // DELETE
    @Transactional
    public String deleteSpecialization(UUID id) {
        Specialization existing = specRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Specialization not found";
        }
        // Specialization OWNS the join table, so clearing its list
        // removes its rows from doctor_specialization first
        existing.getDoctors().clear();
        specRepo.delete(existing);
        return "Specialization deleted successfully";
    }

        // LINK: give a doctor this specialization
    @Transactional
    public String addDoctorToSpecialization(UUID specId, UUID doctorId) {
        Specialization spec = specRepo.findById(specId).orElse(null);
        if (spec == null) {
            return "Specialization not found";
        }
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) {
            return "Doctor not found";
        }
        boolean alreadyLinked = spec.getDoctors().stream()
                .anyMatch(d -> d.getId().equals(doctorId));
        if (alreadyLinked) {
            return "Doctor already has this specialization";
        }
        spec.getDoctors().add(doctor);   // change the OWNER's list
        specRepo.save(spec);             // Hibernate inserts a row in doctor_specialization
        return "Doctor added to specialization successfully";
    }

    // UNLINK: remove this specialization from a doctor
    @Transactional
    public String removeDoctorFromSpecialization(UUID specId, UUID doctorId) {
        Specialization spec = specRepo.findById(specId).orElse(null);
        if (spec == null) {
            return "Specialization not found";
        }
        boolean removed = spec.getDoctors().removeIf(d -> d.getId().equals(doctorId));
        if (!removed) {
            return "Doctor does not have this specialization";
        }
        specRepo.save(spec);             // Hibernate deletes the row from doctor_specialization
        return "Doctor removed from specialization successfully";
    }

    // READ: all doctors having this specialization
    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsOfSpecialization(UUID specId) {
        Specialization spec = specRepo.findById(specId).orElse(null);
        if (spec == null) {
            return null;   // controller turns this into 404
        }
        return new ArrayList<>(spec.getDoctors());
    }
}
