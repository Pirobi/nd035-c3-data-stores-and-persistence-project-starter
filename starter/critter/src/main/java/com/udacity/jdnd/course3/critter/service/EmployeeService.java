package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(long id) throws ChangeSetPersister.NotFoundException {
        return employeeRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    @Transactional
    public void updateAvailability(Set<DayOfWeek> days, long employeeId) throws ChangeSetPersister.NotFoundException {
        Employee rv = employeeRepository.findById(employeeId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        rv.setDaysAvailable(days);
    }

    public List<Employee> findAvailableEmployees(Set<EmployeeSkill> skills, LocalDate date){
        return employeeRepository.findAvailableEmployees(skills, date.getDayOfWeek(), skills.size());
    }
}
