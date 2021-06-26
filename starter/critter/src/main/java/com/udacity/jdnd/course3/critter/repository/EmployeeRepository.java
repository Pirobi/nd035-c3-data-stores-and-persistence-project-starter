package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("Select e from Employee e left join e.skills s join e.daysAvailable d where s in :skills and d = :dayOfWeek group by e having count(s) = :skillSize")
    List<Employee> findAvailableEmployees(@Param("skills")Collection<EmployeeSkill> skills, @Param("dayOfWeek")DayOfWeek dayOfWeek, long skillSize);
}
