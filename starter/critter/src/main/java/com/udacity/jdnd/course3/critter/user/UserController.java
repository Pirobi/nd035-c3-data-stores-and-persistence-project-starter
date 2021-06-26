package com.udacity.jdnd.course3.critter.user;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger = Logger.getLogger(UserController.class.getName());
    @Autowired
    private UserService userService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer saved = userService.saveCustomer(convertToEntity(customerDTO));
        return convertToDto(saved);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> rv = new ArrayList<>();
        userService.getAllCustomers().forEach(customer->rv.add(convertToDto(customer)));
        return rv;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        try {
            Pet pet = petService.getPet(petId);
            return convertToDto(pet.getCustomer());
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.warning("No pet with id " + petId);
            return new CustomerDTO();
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee saved = employeeService.saveEmployee(convertToEntity(employeeDTO));
        return convertToDto(saved);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            return convertToDto(employeeService.getEmployeeById(employeeId));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.log(Level.WARNING, "Could not find employee with id " + employeeId, e);
            return new EmployeeDTO();
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            employeeService.updateAvailability(daysAvailable, employeeId);
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.warning("Could not find employee with id " + employeeId);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> rv = new ArrayList<>();
        List<Employee> employees = employeeService.findAvailableEmployees(employeeDTO.getSkills(), employeeDTO.getDate());
        if(employees != null){
            employees.forEach(employee->rv.add(convertToDto(employee)));
        }
        return rv;
    }

    private CustomerDTO convertToDto(Customer customer){
        CustomerDTO rv = new CustomerDTO();
        BeanUtils.copyProperties(customer, rv);
        if(customer.getPets() != null) {
            customer.getPets().forEach(pet -> rv.getPetIds().add(pet.getId()));
        }
        return rv;
    }

    private Customer convertToEntity(CustomerDTO dto){
        Customer rv = new Customer();
        BeanUtils.copyProperties(dto, rv);
        if(dto.getPetIds() != null) {
            rv.getPets().addAll(petService.findPetsById(dto.getPetIds()));
        }
        return rv;
    }

    private EmployeeDTO convertToDto(Employee employee){
        EmployeeDTO rv = new EmployeeDTO();
        BeanUtils.copyProperties(employee, rv);
        return rv;
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO){
        Employee rv = new Employee();
        BeanUtils.copyProperties(employeeDTO, rv);
        return rv;
    }
}
