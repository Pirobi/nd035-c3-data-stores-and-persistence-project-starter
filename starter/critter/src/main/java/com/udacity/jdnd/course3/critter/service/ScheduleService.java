package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    PetService petService;

    @Transactional
    public Schedule createSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedules(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForEmployee(Long id){
        return scheduleRepository.findAllByEmployee_Id(id);
    }

    public List<Schedule> getScheduleForPet(Long id){
        return scheduleRepository.findAllByPet_Id(id);
    }

    public List<Schedule> getScheduleForCustomer(Long id){
        return scheduleRepository.findAllByCustomers_Id(id);
    }
}
