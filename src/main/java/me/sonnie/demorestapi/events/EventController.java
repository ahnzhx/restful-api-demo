package me.sonnie.demorestapi.events;

import jakarta.validation.Valid;
import me.sonnie.demorestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
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

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDto eventDto, Errors errors){
        if(errors.hasErrors()){
            return badRequest(errors);
        }
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return badRequest(errors);
        }
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
//        Event newEvent = this.eventRepository.save(event);
        event.setId(10);
        URI createUri = linkTo(EventController.class).slash(event.getId()).toUri();
        return ResponseEntity.created(createUri).body(event);

    }

    private static ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
