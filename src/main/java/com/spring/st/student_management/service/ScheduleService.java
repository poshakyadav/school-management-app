package com.spring.st.student_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.st.student_management.model.Schedule;
import com.spring.st.student_management.repository.ScheduleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Schedule findById(Long id) {
        Optional<Schedule> schedule = scheduleRepository.findById(id);
        return schedule.orElseThrow(() -> new RuntimeException("Schedule not found for ID: " + id));
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }
}

