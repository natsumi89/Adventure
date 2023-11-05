package com.example.Adventure.controller;

import com.example.Adventure.domain.Events;
import com.example.Adventure.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/event/{eventId}")
    public String toShowEvents(@PathVariable Integer eventId, Model model) {
        Events events = eventService.load(eventId);
        model.addAttribute("events",events);
        return "events";
    }

}
