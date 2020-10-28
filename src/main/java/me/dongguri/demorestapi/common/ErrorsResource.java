package me.dongguri.demorestapi.common;

import me.dongguri.demorestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {

    public ErrorsResource(Errors content, Link... links) {
        of(content, links);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

}
