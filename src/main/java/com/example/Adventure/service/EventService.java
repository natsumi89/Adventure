package com.example.Adventure.service;

import com.example.Adventure.domain.Events;
import com.example.Adventure.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    public Events load(Integer eventId) {
        return eventRepository.load(eventId);
    }

    public List<Events> findAll() {
        return eventRepository.findAll();
    }
}
