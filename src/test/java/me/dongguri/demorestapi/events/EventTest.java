package me.dongguri.demorestapi.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring Rest API")
                .description("REST API development")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // given
        String name = "Event";
        String spring = "Spring";

        // when
        Event event = new Event();
        event.setName(name);
        event.setDescription("Spring");

        // that
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(spring);
    }
}