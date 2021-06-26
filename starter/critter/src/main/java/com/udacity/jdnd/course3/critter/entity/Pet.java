package com.udacity.jdnd.course3.critter.entity;

import com.udacity.jdnd.course3.critter.pet.PetType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private PetType type;
    @ManyToOne
    @JoinColumn(name="ownerId")
    private Customer customer;
    private LocalDate birthDate;
    private String notes;

    @ManyToMany
    @JoinTable(
            name = "Pet_Schedule",
            joinColumns = { @JoinColumn(name = "pet_id") },
            inverseJoinColumns = { @JoinColumn(name = "schedule_id") }
    )
    private List<Schedule> schedule = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getPets().add(this);
    }

    public void removeCustomer(Customer customer){
        this.customer = null;
        customer.getPets().remove(this);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }
}
