package com.chakreesh.habittracker.repository;

import com.chakreesh.habittracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUserId(Long userId);

    Optional<Habit> findByIdAndUserId(Long habitId, Long userId);

    List<Habit> findByNameIgnoreCaseAndUserId(String name, Long userId);

}