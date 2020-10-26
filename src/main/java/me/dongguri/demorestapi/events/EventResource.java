package me.dongguri.demorestapi.events;

import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class EventResource extends RepresentationModel<Event> {
   Event event;

   public EventResource(Event event, Links... links) {
      this.event = event;
      add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
   }

}

