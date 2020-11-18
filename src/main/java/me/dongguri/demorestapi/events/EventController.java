package me.dongguri.demorestapi.events;

import me.dongguri.demorestapi.accounts.Account;
import me.dongguri.demorestapi.accounts.CurrentUser;
import me.dongguri.demorestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.modelMapper = modelMapper;
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
    }

    /**
     * Bean Serializer
     * 자바스펙준수 할경우 json으로 자동변환해준다
     */
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto,
                                      BindingResult errors,
                                      @CurrentUser Account account) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setManager(account);

        Event newEvent = this.eventRepository.save(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri(); //URI로 만듬

        EventResource eventResource = new EventResource(newEvent);

        eventResource.add(selfLinkBuilder.withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create", "profile"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler
            /*, @AuthenticationPrincipal AccountAdapter currentUser*/
            , @CurrentUser Account currentUser) {
        Page<Event> page = this.eventRepository.findAll(pageable);

        var pagedResources = assembler.toModel(page, EventResource::new);
        pagedResources.add(Link.of("/docs/index.html#resources-events-list", "profile"));

        if (currentUser != null) {
            pagedResources.add(linkTo(EventController.class).withRel("create-event"));
        }

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id,
                                   @CurrentUser Account currentUser) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Event> optionalEvent = this.eventRepository.findById(id);

        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event); // Resource 생성
        eventResource.add(Link.of("/docs/index.html#resources-events-get", "profile"));

        if (currentUser != null && event.getManager().equals(currentUser)) {
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
            // update할 수 있는 link 주기
        }

        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto, BindingResult errors,
                                      @CurrentUser Account currentUser) {
        Optional<Event> eventById = eventRepository.findById(id);

        if (eventById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existingEvent = eventById.get(); // Service를 안만들면 dirtychecking 안일어난다.

        if (currentUser != null && !existingEvent.getManager().equals(currentUser)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        this.modelMapper.map(eventDto, existingEvent);
        existingEvent.update();
        Event savedEvent = eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/doc/index.html#resources-events-update", "profile"));

        return ResponseEntity.ok().body(eventResource);
    }

    private ResponseEntity<ErrorsResource> badRequest(BindingResult errors) {

        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
