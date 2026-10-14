package kigali.clinic.rw.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepo;

    // CREATE
    public String saveOffice(Office office) {
        if (officeRepo.findByOfficeNumber(office.getOfficeNumber()) != null) {
            return "Office already exists";
        }
        officeRepo.save(office);
        return "Office saved successfully";
    }

    // READ all
    public List<Office> getAllOffices() {
        return officeRepo.findAll();
    }

    // READ one
    public Office getOfficeById(UUID id) {
        return officeRepo.findById(id).orElse(null);
    }

    // UPDATE
    public String updateOffice(UUID id, Office newData) {
        Office existing = officeRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Office not found";
        }
        // the new number must not belong to a DIFFERENT office
        Office sameNumber = officeRepo.findByOfficeNumber(newData.getOfficeNumber());
        if (sameNumber != null && !sameNumber.getId().equals(id)) {
            return "Office number already used";
        }
        existing.setName(newData.getName());
        existing.setOfficeNumber(newData.getOfficeNumber());
        officeRepo.save(existing);
        return "Office updated successfully";
    }

    // DELETE
    public String deleteOffice(UUID id) {
        Office existing = officeRepo.findById(id).orElse(null);
        if (existing == null) {
            return "Office not found";
        }
        // the doctor table has a FK to this office, so the database would refuse
        if (existing.getDoctor() != null) {
            return "Office is assigned to a doctor";
        }
        officeRepo.deleteById(id);
        return "Office deleted successfully";
    }
}