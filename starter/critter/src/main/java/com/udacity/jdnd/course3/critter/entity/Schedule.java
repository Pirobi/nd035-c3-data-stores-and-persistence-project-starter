package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Employee> employee = new ArrayList<>();

    @ManyToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Pet> pet = new ArrayList<>();

    @ManyToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Customer> customers = new ArrayList<>();

    @ElementCollection
    private Set<EmployeeSkill> activities = new HashSet<>();
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(List<Employee> employee) {
        this.employee = employee;
    }

    public void addEmployee(Employee employee){
        this.employee.add(employee);
        employee.getSchedule().add(this);
    }

    public void removeEmployee(Employee employee){
        this.employee.remove(employee);
        employee.getSchedule().remove(this);
    }

    public List<Pet> getPet() {
        return pet;
    }

    public void setPet(List<Pet> pet) {
        this.pet = pet;
    }

    public void addPet(Pet pet){
        this.pet.add(pet);
        pet.getSchedule().add(this);
        pet.getCustomer().getSchedule().add(this);
    }

    public void removePet(Pet pet){
        this.pet.remove(pet);
        pet.getSchedule().remove(this);
        pet.getCustomer().getSchedule().remove(this);
    }

    public Set<EmployeeSkill> getActivities() {
        return activities;
    }

    public void setActivities(Set<EmployeeSkill> activities) {
        this.activities = activities;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
