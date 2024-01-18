package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Pet;
import com.uni.vetclinicapi.persistance.entity.User;
import com.uni.vetclinicapi.persistance.entity.Visit;
import com.uni.vetclinicapi.persistance.repository.PetRepository;
import com.uni.vetclinicapi.persistance.repository.UserRepository;
import com.uni.vetclinicapi.persistance.repository.VisitRepository;
import com.uni.vetclinicapi.presentation.exceptions.InvalidVisitDateException;
import com.uni.vetclinicapi.presentation.exceptions.PetNotFoundException;
import com.uni.vetclinicapi.presentation.exceptions.VetNotFoundException;
import com.uni.vetclinicapi.service.dto.FullVisitDTO;
import com.uni.vetclinicapi.service.dto.VisitDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 * Provides the necessary methods regarding CRUD operations with Visit Entities.
 * Uses the VisitRepository, PetRepository and VetRepository as a connection with the database.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VisitService {

    private final PetRepository petRepository;

    private final UserRepository userRepository;

    private final VisitRepository visitRepository;

    private final ModelMapper modelMapper;

    public FullVisitDTO addVisit(VisitDTO visitDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet pet = petRepository.findById(visitDTO.getPet().getId()).orElseThrow(() -> {
            log.warn("Attempted to get a Pet with id: {} for User with id: {}, which does not exist.",visitDTO.getPet().getId(),user.getId());
            throw new PetNotFoundException(String.format("Pet with id: %s and owner: %s does not exist!", visitDTO.getPet().getId(),user.getUsername()));
        });

        User vet = userRepository.findById(visitDTO.getVet().getId()).orElseThrow(() -> {
            log.warn("Attempted to get a Vet(User) with id: {} which does not exist.", visitDTO.getVet().getId());
            throw new VetNotFoundException(String.format("Vet with id: %s does not exist!", visitDTO.getVet().getId()));
        });

        if (visitDTO.getDate().toLocalDate().isBefore(LocalDate.now()) || visitRepository.findByDateAndTime(visitDTO.getDate(),visitDTO.getTime()).isPresent()) {
            log.warn("Attempted to create a Visit with date: {} and hour: {} which already exists or date and and hour is invalid.", visitDTO.getDate(),visitDTO.getTime());
            throw new InvalidVisitDateException(String.format("Visit with date: %s and hour: %s already exists or date and hour invalid!",visitDTO.getDate(),visitDTO.getTime()));
        }

        Visit visit = modelMapper.map(visitDTO, Visit.class);
        visit.setPet(pet);
        visit.setVet(vet);
        visit.setUser(user);
        Visit persistedVisit = visitRepository.save(visit);
        log.warn("Visit by User: {} with pet: {} and vet: {} was created successfully",user.getId(),pet.getId(),vet.getId());
        return modelMapper.map(persistedVisit, FullVisitDTO.class);
    }

    public List<FullVisitDTO> findAllVisitsForUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FullVisitDTO> fullVisitDTOList = visitRepository.findAllByUser(user)
                .stream()
                .map(visit -> modelMapper.map(visit, FullVisitDTO.class)).toList();
        if (fullVisitDTOList.isEmpty()) {
            log.info("There are no Visits present in database!");
        } else {
            log.info("{} Visits for user with id : {}, have been fetched from database.", fullVisitDTOList.size(), user.getId());
        }
        return fullVisitDTOList;
    }
}
