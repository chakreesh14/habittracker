package com.chakreesh.habittracker.controller;

import com.chakreesh.habittracker.dto.CreateHabitRequest;
import com.chakreesh.habittracker.entity.Habit;
import com.chakreesh.habittracker.service.HabitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<?> createHabit(@RequestBody CreateHabitRequest request) {
        try {
            return ResponseEntity.ok(habitService.createHabit(request.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeHabit(@Parameter(description = "Please enter habit ID to mark as complete", required = true) 
                                           @PathVariable Long id) {
        try {
            return ResponseEntity.ok(habitService.logHabit(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Habit>> getMyHabits() {
        return ResponseEntity.ok(habitService.getMyHabits());
    }

    @GetMapping("/{id}/streak")
    public ResponseEntity<?> getStreak(@Parameter(description = "Please enter habit ID to view streak", required = true)
                                       @PathVariable Long id) {
        try {
            return ResponseEntity.ok(habitService.getUserStreakMessage(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
