package com.udacity.jdnd.course3.critter.schedule;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    Logger logger = Logger.getLogger(ScheduleController.class.getName());
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
       return convertToDTO(scheduleService.createSchedule(convertToEntity(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> rv = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getSchedules();
        if(schedules != null){
            schedules.forEach(schedule->rv.add(convertToDTO(schedule)));
        }
        return rv;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleDTO> rv = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getScheduleForPet(petId);
        if(schedules != null){
            schedules.forEach(schedule->rv.add(convertToDTO(schedule)));
        }
        return rv;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleDTO> rv = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);
        if(schedules != null){
            schedules.forEach(schedule->rv.add(convertToDTO(schedule)));
        }
        return rv;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleDTO> rv = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);
        if(schedules != null){
            schedules.forEach(schedule->rv.add(convertToDTO(schedule)));
        }
        return rv;
    }

    public ScheduleDTO convertToDTO(Schedule schedule){
        ScheduleDTO rv = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, rv);
        for (Pet pet : schedule.getPet()) {
            rv.getPetIds().add(pet.getId());
        }
        for (Employee employee : schedule.getEmployee()){
            rv.getEmployeeIds().add(employee.getId());
        }
        return rv;
    }

    public Schedule convertToEntity(ScheduleDTO dto){
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(dto, schedule);
        for (Long id : dto.getEmployeeIds()) {
            try {
                schedule.addEmployee(employeeService.getEmployeeById(id));
            } catch (ChangeSetPersister.NotFoundException e) {
                logger.warning("Could not find employee with id " + id);
            }
        }
        for(Long id : dto.getPetIds()){
            try {
                schedule.addPet(petService.getPet(id));
            } catch (ChangeSetPersister.NotFoundException e) {
                logger.warning("Could not find pet with id " + id);
            }
        }
        return schedule;
    }
}
