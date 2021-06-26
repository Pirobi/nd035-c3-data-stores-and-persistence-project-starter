package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.user.UserController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    Logger logger = Logger.getLogger(PetController.class.getName());
    @Autowired
    PetService petService;
    @Autowired
    UserService userService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        return convertToDto(petService.createPet(convertToEntity(petDTO)));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            return convertToDto(petService.getPet(petId));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.log(Level.WARNING, "Could not find pet by id " + petId, e);
            return new PetDTO();
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> rv = new ArrayList<>();
        List<Pet> pets = petService.findAllPets();
        if(pets != null){
            for (Pet pet : pets) {
                rv.add(convertToDto(pet));
            }
        }
        return rv;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<PetDTO> rv = new ArrayList<>();
        List<Pet> pets = petService.findPetByOwnerId(ownerId);
        if(pets != null){
            pets.forEach(pet->rv.add(convertToDto(pet)));
        }
        return rv;
    }

    private Pet convertToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);

        try {
            pet.setCustomer(userService.getOwnerById(petDTO.getOwnerId()));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.warning("No owner for this pet!");
        }
        return pet;
    }

    private PetDTO convertToDto(Pet pet){
        PetDTO dto = new PetDTO();
        BeanUtils.copyProperties(pet, dto);
        dto.setOwnerId(pet.getCustomer().getId());
        return dto;
    }
}
