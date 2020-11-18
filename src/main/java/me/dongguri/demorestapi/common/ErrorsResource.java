package me.dongguri.demorestapi.common;

import me.dongguri.demorestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ErrorsResource extends EntityModel<Errors> {
    Errors contents;

    public ErrorsResource(Errors errors, Link... links) {
        this.contents = errors;
        add(linkTo(IndexController.class).withRel("index"));
    }



    @Override
    public Errors getContent() {
        return this.contents;
    }
}
