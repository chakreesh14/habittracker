package com.chakreesh.habittracker.repository;

import com.chakreesh.habittracker.entity.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    List<HabitLog> findByHabitIdOrderByLoggedAtAsc(Long habitId);

    List<HabitLog> findByHabitIdOrderByLoggedAtDesc(Long habitId);

    Optional<HabitLog> findTopByHabitIdOrderByLoggedAtDesc(Long habitId);;

}