package me.sonnie.demorestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/events/", produces = "application/json")
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto){
        Event event = modelMapper.map(eventDto, Event.class);
//        Event newEvent = this.eventRepository.save(event);
        // temp bc db connection is not made yet
        event.setId(10);
        URI createUri = linkTo(EventController.class).slash(event.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);

    }
}
