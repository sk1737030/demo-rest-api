package me.dongguri.demorestapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    @PostMapping
    public ResponseEntity createEvent(@RequestBody  Event event) {
        URI createdUri = linkTo(EventController.class).slash("{id}").toUri(); //URI로 만듬
        event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}
