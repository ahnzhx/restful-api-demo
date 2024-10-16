package me.sonnie.demorestapi.index;

import me.sonnie.demorestapi.events.EventController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
//@RequestMapping(produces = "application/json")
public class IndexController {

    @GetMapping("/api/")
    public EntityModel<RepresentationModel> index() {
        var index = new RepresentationModel();
        index.add(linkTo(EventController.class).withRel("events"));
        return EntityModel.of(index);
    }
}
