package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public List<Pet> findPetsById(Collection<Long> ids){
        return petRepository.findAllById(ids);
    }

    public List<Pet> findAllPets(){
        return petRepository.findAll();
    }

    public List<Pet> findPetByOwnerId(long id){
        return petRepository.findAllByCustomerId(id);
    }

    public Pet getPet(long id) throws ChangeSetPersister.NotFoundException {
        return petRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    @Transactional
    public Pet createPet(Pet pet){
        return petRepository.save(pet);
    }
}
