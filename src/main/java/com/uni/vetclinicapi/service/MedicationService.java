package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Medication;
import com.uni.vetclinicapi.persistance.repository.MedicationRepository;
import com.uni.vetclinicapi.presentation.exceptions.MedicationAlreadyExistsException;
import com.uni.vetclinicapi.presentation.exceptions.MedicationInsufficientQuantity;
import com.uni.vetclinicapi.presentation.exceptions.MedicationNotFoundException;
import com.uni.vetclinicapi.service.dto.FullMedicationDTO;
import com.uni.vetclinicapi.service.dto.MedicationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Provides the necessary methods regarding CRUD operations with Medication Entities.
 * Uses the MedicationRepository as a connection with the database.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MedicationService {

    private final MedicationRepository medRepository;

    private final ModelMapper modelMapper;

    /**
     * Checks if a Medication with name like the one of MedicationDTO exists.
     * If it doesn't, Medication entity is created.
     * Else throws Medication already exists exception.
     *
     * @param medDTO - Medication data coming from request.
     * @return - FullMedicationDTO object, containing all the information for the Medication Entity.
     */

    public FullMedicationDTO create(MedicationDTO medDTO) {
        if(medRepository.findByName(medDTO.getName()).isPresent()) {
            log.warn("Attempted to create a Medication with name: {} which already exist", medDTO.getName());
            throw new MedicationAlreadyExistsException(String.format("Medication with the same name: %s already exists!", medDTO.getName()));
        }

        Medication med = modelMapper.map(medDTO, Medication.class);
        Medication persistedMed = medRepository.save(med);
        log.info("Medication with details : {}, was created!", med);
        return modelMapper.map(persistedMed, FullMedicationDTO.class);
    }

    /**
     * Deletes a medication - Returns the deleted medication.
     * Checks if medication with such id exists.
     *
     * @param medId - the id of the medication to delete.
     * @return - FullMedicationDTO object, containing all the information about the deleted Medication entity.
     */
    public FullMedicationDTO delete(UUID medId) {
        Medication medication = medRepository.findById(medId).orElseThrow(() -> {
            log.warn("Attempted to delete a Medication with id: {} , which does not exist.", medId);
            throw new MedicationNotFoundException(String.format("User with id: %s does not exist!", medId));
        });

        medRepository.deleteById(medication.getId());
        log.info("Medication with details : {}, was deleted!", medication);
        return modelMapper.map(medication, FullMedicationDTO.class);
    }

    /**
     * Returns a list with all medications.
     *
     * @return - List of FullMedicationDTO with all the information about a medication.
     */
    public List<FullMedicationDTO> getAllMedications() {
        List<Medication> medications = medRepository.findAll();
        return medications.stream().map(med -> modelMapper.map(med, FullMedicationDTO.class)).toList();
    }


    /**
     * Updates a medication's property - Returns the updated medication.
     * Checks if medication with such id exists.
     * Checks which property is changed.
     *
     * @param medId - the id of the medication to update.
     * @return - FullMedicationDTO object, containing all the information about the updated Medication entity.
     */
    public FullMedicationDTO updateMedicationProperty(UUID medId, MedicationDTO medDTO) {
        Medication medication = medRepository.findById(medId).orElseThrow(() -> {
            log.warn("Attempted to update a Medication with id: {} , which does not exist.", medId);
            throw new MedicationNotFoundException(String.format("Medication with id: %s does not exist!", medId));
        });

        updatePropertyIfNotNull(medication,medDTO.getName(),Medication::setName);
        updatePropertyIfNotNull(medication,medDTO.getDescription(),Medication::setDescription);
        updatePropertyIfNotNull(medication,medDTO.getType(),Medication::setType);
        updatePropertyIfNotNull(medication,medDTO.getQuantity(),Medication::setQuantity);

        Medication persistedMedication = medRepository.save(medication);

        log.info("Medication with details : {}, was updated!", medication);
        return modelMapper.map(persistedMedication, FullMedicationDTO.class);
    }

    private <T> void updatePropertyIfNotNull(Medication medication, T value, BiConsumer<Medication, T> setter) {
        if (value != null) {
            setter.accept(medication, value);
        }
    }

    public FullMedicationDTO updateMedicationQuantity(UUID medId, MedicationDTO medDTO) {
        Medication medication = medRepository.findById(medId).orElseThrow(() -> {
            log.warn("Attempted to update a Medication with id: {} , which does not exist.", medId);
            throw new MedicationNotFoundException(String.format("Medication with id: %s does not exist!", medId));
        });

        if (medDTO.getQuantity() > medication.getQuantity()) {
            log.warn("Insufficient quantity of a Medication with id: {}", medId);
            throw new MedicationInsufficientQuantity(String.format("Insufficient quantity of Medication with id: %s !", medId));
        } else {
            medication.setQuantity(medication.getQuantity() - medDTO.getQuantity());
        }

        Medication persistedMedication = medRepository.save(medication);

        log.info("Medication with details : {}, was updated(quantity)!", medication);
        return modelMapper.map(persistedMedication, FullMedicationDTO.class);
    }
}
