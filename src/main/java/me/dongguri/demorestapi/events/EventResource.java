package me.dongguri.demorestapi.events;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class EventResource extends EntityModel<Event> {
    Event content;

    public EventResource(Event event) {
        this.content = event;
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }

    @Override
    public Event getContent() {
        return this.content;
    }
}

