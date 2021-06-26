package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private CustomerRepository customerRepo;

    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }
    public Customer getOwnerById(long id) throws NotFoundException {
        return customerRepo.findById(id).orElseThrow(NotFoundException::new);
    }

    public Customer getOwnerByPet(Pet pet){
        return customerRepo.findByPetsContaining(pet);
    }

    @Transactional
    public Customer saveCustomer(Customer customer){
        return customerRepo.save(customer);
    }


}
