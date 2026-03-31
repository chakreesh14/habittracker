package com.chakreesh.habittracker.service;

import com.chakreesh.habittracker.entity.Habit;
import com.chakreesh.habittracker.entity.HabitLog;
import com.chakreesh.habittracker.entity.User;
import com.chakreesh.habittracker.repository.HabitLogRepository;
import com.chakreesh.habittracker.repository.HabitRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class HabitService {
    @Value("${habit.log.interval.minutes}")
    private int logIntervalMinutes;
    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public HabitService(HabitRepository habitRepository,
                        HabitLogRepository habitLogRepository,
                        AuthenticatedUserService authenticatedUserService) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public Habit createHabit(String name) {
        User currentUser = authenticatedUserService.getCurrentUser();
        String normalizedName = name.trim().toLowerCase();

        List<Habit> existing = habitRepository.findByNameIgnoreCaseAndUserId(normalizedName, currentUser.getId());
        if (!existing.isEmpty()) {
            throw new RuntimeException("Habit already exists for this user");
        }

        Habit habit = new Habit();
        habit.setName(normalizedName);
        habit.setCreatedDate(LocalDate.now());
        habit.setUser(currentUser);

        return habitRepository.save(habit);
    }

    public String logHabit(Long habitId) {

    User currentUser = authenticatedUserService.getCurrentUser();

    Habit habit = habitRepository.findByIdAndUserId(habitId, currentUser.getId())
            .orElseThrow(() -> new RuntimeException("Habit not found for this user"));

    Optional<HabitLog> lastLogOpt =
            habitLogRepository.findTopByHabitOrderByLoggedAtDesc(habit);

    LocalDateTime now = LocalDateTime.now();

    if (lastLogOpt.isPresent()) {
        LocalDateTime lastLoggedTime = lastLogOpt.get().getLoggedAt();

        long minutes = java.time.Duration.between(lastLoggedTime, now).toMinutes();

        if (minutes < logIntervalMinutes) {
                long remaining = logIntervalMinutes - minutes;
                return "Try again after " + remaining + " minutes";
            }
    }

    HabitLog log = new HabitLog();
    log.setHabit(habit);
    log.setLoggedAt(now);

    habitLogRepository.save(log);

    return currentUser.getName() + ", habit logged successfully at " + now;
}

    public List<Habit> getMyHabits() {
        User currentUser = authenticatedUserService.getCurrentUser();
        return habitRepository.findByUserId(currentUser.getId());
    }

    public int getStreak(Long habitId) {
        User currentUser = authenticatedUserService.getCurrentUser();

        Habit habit = habitRepository.findByIdAndUserId(habitId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Habit not found for this user"));

        List<HabitLog> logs = habitLogRepository.findByHabitIdOrderByLoggedAtAsc(habit.getId());

        if (logs.isEmpty()) return 0;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime latestLog = logs.get(logs.size() - 1).getLoggedAt();

        if (Duration.between(latestLog, now).toHours() >= 24) {
            return 0;
        }

        int streak = 1;
        LocalDateTime lastCounted = logs.get(0).getLoggedAt();

        for (int i = 1; i < logs.size(); i++) {
            LocalDateTime current = logs.get(i).getLoggedAt();
            long minutesDiff = Duration.between(lastCounted, current).toMinutes();

            if (minutesDiff < 24 * 60) {
                continue;
            }

            if (minutesDiff < 48 * 60) {
                streak++;
                lastCounted = current;
            } else {
                streak = 1;
                lastCounted = current;
            }
        }

        return streak;
    }

    public String getUserStreakMessage(Long habitId) {
        User currentUser = authenticatedUserService.getCurrentUser();
        int streak = getStreak(habitId);
        return currentUser.getName() + ", your streak count is " + streak;
    }
}
